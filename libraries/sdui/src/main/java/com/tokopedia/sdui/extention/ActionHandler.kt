package com.tokopedia.sdui.extention

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.applink.RouteManager
import com.tokopedia.sdui.utils.DivActionUtils
import com.tokopedia.track.TrackApp
import com.yandex.div.core.DivActionHandler
import com.yandex.div.core.DivViewFacade
import com.yandex.div2.DivAction
import com.yandex.div2.DivSightAction
import org.json.JSONObject
import com.yandex.div2.DivVisibilityAction
import java.net.URI


class ActionHandler(private val contextDivAction: Context?): DivActionHandler() {

    object APPLINK {
        const val HOST_ROUTE = "route"
        const val QUERY_SEPARATOR = "&&"
        const val IDENTIFIER_ANDROID_APPLINK = "android_applink="
        const val IDENTIFIER_APPLINK = "applink="
        const val KEY_TRACKING_DATA = "tracking_data"
    }
    override fun handleAction(action: DivAction, view: DivViewFacade): Boolean {
        RouteManager.route(contextDivAction, getApplink(parseClickActionUrl(action.url?.rawValue.toString())))
        sendTracker(action.payload)
        return super.handleAction(action, view)
    }

    override fun handleAction(
        action: DivVisibilityAction,
        view: DivViewFacade,
        actionUid: String
    ): Boolean {
        sendTracker(action.payload)
        return super.handleAction(action, view, actionUid)
    }

    override fun handleAction(action: DivSightAction, view: DivViewFacade): Boolean {
        //Send impression tracker
        sendTracker(action.payload)
        return super.handleAction(action, view)
    }

    private fun getApplink(applinkActionStr: String): String{
        return applinkActionStr
    }

    private fun sendTracker(trackerPayload: JSONObject?){
        //Convert JSON to Hash Map
        if(trackerPayload != null) {
            val trackingData = trackerPayload.getJSONObject(APPLINK.KEY_TRACKING_DATA)
            val evnetDataMap: HashMap<String, Any> = DivActionUtils.toMap(trackingData)
            TrackApp.getInstance().gtm.sendGeneralEvent(evnetDataMap)
        }
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

