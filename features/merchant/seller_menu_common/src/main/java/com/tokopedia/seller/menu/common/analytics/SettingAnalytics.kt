package com.tokopedia.seller.menu.common.analytics

import android.view.View
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.seller.menu.common.view.uimodel.base.*
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.unifycomponents.ImageUnify

/**
 * Sellerapp Navigation Revamp
 * Data layer docs
 * https://docs.google.com/spreadsheets/d/1AZjuQ_dg25EvEEWmE8MPMo0f1_DT4IyZPaNpt4cxidA/edit#gid=0
 */

inline fun <R, T : SettingShopInfoImpressionTrackable> R.sendSettingShopInfoImpressionTracking(
    uiModel: T,
    crossinline action: (T) -> Unit
) {
    when (this) {
        is ImageUnify -> {
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

private fun sendTrackingManual(
    eventName: String,
    eventCategory: String,
    eventAction: String,
    eventLabel: String
) {
    val map = mapOf(
        SettingTrackingConstant.EVENT to eventName,
        SettingTrackingConstant.EVENT_CATEGORY to eventCategory,
        SettingTrackingConstant.EVENT_ACTION to eventAction,
        SettingTrackingConstant.EVENT_LABEL to eventLabel
    )
    TrackApp.getInstance().gtm.sendGeneralEvent(map)
}

fun sendEventImpressionStatisticMenuItem(userId: String) {
    val event = TrackAppUtils.gtmData(
        SettingTrackingConstant.VIEW_STATISTIC_IRIS,
        SettingTrackingConstant.SELLER_APP_LAINNYA,
        SettingTrackingConstant.IMPRESSION_MENU_STATISTIC,
        ""
    )
    event[SettingTrackingConstant.KEY_BUSINESS_UNIT] = SettingTrackingConstant.PHYSICAL_GOODS
    event[SettingTrackingConstant.KEY_CURRENT_SITE] = SettingTrackingConstant.TOKOPEDIASELLER
    event[SettingTrackingConstant.USER_ID] = userId

    TrackApp.getInstance().gtm.sendGeneralEvent(event)
}

fun sendEventClickStatisticMenuItem(userId: String) {
    val event = TrackAppUtils.gtmData(
        SettingTrackingConstant.CLICK_STATISTIC,
        SettingTrackingConstant.SELLER_APP_LAINNYA,
        SettingTrackingConstant.CLICK_MENU_STATISTIC,
        ""
    )
    event[SettingTrackingConstant.KEY_BUSINESS_UNIT] = SettingTrackingConstant.PHYSICAL_GOODS
    event[SettingTrackingConstant.KEY_CURRENT_SITE] = SettingTrackingConstant.TOKOPEDIASELLER
    event[SettingTrackingConstant.USER_ID] = userId

    TrackApp.getInstance().gtm.sendGeneralEvent(event)
}

object NewOtherMenuTracking {
    fun sendEventImpressionShopAvatar() {
        val event = TrackAppUtils.gtmData(
            SettingTrackingConstant.VIEW_NAVIGATION_IRIS,
            SettingTrackingConstant.OTHERS_TAB,
            "${SettingTrackingConstant.IMPRESSION} ${SettingTrackingConstant.SHOP_PICTURE}",
            ""
        )

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun sendEventImpressionSaldoBalance() {
        val event = TrackAppUtils.gtmData(
            SettingTrackingConstant.VIEW_NAVIGATION_IRIS,
            SettingTrackingConstant.OTHERS_TAB,
            "${SettingTrackingConstant.IMPRESSION} ${SettingTrackingConstant.ON_SALDO}",
            ""
        )

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun sendEventImpressionTopadsBalance() {
        val event = TrackAppUtils.gtmData(
            SettingTrackingConstant.VIEW_NAVIGATION_IRIS,
            SettingTrackingConstant.OTHERS_TAB,
            "${SettingTrackingConstant.IMPRESSION} ${SettingTrackingConstant.ON_TOPADS_CREDIT}",
            ""
        )

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun sendEventImpressionShopStatus(shopType: ShopType) {
        val eventAction =
            "${SettingTrackingConstant.IMPRESSION} ${SettingTrackingConstant.SHOP_STATE} - ${shopType.mapToEventCategory()}"

        val event = TrackAppUtils.gtmData(
            SettingTrackingConstant.VIEW_NAVIGATION_IRIS,
            SettingTrackingConstant.OTHERS_TAB,
            eventAction,
            ""
        )

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    private fun ShopType.mapToEventCategory(): String {
        val shopType = when (this) {
            is PowerMerchantStatus -> SettingTrackingConstant.GOLD_MERCHANT
            is RegularMerchant -> SettingTrackingConstant.REGULAR
            is ShopType.OfficialStore -> SettingTrackingConstant.OFFICIAL_STORE
            else -> ""
        }
        val shopStatus = when (this) {
            is PowerMerchantStatus.Active -> SettingTrackingConstant.ACTIVE
            is PowerMerchantStatus.NotActive -> SettingTrackingConstant.INACTIVE
            is RegularMerchant.NeedUpgrade -> SettingTrackingConstant.UPGRADE
            else -> ""
        }
        return "$shopType $shopStatus"
    }

    fun sendEventClickShopReputationBadge() {
        val event = TrackAppUtils.gtmData(
            SettingTrackingConstant.CLICK_NAVIGATION_DRAWER,
            SettingTrackingConstant.OTHERS_TAB,
            "${SettingTrackingConstant.CLICK} ${SettingTrackingConstant.SHOP_BADGE}",
            ""
        )

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun sendEventClickTotalFollowers() {
        val event = TrackAppUtils.gtmData(
            SettingTrackingConstant.CLICK_NAVIGATION_DRAWER,
            SettingTrackingConstant.OTHERS_TAB,
            "${SettingTrackingConstant.CLICK} ${SettingTrackingConstant.SHOP_FOLLOWERS}",
            ""
        )

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun sendEventClickShopAvatar() {
        val event = TrackAppUtils.gtmData(
            SettingTrackingConstant.CLICK_NAVIGATION_DRAWER,
            SettingTrackingConstant.OTHERS_TAB,
            "${SettingTrackingConstant.CLICK} ${SettingTrackingConstant.SHOP_PICTURE}",
            ""
        )

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun sendEventClickSaldoBalance() {
        val event = TrackAppUtils.gtmData(
            SettingTrackingConstant.CLICK_NAVIGATION_DRAWER,
            SettingTrackingConstant.OTHERS_TAB,
            "${SettingTrackingConstant.CLICK} ${SettingTrackingConstant.ON_SALDO}",
            ""
        )

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun sendEventClickTopadsBalance() {
        val event = TrackAppUtils.gtmData(
            SettingTrackingConstant.CLICK_NAVIGATION_DRAWER,
            SettingTrackingConstant.OTHERS_TAB,
            "${SettingTrackingConstant.CLICK} ${SettingTrackingConstant.ON_TOPADS_CREDIT}",
            ""
        )

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }


    fun sendEventClickShopStatus(shopType: ShopType) {
        val eventAction =
            "${SettingTrackingConstant.CLICK} ${SettingTrackingConstant.SHOP_STATE} - ${shopType.mapToEventCategory()}"

        val event = TrackAppUtils.gtmData(
            SettingTrackingConstant.CLICK_NAVIGATION_DRAWER,
            SettingTrackingConstant.OTHERS_TAB,
            eventAction,
            ""
        )

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun sendEventClickShareButton(shopId: String, userId: String) {
        sendLainnyaShareEvent(
            shopId = shopId,
            userId = userId,
            eventName = SettingTrackingConstant.CLICK_SHOP_PAGE,
            eventAction = SettingTrackingConstant.CLICK_SHARE_BUTTON,
            eventLabel = SettingTrackingConstant.LAINNYA
        )
    }

    fun sendEventClickCloseShareBottomSheet(shopId: String, userId: String) {
        sendLainnyaShareEvent(
            shopId = shopId,
            userId = userId,
            eventName = SettingTrackingConstant.CLICK_SHOP_PAGE,
            eventAction = SettingTrackingConstant.CLICK_CLOSE_SHARE_BOTTOM_SHEET,
            eventLabel = SettingTrackingConstant.LAINNYA
        )
    }

    fun sendEventClickSharingChannel(shopId: String, userId: String, channel: String) {
        sendLainnyaShareEvent(
            shopId = shopId,
            userId = userId,
            eventName = SettingTrackingConstant.CLICK_SHOP_PAGE,
            eventAction = SettingTrackingConstant.CLICK_SHARING_CHANNEL,
            eventLabel = "${SettingTrackingConstant.LAINNYA} - $channel"
        )
    }

    fun sendEventImpressionViewOnSharingChannel(shopId: String, userId: String) {
        sendLainnyaShareEvent(
            shopId = shopId,
            userId = userId,
            eventName = SettingTrackingConstant.VIEW_SHOP_PAGE_IRIS,
            eventAction = SettingTrackingConstant.VIEW_ON_SHARING_CHANNEL,
            eventLabel = SettingTrackingConstant.LAINNYA
        )
    }

    fun sendEventImpressionTokoPlus() {
        val event = TrackAppUtils.gtmData(
            SettingTrackingConstant.VIEW_PG_IRIS,
            SettingTrackingConstant.OTHERS_TAB,
            SettingTrackingConstant.IMPRESSION_TOKOPEDIA_PLUS,
            String.EMPTY
        )

        event[SettingTrackingConstant.KEY_TRACKER_ID] = SettingTrackingConstant.TOKO_PLUS_IMPRESSION_TRACKER_ID
        event[SettingTrackingConstant.KEY_BUSINESS_UNIT] = SettingTrackingConstant.PHYSICAL_GOODS_
        event[SettingTrackingConstant.KEY_CURRENT_SITE] = SettingTrackingConstant.TOKOPEDIA_MARKETPLACE

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun sendEventClickTokoPlus() {
        val event = TrackAppUtils.gtmData(
            SettingTrackingConstant.CLICK_PG,
            SettingTrackingConstant.OTHERS_TAB,
            SettingTrackingConstant.CLICK_TOKOPEDIA_PLUS,
            String.EMPTY
        )

        event[SettingTrackingConstant.KEY_TRACKER_ID] = SettingTrackingConstant.TOKO_PLUS_CLICK_TRACKER_ID
        event[SettingTrackingConstant.KEY_BUSINESS_UNIT] = SettingTrackingConstant.PHYSICAL_GOODS_
        event[SettingTrackingConstant.KEY_CURRENT_SITE] = SettingTrackingConstant.TOKOPEDIA_MARKETPLACE

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    private fun sendLainnyaShareEvent(shopId: String,
                                      userId: String,
                                      eventName: String,
                                      eventAction: String,
                                      eventLabel: String) {
        val event = TrackAppUtils.gtmData(
            eventName,
            SettingTrackingConstant.SELLER_SHOP_PAGE,
            eventAction,
            eventLabel
        )

        event[SettingTrackingConstant.KEY_BUSINESS_UNIT] = SettingTrackingConstant.SHARING_EXPERIENCE
        event[SettingTrackingConstant.KEY_CURRENT_SITE] = SettingTrackingConstant.TOKOPEDIA_MARKETPLACE
        event[SettingTrackingConstant.SHOP_ID] = shopId
        event[SettingTrackingConstant.USER_ID] = userId

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }
}
