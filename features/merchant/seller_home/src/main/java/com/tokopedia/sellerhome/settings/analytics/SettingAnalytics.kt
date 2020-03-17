package com.tokopedia.sellerhome.settings.analytics

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.tokopedia.iris.IrisAnalytics
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.sellerhome.analytic.TrackingConstant
import com.tokopedia.sellerhome.settings.view.uimodel.base.SettingShopInfoClickTrackable
import com.tokopedia.sellerhome.settings.view.uimodel.base.SettingShopInfoImpressionTrackable
import com.tokopedia.track.TrackApp

fun <R, T : SettingShopInfoImpressionTrackable> R.sendSettingShopInfoImpressionTracking(uiModel: T, context: Context) {
    when(this) {
        is ImageView -> {
            addOnImpressionListener(uiModel.impressHolder) { uiModel.sendShopInfoImpressionData(context) }
        }
        is View -> {
            addOnImpressionListener(uiModel.impressHolder) { uiModel.sendShopInfoImpressionData(context) }
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

private fun SettingShopInfoImpressionTrackable.sendShopInfoImpressionData(context: Context) {
    val map = mapOf(
            TrackingConstant.EVENT to impressionEventName,
            TrackingConstant.EVENT_CATEGORY to impressionEventCategory,
            TrackingConstant.EVENT_ACTION to impressionEventAction,
            TrackingConstant.EVENT_LABEL to impressionEventLabel
    )
    IrisAnalytics.getInstance(context).sendEvent(map)
}
