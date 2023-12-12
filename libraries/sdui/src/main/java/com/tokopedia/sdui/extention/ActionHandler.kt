package com.tokopedia.sdui.extention

import android.content.Context
import com.google.gson.JsonObject
import com.tokopedia.applink.RouteManager
import com.tokopedia.track.TrackApp
import com.yandex.div.core.DivActionHandler
import com.yandex.div.core.DivViewFacade
import com.yandex.div2.DivAction
import com.yandex.div2.DivSightAction
import com.yandex.div2.DivVisibilityAction
import java.net.URI

class ActionHandler(private val contextDivAction: Context?): DivActionHandler() {

    object APPLINK {
        const val HOST_ROUTE = "route"
        const val QUERY_SEPARATOR = "&&"
        const val IDENTIFIER_ANDROID_APPLINK = "android_applink="
        const val IDENTIFIER_APPLINK = "applink="
    }
    override fun handleAction(action: DivAction, view: DivViewFacade): Boolean {
        RouteManager.route(contextDivAction, getApplink(parseClickActionUrl(action.url?.rawValue.toString())))
        return super.handleAction(action, view)
    }

    override fun handleAction(
        action: DivVisibilityAction,
        view: DivViewFacade,
        actionUid: String
    ): Boolean {
        return super.handleAction(action, view, actionUid)
    }

    override fun handleAction(action: DivSightAction, view: DivViewFacade): Boolean {
        //Send impression tracker
        return super.handleAction(action, view)
    }

    private fun getApplink(applinkActionStr: String): String{
        return applinkActionStr
    }

    private fun sendTracker(trackerPayload: JsonObject){
        //Convert JSON to Object
        //TrackApp.getInstance().gtm.sendGeneralEvent(null)
    }

    private fun parseClickActionUrl(actionUrlString: String):String{
        var deeplink = ""
        val deeplinkUri = URI.create(actionUrlString)
        if(deeplinkUri.host.equals(APPLINK.HOST_ROUTE)){
            if(deeplinkUri.query.contains(APPLINK.IDENTIFIER_ANDROID_APPLINK)){
                deeplink = (deeplinkUri.query.split(APPLINK.IDENTIFIER_ANDROID_APPLINK))[1]
            }else if(deeplinkUri.query.contains(APPLINK.IDENTIFIER_APPLINK)){
                deeplink = (deeplinkUri.query.split(APPLINK.IDENTIFIER_APPLINK))[1]
            }
        }
        return deeplink
    }
}

