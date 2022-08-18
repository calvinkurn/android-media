package com.tokopedia.topchat.common.analytics

import android.os.Bundle
import com.tokopedia.atc_common.domain.analytics.AddToCartExternalAnalytics
import com.tokopedia.chat_common.data.ChatroomViewModel
import com.tokopedia.chat_common.data.ProductAttachmentUiModel
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import java.util.ArrayList

object TopChatAnalyticsKt {

    var sourcePage = ""
    const val PUSH_NOTIF = "push_notif"

    fun eventClickOCCButton(
        element: ProductAttachmentUiModel,
        chatData: ChatroomViewModel,
        userId: String
    ) {
        val itemBundle = Bundle()
        itemBundle.putString(CATEGORY_ID, setValueOrDefault(element.categoryId.toString()))
        itemBundle.putString(DIMENSION_10, setValueOrDefault())
        itemBundle.putString(DIMENSION_12, setValueOrDefault())
        itemBundle.putString(DIMENSION_120, setValueOrDefault())
        itemBundle.putString(DIMENSION_14, setValueOrDefault())
        itemBundle.putString(DIMENSION_16, setValueOrDefault())
        itemBundle.putString(DIMENSION_38, setValueOrDefault())
        itemBundle.putString(DIMENSION_40, element.getAtcDimension40(sourcePage))
        itemBundle.putString(DIMENSION_45, setValueOrDefault(element.cartId))
        itemBundle.putString(DIMENSION_54, setValueOrDefault())
        itemBundle.putString(DIMENSION_79, chatData.headerModel.shopId.toString())
        itemBundle.putString(DIMENSION_80, chatData.shopName)
        itemBundle.putString(DIMENSION_81, chatData.shopType)
        itemBundle.putString(DIMENSION_82, setValueOrDefault(element.categoryId.toString()))
        itemBundle.putString(DIMENSION_83, setValueOrDefault())

        itemBundle.putString(ITEM_BRAND, setValueOrDefault())
        itemBundle.putString(ITEM_CATEGORY, setValueOrDefault(element.category))
        itemBundle.putString(ITEM_ID, element.productId)
        itemBundle.putString(ITEM_NAME, element.productName)
        itemBundle.putString(ITEM_VARIANT, getValueOrEmpty(element.variants))
        itemBundle.putDouble(PRICE, element.priceNumber.toDouble())
        itemBundle.putInt(QUANTITY, element.minOrder)
        itemBundle.putString(SHOP_ID, chatData.headerModel.shopId.toString())
        itemBundle.putString(SHOP_NAME, chatData.shopName)
        itemBundle.putString(SHOP_TYPE, chatData.shopType)

        val eventDataLayer = Bundle()
        eventDataLayer.putString(TrackAppUtils.EVENT, ATC)
        eventDataLayer.putString(TrackAppUtils.EVENT_CATEGORY, EVENT_CATEGORY_CHAT)
        eventDataLayer.putString(TrackAppUtils.EVENT_ACTION, CLICK_OCC)
        eventDataLayer.putString(TrackAppUtils.EVENT_LABEL, OCC_LABEL)
        eventDataLayer.putString(KEY_BUSINESS_UNIT, COMMUNICATION_MEDIA)
        eventDataLayer.putString(KEY_CURRENT_SITE, CURRENT_SITE)
        eventDataLayer.putParcelableArrayList(
            AddToCartExternalAnalytics.EE_VALUE_ITEMS,
            object : ArrayList<Bundle?>() {
                init {
                    add(itemBundle)
                }
            })
        eventDataLayer.putString(PRODUCT_ID, element.productId)
        eventDataLayer.putString(USER_ID, userId)

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(ATC, eventDataLayer)
    }

    private fun setValueOrDefault(value: String = ""): String {
        return if (value.isEmpty()) {
            AddToCartExternalAnalytics.EE_VALUE_NONE_OTHER
        } else value
    }

    private fun <T> getValueOrEmpty(value: List<T>): String {
        return if (value.isEmpty()) {
            ""
        } else value.toString()
    }

