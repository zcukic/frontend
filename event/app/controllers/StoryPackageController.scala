package controllers

import common._
import play.api.mvc.{ Action, Controller }
import model._

object StoryPackageController extends Controller with Logging {

  def forContent(contentId: String) = Action { implicit request =>

    val events = Event.mongo.withContent(contentId)

    events.sortBy(_.startDate.getMillis)
    //Now doing this in admin tool
    //events.foreach(_.content.sortBy(_.importance))

    Cached(60) {
      val html = views.html.fragments.storyPackage(events)
      request.getQueryString("callback").map { callback =>
        JsonComponent(html)
      } getOrElse {
        Cached(60) {
          Ok(Compressed(html))
        }
      }
    }

  }
}