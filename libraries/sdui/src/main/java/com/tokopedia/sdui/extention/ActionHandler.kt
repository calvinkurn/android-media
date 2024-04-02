package com.tokopedia.sdui.extention

import android.content.Context
import android.net.Uri
import com.tokopedia.applink.RouteManager
import com.tokopedia.sdui.interfaces.SDUITrackingInterface
import com.tokopedia.sdui.utils.DivActionUtils
import com.tokopedia.track.TrackApp
import com.yandex.div.core.DivActionHandler
import com.yandex.div.core.DivViewFacade
import com.yandex.div.json.expressions.Expression
import com.yandex.div2.DivSightAction
import org.json.JSONObject


class ActionHandler(
    private val contextDivAction: Context?,
    private val sduiTrackingInterface: SDUITrackingInterface? = null,
    private val customActionInterface: CustomActionInterface? = null
) : DivActionHandler() {

    companion object {
        const val HOST_ROUTE = "route"
        const val IDENTIFIER_ANDROID_APPLINK = "android_applink="
        const val IDENTIFIER_APPLINK = "applink="
        const val KEY_TRACKING_DATA = "tracking_data"
        const val CUSTOM_ACTION = "custom_action"
        const val ACTION = "action"
    }

    override fun handleAction(action: DivSightAction, view: DivViewFacade): Boolean {
        onHandleAction(action.url, action.payload, view)
        return super.handleAction(action, view)
    }

    override fun handleAction(
        action: DivSightAction,
        view: DivViewFacade,
        actionUid: String
    ): Boolean {
        sendTracker(action.payload)
        return super.handleAction(action, view, actionUid)
    }

    private fun onHandleAction(url: Expression<Uri>?, payload: JSONObject?, view: DivViewFacade) {
        val mUrl = url?.evaluate(view.expressionResolver)

        when (mUrl?.authority) {
            CUSTOM_ACTION -> onHandleCustomAction(mUrl)
            HOST_ROUTE -> onHandleRoute(mUrl)
        }

        if (sduiTrackingInterface != null) {
            sduiTrackingInterface.onViewClick(payload)
        } else {
            sendTracker(payload)
        }

        //Send impression tracker
        if (sduiTrackingInterface != null) {
            sduiTrackingInterface.onViewVisible(payload)
        } else {
            sendTracker(payload)
        }
    }

    private fun onHandleRoute(url: Uri) {
        RouteManager.route(contextDivAction, parseClickRedirectionUrl(url))
    }

    private fun onHandleCustomAction(url: Uri?) {
        customActionInterface?.onHandleCustomAction(url?.getQueryParameter(ACTION))
    }

    private fun sendTracker(trackerPayload: JSONObject?) {
        //Convert JSON to Hash Map
        try {
            if (trackerPayload != null) {
                val trackingData = trackerPayload.getJSONObject(KEY_TRACKING_DATA)
                val evnetDataMap: HashMap<String, Any> = DivActionUtils.toMap(trackingData)
                TrackApp.getInstance().gtm.sendGeneralEvent(evnetDataMap)
            }
        } catch (_: Exception) {
        }
    }

    private fun parseClickRedirectionUrl(uri: Uri): String {
        var deeplink = ""
        try {
            uri.query?.let {
                if (it.contains(IDENTIFIER_ANDROID_APPLINK)) {
                    deeplink = (it.split(IDENTIFIER_ANDROID_APPLINK))[1]
                } else if (it.contains(IDENTIFIER_APPLINK)) {
                    deeplink = (it.split(IDENTIFIER_APPLINK))[1]
                }
            }
        } catch (_: Exception) {
        }
        return deeplink
    }
}

