package com.tokopedia.seller.menu.common.analytics

import android.view.View
import android.widget.ImageView
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingShopInfoClickTrackable
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingShopInfoImpressionTrackable
import com.tokopedia.track.TrackApp

/**
 * Sellerapp Navigation Revamp
 * Data layer docs
 * https://docs.google.com/spreadsheets/d/1AZjuQ_dg25EvEEWmE8MPMo0f1_DT4IyZPaNpt4cxidA/edit#gid=0
 */

inline fun <R, T : SettingShopInfoImpressionTrackable> R.sendSettingShopInfoImpressionTracking(uiModel: T, crossinline action: (T) -> Unit) {
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
             SettingTrackingConstant.EVENT to clickEventName,
             SettingTrackingConstant.EVENT_CATEGORY to clickEventCategory,
             SettingTrackingConstant.EVENT_ACTION to clickEventAction,
             SettingTrackingConstant.EVENT_LABEL to clickEventLabel,
             SettingTrackingConstant.USER_ID to clickEventUserId,
             SettingTrackingConstant.SHOP_ID to clickEventShopId,
             SettingTrackingConstant.SHOP_TYPE to clickEventShopType
    )
    TrackApp.getInstance().gtm.sendGeneralEvent(map)
}

fun SettingShopInfoImpressionTrackable.sendShopInfoImpressionData() {
    val map = mapOf(
             SettingTrackingConstant.EVENT to impressionEventName,
             SettingTrackingConstant.EVENT_CATEGORY to impressionEventCategory,
             SettingTrackingConstant.EVENT_ACTION to impressionEventAction,
             SettingTrackingConstant.EVENT_LABEL to impressionEventLabel
    )
    TrackApp.getInstance().gtm.sendGeneralEvent(map)
}

fun sendSettingClickBackButtonTracking() {
    sendTrackingManual(
            eventName = SettingTrackingConstant.CLICK_SHOP_SETTING,
            eventCategory = SettingTrackingConstant.SETTINGS,
            eventAction = "${SettingTrackingConstant.CLICK} ${SettingTrackingConstant.BACK_ARROW}",
            eventLabel = ""
    )
}

fun sendShopInfoClickNextButtonTracking() {
    sendTrackingManual(
            eventName = SettingTrackingConstant.CLICK_NAVIGATION_DRAWER,
            eventCategory = SettingTrackingConstant.OTHERS_TAB,
            eventAction = "${SettingTrackingConstant.CLICK} ${SettingTrackingConstant.SHOP_ARROW}",
            eventLabel = ""
    )
}

fun sendClickShopNameTracking() {
    sendTrackingManual(
            eventName = SettingTrackingConstant.CLICK_NAVIGATION_DRAWER,
            eventCategory = SettingTrackingConstant.OTHERS_TAB,
            eventAction = "${SettingTrackingConstant.CLICK} ${SettingTrackingConstant.SHOP_NAME}",
            eventLabel = ""
    )
}

private fun sendTrackingManual(eventName: String,
                       eventCategory: String,
                       eventAction: String,
                       eventLabel: String) {
    val map = mapOf(
             SettingTrackingConstant.EVENT to eventName,
             SettingTrackingConstant.EVENT_CATEGORY to eventCategory,
             SettingTrackingConstant.EVENT_ACTION to eventAction,
             SettingTrackingConstant.EVENT_LABEL to eventLabel
    )
    TrackApp.getInstance().gtm.sendGeneralEvent(map)
}