    fun eventTapAndHoldBubbleChat(replyId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createGeneralEvent(
                event = Event.CHAT_DETAIL,
                category = Category.CHAT_DETAIL,
                action = Action.TAP_AND_HOLD_BUBBLE,
                label = replyId,
                businessUnit = COMMUNICATION_MEDIA,
                currentSite = CURRENT_SITE_TOKOPEDIA
            )
        )
    }

    fun eventClickMsgMenu(title: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createGeneralEvent(
                event = Event.CHAT_DETAIL,
                category = Category.CHAT_DETAIL,
                action = Action.CLICK_MSG_MENU,
                label = title,
                businessUnit = COMMUNICATION_MEDIA,
                currentSite = CURRENT_SITE_TOKOPEDIA
            )
        )
    }

    fun eventConfirmDeleteMsg(replyId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createGeneralEvent(
                event = Event.CHAT_DETAIL,
                category = Category.CHAT_DETAIL,
                action = Action.CLICK_CONFIRM_DELETE_MSG,
                label = replyId,
                businessUnit = COMMUNICATION_MEDIA,
                currentSite = CURRENT_SITE_TOKOPEDIA
            )
        )
    }

    fun eventClickCloseReplyBubblePreview(replyId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createGeneralEvent(
                event = Event.CHAT_DETAIL,
                category = Category.CHAT_DETAIL,
                action = Action.CLICK_CLOSE_REPLY_BUUBLE_PREVIEW,
                label = replyId,
                businessUnit = COMMUNICATION_MEDIA,
                currentSite = CURRENT_SITE_TOKOPEDIA
            )
        )
    }

    fun eventCLickReplyBubble(childReplyId: String, parentReplyId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createGeneralEvent(
                event = Event.CHAT_DETAIL,
                category = Category.CHAT_DETAIL,
                action = Action.CLICK_REPLY_BUBBLE,
                label = "$childReplyId - $parentReplyId",
                businessUnit = COMMUNICATION_MEDIA,
                currentSite = CURRENT_SITE_TOKOPEDIA
            )
        )
    }

    fun eventVoucherThumbnailClicked(sourceVoucher: String, voucherId: Int) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createGeneralEvent(
                event = Event.CLICK_COMMUNICATION,
                category = Category.CHAT_DETAIL,
                action = Action.CLICK_SHOP_VOUCHER_THUMBNAIL,
                label = "$sourceVoucher - $voucherId",
                businessUnit = COMMUNICATION_MEDIA,
                currentSite = CURRENT_SITE_TOKOPEDIA
            )
        )
    }

    fun eventViewVoucher(sourceVoucher: String, voucherId: Int) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createGeneralEvent(
                event = Event.VIEW_COMMUNICATION_IRIS,
                category = Category.CHAT_DETAIL,
                action = Action.VIEW_VOUCHER_THUMBNAIL,
                label = "$sourceVoucher - $voucherId",
                businessUnit = COMMUNICATION_MEDIA,
                currentSite = CURRENT_SITE_TOKOPEDIA
            )
        )
    }

    fun eventViewProductBundling(firstProductId: String, bundleId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createGeneralEvent(
                event = Event.VIEW_COMMUNICATION_IRIS,
                category = Category.CHAT_DETAIL,
                action = Action.VIEW_BUNDLING_PRODUCT_CARD,
                label = "$firstProductId - $bundleId",
                businessUnit = COMMUNICATION_MEDIA,
                currentSite = CURRENT_SITE_TOKOPEDIA
            )
        )
    }

    fun eventClickProductBundlingCta(firstProductId: String, bundleId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createGeneralEvent(
                event = Event.CLICK_COMMUNICATION,
                category = Category.CHAT_DETAIL,
                action = Action.CLICK_BUNDLING_PRODUCT_CTA,
                label = "$firstProductId - $bundleId",
                businessUnit = COMMUNICATION_MEDIA,
                currentSite = CURRENT_SITE_TOKOPEDIA
            )
        )
    }

    fun eventViewSrwOnBoarding() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createGeneralEvent(
                event = Event.VIEW_COMMUNICATION_IRIS,
                category = Category.CHAT_DETAIL,
                action = Action.VIEW_SRW_ONBOARDING,
                label = "",
                businessUnit = COMMUNICATION_MEDIA,
                currentSite = CURRENT_SITE_TOKOPEDIA
            )
        )
    }

    fun eventClickCloseSrwOnBoarding() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createGeneralEvent(
                event = Event.CLICK_COMMUNICATION,
                category = Category.CHAT_DETAIL,
                action = Action.CLICK_CLOSE_SRW_ONBOARDING,
                label = "",
                businessUnit = COMMUNICATION_MEDIA,
                currentSite = CURRENT_SITE_TOKOPEDIA
            )
        )
    }

    fun eventClickReplyChatFromNotif() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createGeneralEvent(
                event = Event.CLICK_COMMUNICATION,
                action = Action.CLICK_SEND_MSG_ON_NOTIF,
                category = Category.PUSH_NOTIF_CHAT,
                label = "",
                businessUnit = COMMUNICATION,
                currentSite = CURRENT_SITE_TOKOPEDIA,
                trackerId = "34310"
            )
        )
    }

    fun eventViewProductBundlingBroadcast(
        blastId: String,
        statusBundle: String,
        bundleId: String,
        shopId: String,
        userId: String
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createGeneralEvent(
                event = Event.VIEW_COMMUNICATION_IRIS,
                category = Category.CHAT_DETAIL,
                action = Action.VIEW_BUNDLE_CART_CHATROOM,
                label = "$blastId - $statusBundle - $bundleId - bundling",
                businessUnit = COMMUNICATION_MEDIA,
                currentSite = CURRENT_SITE_TOKOPEDIA,
                trackerId = "35596",
                shopId = shopId,
                userId = userId
            )
        )
    }

    fun eventClickProductAttachmentOnProductBundlingBroadcast(
        blastId: String,
        statusBundle: String,
        bundleId: String,
        productId: String,
        shopId: String,
        userId: String
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createGeneralEvent(
                event = Event.CLICK_COMMUNICATION,
                category = Category.CHAT_DETAIL,
                action = Action.CLICK_PRODUCT_BUNDLE,
                label = "$blastId - $statusBundle - $bundleId - bundling - $productId",
                businessUnit = COMMUNICATION_MEDIA,
                currentSite = CURRENT_SITE_TOKOPEDIA,
                trackerId = "35598",
                shopId = shopId,
                userId = userId
            )
        )
    }

    fun eventClickCtaOnProductBundlingBroadcast(
        blastId: String,
        statusBundle: String,
        bundleId: String,
        shopId: String,
        userId: String
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createGeneralEvent(
                event = Event.CLICK_COMMUNICATION,
                category = Category.CHAT_DETAIL,
                action = Action.CLICK_ADD_TO_CART_BUNDLE,
                label = "$blastId - $statusBundle - $bundleId - bundling",
                businessUnit = COMMUNICATION_MEDIA,
                currentSite = CURRENT_SITE_TOKOPEDIA,
                trackerId = "35599",
                shopId = shopId,
                userId = userId
            )
        )
    }

    private fun createGeneralEvent(
        event: String,
        category: String,
        action: String,
        label: String,
        businessUnit: String,
        currentSite: String?,
        userId: String? = null,
        trackerId: String? = null,
        shopId: String? = null
    ): Map<String, Any> {
        val data = mutableMapOf(
            TrackAppUtils.EVENT to event,
            TrackAppUtils.EVENT_CATEGORY to category,
            TrackAppUtils.EVENT_ACTION to action,
            TrackAppUtils.EVENT_LABEL to label,
            KEY_BUSINESS_UNIT to businessUnit
        )
        if (currentSite != null) {
            data[KEY_CURRENT_SITE] = currentSite
        }
        if (userId != null) {
            data[USER_ID] = userId
        }
        if (trackerId != null) {
            data[TRACKER_ID] = trackerId
        }
        if (shopId != null) {
            data[SHOPID] = shopId
        }
        return data
    }

    object Event {
        const val CHAT_DETAIL = "clickChatDetail"
        const val CLICK_COMMUNICATION = "clickCommunication"
        const val VIEW_COMMUNICATION_IRIS = "viewCommunicationIris"
    }

    object Category {
        const val CHAT_DETAIL = "chat detail"
        const val PUSH_NOTIF_CHAT = "push notification chat"
    }

    object Action {
        const val TAP_AND_HOLD_BUBBLE = "tap and hold bubble chat"
        const val CLICK_MSG_MENU = "click menu on atur pesan bottomsheet"
        const val CLICK_CONFIRM_DELETE_MSG = "click confirm delete message"
        const val CLICK_CLOSE_REPLY_BUUBLE_PREVIEW =
            "click close preview replied bubble chat above keyboard"
        const val CLICK_REPLY_BUBBLE = "click view parent replied bubble chat"
        const val CLICK_SHOP_VOUCHER_THUMBNAIL = "click shop voucher thumbnail"
        const val VIEW_VOUCHER_THUMBNAIL = "view voucher thumbnail"
        const val VIEW_SRW_ONBOARDING = "view on srw onboarding"
        const val CLICK_CLOSE_SRW_ONBOARDING = "click close on srw onboarding"
        const val VIEW_BUNDLING_PRODUCT_CARD = "view on bundling product card"
        const val CLICK_BUNDLING_PRODUCT_CTA = "click on bundling product card"
        const val CLICK_SEND_MSG_ON_NOTIF = "click sent msg on notifpush"
        const val VIEW_BUNDLE_CART_CHATROOM = "view on bundle card in chatroom"
        const val CLICK_PRODUCT_BUNDLE = "click on product attachment on bundle card"
        const val CLICK_ADD_TO_CART_BUNDLE = "click on add to cart from bundle card"
    }

    //Event Name
    private const val ATC = "add_to_cart"

    //Event Category
    private const val EVENT_CATEGORY_CHAT = "chat"

    //Event Action
    private const val CLICK_OCC = "click - Beli Langsung on chat"

    //Event Label
    private const val OCC_LABEL = "fitur : OCC"

    //Other
    private const val CURRENT_SITE = "topchat"
    private const val COMMUNICATION_MEDIA = "Communication & Media"
    private const val CURRENT_SITE_TOKOPEDIA = "tokopediamarketplace"
    private const val COMMUNICATION = "communication"

    //General Keys
    private const val KEY_BUSINESS_UNIT = "businessUnit"
    private const val KEY_CURRENT_SITE = "currentSite"

    //OCC Product Keys
    private const val CATEGORY_ID = "category_id"
    private const val DIMENSION_10 = "dimension10"
    private const val DIMENSION_12 = "dimension12"
    private const val DIMENSION_120 = "dimension120"
    private const val DIMENSION_14 = "dimension14"
    private const val DIMENSION_16 = "dimension16"
    private const val DIMENSION_38 = "dimension38"
    private const val DIMENSION_40 = "dimension40"
    private const val DIMENSION_45 = "dimension45"
    private const val DIMENSION_54 = "dimension54"
    private const val DIMENSION_79 = "dimension79"
    private const val DIMENSION_80 = "dimension80"
    private const val DIMENSION_81 = "dimension81"
    private const val DIMENSION_82 = "dimension82"
    private const val DIMENSION_83 = "dimension83"
    private const val ITEM_BRAND = "item_brand"
    private const val ITEM_CATEGORY = "item_category"
    private const val ITEM_ID = "item_id"
    private const val ITEM_NAME = "item_name"
    private const val ITEM_VARIANT = "item_variant"
    private const val PRICE = "price"
    private const val QUANTITY = "quantity"
    private const val SHOP_ID = "shop_id"
    private const val SHOP_NAME = "shop_name"
    private const val SHOP_TYPE = "shop_type"
    private const val PRODUCT_ID = "productId"
    private const val USER_ID = "userId"
    private const val TRACKER_ID = "trackerId"
    private const val SHOPID = "shopId"
}