package com.tokopedia.sellerhome.settings.analytics

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.tokopedia.analyticsdebugger.debugger.IrisLogger
import com.tokopedia.iris.data.db.mapper.TrackingMapper
import com.tokopedia.iris.data.db.table.Tracking
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.sellerhome.analytic.TrackingConstant
import com.tokopedia.sellerhome.settings.view.uimodel.base.SettingShopInfoClickTrackable
import com.tokopedia.sellerhome.settings.view.uimodel.base.SettingShopInfoImpressionTrackable
import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSessionInterface
import org.json.JSONObject

fun <R, T : SettingShopInfoImpressionTrackable> R.sendSettingShopInfoImpressionTracking(uiModel: T, action: (T) -> Unit) {
    when(this) {
        is ImageView -> {
            addOnImpressionListener(uiModel.impressHolder) { action.invoke(uiModel) }
        }
        is View -> {
            addOnImpressionListener(uiModel.impressHolder) { action.invoke(uiModel) }
        }
    }
}

fun <T : SettingShopInfoClickTrackable> T.sendSettingShopInfoClickTracking() {
    val map = mapOf(
            TrackingConstant.EVENT to clickEventName,
            TrackingConstant.EVENT_CATEGORY to clickEventCategory,
            TrackingConstant.EVENT_ACTION to clickEventAction,
            TrackingConstant.EVENT_LABEL to clickEventLabel
    )
    TrackApp.getInstance().gtm.sendGeneralEvent(map)
}

fun sendTrackingManual(eventName: String,
                       eventCategory: String,
                       eventAction: String,
                       eventLabel: String) {
    val map = mapOf(
            TrackingConstant.EVENT to eventName,
            TrackingConstant.EVENT_CATEGORY to eventCategory,
            TrackingConstant.EVENT_ACTION to eventAction,
            TrackingConstant.EVENT_LABEL to eventLabel
    )
    TrackApp.getInstance().gtm.sendGeneralEvent(map)
}

fun SettingShopInfoImpressionTrackable.sendShopInfoImpressionData(context: Context, userSession: UserSessionInterface) {
    val dataSize = 1
    val eventObject = JSONObject().apply {
        put(TrackingConstant.EVENT, impressionEventName)
        put(TrackingConstant.EVENT_CATEGORY, impressionEventCategory)
        put(TrackingConstant.EVENT_ACTION, impressionEventAction)
        put(TrackingConstant.EVENT_LABEL, impressionEventLabel)
    }
    val tracking = Tracking(
            eventObject.toString(),
            userSession.deviceId,
            userSession.userId,
            System.currentTimeMillis()
    )
    val request = TrackingMapper().transformListEvent(listOf(tracking))
    IrisLogger.getInstance(context).putSendIrisEvent(request, dataSize)
}
