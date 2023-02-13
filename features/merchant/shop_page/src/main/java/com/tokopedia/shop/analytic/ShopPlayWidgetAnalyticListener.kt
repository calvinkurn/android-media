package com.tokopedia.shop.analytic

import com.tokopedia.config.GlobalConfig
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType
import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSessionInterface

/**
 * Created by mzennis on 26/10/20.
 *
 * https://docs.google.com/spreadsheets/d/1l91ritx5rj-RJzcTNVXnMTcOp3sWZz6O2v__nfV64Co/edit#gid=1005486470 row 61-65
 * https://mynakama.tokopedia.com/datatracker/requestdetail/view/63
 * Save Live to VOD https://mynakama.tokopedia.com/datatracker/product/requestdetail/170
 */
class ShopPlayWidgetAnalyticListener(
    private val userSession: UserSessionInterface
) {

    var shopId: String = ""
    var widgetId: String = ""

    private val userId: String
        get() = userSession.userId

    private val currentSite: String
        get() = if (GlobalConfig.isSellerApp()) "tokopediaseller" else "tokopediamarketplace"

    private val isOwnShop: Boolean
        get() = shopId == userSession.shopId

    fun onImpressMoreActionChannel(item: PlayWidgetChannelUiModel) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                EVENT to VIEW_SHOP_PAGE_IRIS,
                EVENT_CATEGORY to SHOP_PAGE_SELLER,
                EVENT_ACTION to "view bottom sheet - widget play",
                EVENT_LABEL to "$shopId - ${item.channelId}",
                BUSINESS_UNIT to "play",
                CURRENT_SITE to currentSite,
                USER_ID to userId,
                SHOP_ID to shopId
            )
        )
    }

    fun onClickMoreActionShareLinkChannel(channelId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                EVENT to CLICK_SHOP_PAGE,
                EVENT_CATEGORY to SHOP_PAGE_SELLER,
                EVENT_ACTION to "click share on bottom sheet - widget play",
                EVENT_LABEL to "$shopId - $channelId",
                BUSINESS_UNIT to "play",
                CURRENT_SITE to currentSite,
                USER_ID to userId,
                SHOP_ID to shopId
            )
        )
    }

    fun onClickMoreActionPerformaChannel(channelId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                EVENT to CLICK_SHOP_PAGE,
                EVENT_CATEGORY to SHOP_PAGE_SELLER,
                EVENT_ACTION to "click lihat performa - widget play",
                EVENT_LABEL to "$shopId - $channelId",
                BUSINESS_UNIT to "play",
                CURRENT_SITE to currentSite,
                USER_ID to userId,
                SHOP_ID to shopId
            )
        )
    }

    fun onClickMoreActionDeleteChannel(channelId: String) {
        if (!isOwnShop) return
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                EVENT to CLICK_SHOP_PAGE,
                EVENT_CATEGORY to SHOP_PAGE_SELLER,
                EVENT_ACTION to "click delete on bottom sheet - widget play",
                EVENT_LABEL to "$shopId - $channelId",
                BUSINESS_UNIT to "play",
                CURRENT_SITE to currentSite,
                USER_ID to userId,
                SHOP_ID to shopId
            )
        )
    }

    fun onImpressDialogDeleteChannel(channelId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                EVENT to VIEW_SHOP_PAGE_IRIS,
                EVENT_CATEGORY to SHOP_PAGE_SELLER,
                EVENT_ACTION to "view confirm on pop up delete - widget play",
                EVENT_LABEL to "$shopId - $channelId",
                BUSINESS_UNIT to "play",
                CURRENT_SITE to currentSite,
                USER_ID to userId,
                SHOP_ID to shopId
            )
        )
    }

    fun onClickDialogDeleteChannel(channelId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                EVENT to CLICK_SHOP_PAGE,
                EVENT_CATEGORY to SHOP_PAGE_SELLER,
                EVENT_ACTION to "click delete on pop up delete - widget play",
                EVENT_LABEL to "$shopId - $channelId",
                BUSINESS_UNIT to "play",
                CURRENT_SITE to currentSite,
                USER_ID to userId,
                SHOP_ID to shopId
            )
        )
    }

    fun onImpressErrorDeleteChannel(channelId: String, errorMessage: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                EVENT to VIEW_SHOP_PAGE_IRIS,
                EVENT_CATEGORY to SHOP_PAGE_SELLER,
                EVENT_ACTION to "error state on shop page seller - widget play",
                EVENT_LABEL to "$shopId - $channelId - $errorMessage",
                BUSINESS_UNIT to "play",
                CURRENT_SITE to currentSite,
                USER_ID to userId,
                SHOP_ID to shopId
            )
        )
    }

    companion object {
        const val EVENT = "event"
        const val EVENT_CATEGORY = "eventCategory"
        const val EVENT_ACTION = "eventAction"
        const val EVENT_LABEL = "eventLabel"

        const val CURRENT_SITE = "currentSite"
        const val BUSINESS_UNIT = "businessUnit"

        const val CLICK_SHOP_PAGE = "clickShopPage"
        const val VIEW_SHOP_PAGE_IRIS = "viewShopPageIris"

        const val SHOP_PAGE_SELLER = "shop page - seller"

        const val SHOP_ID = "shopId"
        const val USER_ID = "userId"
    }
}
