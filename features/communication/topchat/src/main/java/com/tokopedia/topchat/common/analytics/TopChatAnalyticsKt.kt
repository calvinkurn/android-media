package com.tokopedia.topchat.common.analytics

import android.os.Bundle
import com.tokopedia.atc_common.domain.analytics.AddToCartExternalAnalytics
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.chat_common.data.ChatroomViewModel
import com.tokopedia.chat_common.data.ProductAttachmentUiModel
import com.tokopedia.topchat.chatroom.domain.pojo.param.AddToCartParam
import com.tokopedia.topchat.chatroom.domain.pojo.param.AddToCartParam.Companion.EVENT_ACTION_ATC
import com.tokopedia.topchat.chatroom.domain.pojo.param.AddToCartParam.Companion.EVENT_ACTION_BUY
import com.tokopedia.topchat.chatroom.domain.pojo.product_bundling.BundleItem
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.user.session.UserSessionInterface

object TopChatAnalyticsKt {

    var sourcePage = ""
    const val PUSH_NOTIF = "push_notif"

    fun eventClickOCCButton(
        element: ProductAttachmentUiModel,
        chatData: ChatroomViewModel,
        userId: String
    ) {
        val itemBundle = Bundle()
        itemBundle.putString(CATEGORY_ID, setValueOrDefault(element.categoryId))
        itemBundle.putString(DIMENSION_10, setValueOrDefault())
        itemBundle.putString(DIMENSION_12, setValueOrDefault())
        itemBundle.putString(DIMENSION_120, setValueOrDefault())
        itemBundle.putString(DIMENSION_14, setValueOrDefault())
        itemBundle.putString(DIMENSION_16, setValueOrDefault())
        itemBundle.putString(DIMENSION_38, setValueOrDefault())
        itemBundle.putString(DIMENSION_40, element.getAtcDimension40(sourcePage))
        itemBundle.putString(DIMENSION_45, setValueOrDefault(element.cartId))
        itemBundle.putString(DIMENSION_54, setValueOrDefault())
        itemBundle.putString(DIMENSION_79, chatData.headerModel.shopId)
        itemBundle.putString(DIMENSION_80, chatData.shopName)
        itemBundle.putString(DIMENSION_81, chatData.shopType)
        itemBundle.putString(DIMENSION_82, setValueOrDefault(element.categoryId))
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
            }
        )
        eventDataLayer.putString(PRODUCT_ID, element.productId)
        eventDataLayer.putString(USER_ID, userId)

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(ATC, eventDataLayer)
    }

    private fun setValueOrDefault(value: String = ""): String {
        return value.ifEmpty {
            EE_VALUE_NONE_OTHER
        }
    }

    private fun <T> getValueOrEmpty(value: List<T>): String {
        return if (value.isEmpty()) {
            EE_VALUE_NONE_OTHER
        } else {
            value.toString()
        }
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

    fun eventClickReplyTabChatTextAreaLayout(
        productIds: String,
        userId: String,
        shopId: String
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createGeneralEvent(
                event = Event.CLICK_COMMUNICATION,
                category = Category.CHAT_DETAIL,
                action = Action.CLICK_TAB_REPLY,
                label = "$shopId - $userId - $productIds",
                trackerId = "37348",
                businessUnit = COMMUNICATION_MEDIA,
                currentSite = CURRENT_SITE_TOKOPEDIA
            )
        )
    }

    fun eventClickSRWTabChatTextAreaLayout(
        productIds: String,
        userId: String,
        shopId: String
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createGeneralEvent(
                event = Event.CLICK_COMMUNICATION,
                category = Category.CHAT_DETAIL,
                action = Action.CLICK_TAB_SRW,
                label = "$shopId - $userId - $productIds",
                trackerId = "37349",
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

    private fun getItemBundle(
        bundleItems: List<BundleItem>,
        bundleId: String,
        bundleType: String,
        source: String,
        hasQuantityValue: Boolean = false,
        shopId: String = "",
        shopName: String = "",
        hasShopTypeValue: Boolean = false,
        hasProductCategoryId: Boolean = false,
        hasIndex: Boolean = true
    ): ArrayList<Bundle> {
        val listItemBundles = ArrayList<Bundle>()
        for (item in bundleItems) {
            val itemBundle = Bundle()
            if (hasProductCategoryId) {
                itemBundle.putString(CATEGORY_ID, EE_VALUE_NONE_OTHER)
            }
            itemBundle.putString(DIMENSION_117, setValueOrDefault(bundleType))
            itemBundle.putString(DIMENSION_118, setValueOrDefault(bundleId))
            itemBundle.putString(
                DIMENSION_40,
                setValueOrDefault("/$source - product bundling - $bundleType")
            )
            itemBundle.putString(DIMENSION_87, setValueOrDefault(source))
            if (hasIndex) {
                itemBundle.putString(INDEX, bundleItems.indexOf(item).toString())
            }
            itemBundle.putString(ITEM_BRAND, EE_VALUE_NONE_OTHER)
            itemBundle.putString(ITEM_CATEGORY, setValueOrDefault(""))
            itemBundle.putString(ITEM_ID, setValueOrDefault(item.productId))
            itemBundle.putString(ITEM_NAME, setValueOrDefault(item.name))
            itemBundle.putString(ITEM_VARIANT, EE_VALUE_NONE_OTHER)
            itemBundle.putString(PRICE, item.price.toString())
            if (hasQuantityValue) {
                itemBundle.putString(QUANTITY, item.quantity)
            }
            if (shopId.isNotEmpty()) {
                itemBundle.putString(SHOP_ID, shopId)
            }
            if (shopName.isNotEmpty()) {
                itemBundle.putString(SHOP_NAME, shopName)
            }
            if (hasShopTypeValue) {
                itemBundle.putString(SHOP_TYPE, "")
            }
            listItemBundles.add(itemBundle)
        }

        return listItemBundles
    }

    private fun getItemIdList(bundleItems: List<BundleItem>): List<String> {
        val productIdList = mutableListOf<String>()
        for (item in bundleItems) {
            productIdList += setValueOrDefault(item.productId)
        }
        return productIdList
    }

    fun eventViewProductBundlingBroadcast(
        blastId: String,
        statusBundle: String,
        bundleId: String,
        bundleType: String,
        bundleItems: List<BundleItem>,
        shopId: String,
        userId: String
    ) {
        val listItemBundles = getItemBundle(
            bundleItems = bundleItems,
            bundleId = bundleId,
            bundleType = bundleType,
            source = "broadcast",
            hasQuantityValue = false,
            shopId = "",
            shopName = "",
            hasShopTypeValue = false,
            hasProductCategoryId = false,
            hasIndex = true
        )

        val eventDataLayer = Bundle()

        eventDataLayer.putString(TrackAppUtils.EVENT, VIEW_ITEM_LIST)
        eventDataLayer.putString(TrackAppUtils.EVENT_ACTION, Action.VIEW_BUNDLE_CARD_CHATROOM)
        eventDataLayer.putString(TrackAppUtils.EVENT_CATEGORY, Category.CHAT_DETAIL)
        eventDataLayer.putString(
            TrackAppUtils.EVENT_LABEL,
            "$blastId - $statusBundle - $bundleId - bundling"
        )
        eventDataLayer.putString(TRACKER_ID, "35596")
        eventDataLayer.putString(KEY_BUSINESS_UNIT, COMMUNICATION)
        eventDataLayer.putString(KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA)

        eventDataLayer.putString(USER_ID, userId)
        eventDataLayer.putString(SHOP_ID, shopId)

        eventDataLayer.putParcelableArrayList(
            AddToCartExternalAnalytics.EE_VALUE_ITEMS,
            listItemBundles
        )

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            Event.VIEW_COMMUNICATION_IRIS,
            eventDataLayer
        )
    }

    fun eventClickProductAttachmentOnProductBundlingBroadcast(
        blastId: String,
        statusBundle: String,
        bundleId: String,
        bundleType: String,
        source: String,
        bundleItems: List<BundleItem>,
        shopId: String,
        userId: String
    ) {
        val listItemBundles = getItemBundle(bundleItems, bundleId, bundleType, source)

        val eventDataLayer = Bundle()

        eventDataLayer.putString(TrackAppUtils.EVENT, SELECT_CONTENT)
        eventDataLayer.putString(TrackAppUtils.EVENT_ACTION, Action.CLICK_PRODUCT_BUNDLE)
        eventDataLayer.putString(TrackAppUtils.EVENT_CATEGORY, Category.CHAT_DETAIL)

        val productId = if (bundleItems.isNotEmpty()) {
            // if bundleItems is not empty
            bundleItems[0].productId
        } else {
            "0"
        }
        eventDataLayer.putString(
            TrackAppUtils.EVENT_LABEL,
            "$blastId - $statusBundle - $bundleId - bundling - $productId"
        )

        eventDataLayer.putString(TRACKER_ID, "35598")
        eventDataLayer.putString(KEY_BUSINESS_UNIT, COMMUNICATION)
        eventDataLayer.putString(KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA)

        val productIdList = getItemIdList(bundleItems)

        eventDataLayer.putString(ITEM_LIST, productIdList.joinToString())
        eventDataLayer.putParcelableArrayList(
            AddToCartExternalAnalytics.EE_VALUE_ITEMS,
            listItemBundles
        )
        eventDataLayer.putString(USER_ID, userId)
        eventDataLayer.putString(SHOP_ID, shopId)

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(SELECT_CONTENT, eventDataLayer)
    }

    fun eventClickCtaOnProductBundlingBroadcast(
        blastId: String,
        statusBundle: String,
        bundleId: String,
        bundleItems: List<BundleItem>,
        bundleType: String,
        source: String,
        shopId: String,
        shopName: String,
        userId: String
    ) {
        val listItemBundles = getItemBundle(
            bundleItems,
            bundleId,
            bundleType,
            source,
            true,
            shopId,
            shopName,
            hasShopTypeValue = true,
            hasProductCategoryId = true,
            false
        )

        val eventDataLayer = Bundle()

        eventDataLayer.putString(TrackAppUtils.EVENT, ATC)
        eventDataLayer.putString(TrackAppUtils.EVENT_ACTION, Action.CLICK_ADD_TO_CART_BUNDLE)
        eventDataLayer.putString(TrackAppUtils.EVENT_CATEGORY, Category.CHAT_DETAIL)

        eventDataLayer.putString(
            TrackAppUtils.EVENT_LABEL,
            "$blastId - $statusBundle - $bundleId - bundling"
        )

        eventDataLayer.putString(TRACKER_ID, "35599")
        eventDataLayer.putString(KEY_BUSINESS_UNIT, COMMUNICATION)
        eventDataLayer.putString(KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA)

        eventDataLayer.putParcelableArrayList(
            AddToCartExternalAnalytics.EE_VALUE_ITEMS,
            listItemBundles
        )
        eventDataLayer.putString(USER_ID, userId)
        eventDataLayer.putString(SHOP_ID, shopId)

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(ATC, eventDataLayer)
    }

    fun eventSeenProductAttachment(
        product: ProductAttachmentUiModel,
        user: UserSessionInterface,
        amISeller: Boolean
    ) {
        val eventLabel = product.getEventLabelImpression(amISeller)
        val itemBundle = Bundle()
        itemBundle.putString(INDEX, PRODUCT_INDEX)
        itemBundle.putString(ITEM_BRAND, EE_VALUE_NONE_OTHER)
        itemBundle.putString(ITEM_CATEGORY, setValueOrDefault(product.category))
        itemBundle.putString(ITEM_ID, setValueOrDefault(product.productId))
        itemBundle.putString(ITEM_NAME, setValueOrDefault(product.productName))
        itemBundle.putString(ITEM_VARIANT, EE_VALUE_NONE_OTHER)
        itemBundle.putDouble(PRICE, product.priceNumber + 0.0)

        val itemBundleList = ArrayList<Bundle>()
        itemBundleList.add(itemBundle)

        val productIdList = mutableListOf<String>()
        productIdList += product.productId

        val eventDataLayer = Bundle()
        eventDataLayer.putString(TrackAppUtils.EVENT, VIEW_ITEM_LIST)
        eventDataLayer.putString(TrackAppUtils.EVENT_ACTION, Action.VIEW_ON_PRODUCT_THUMBNAIL)
        eventDataLayer.putString(TrackAppUtils.EVENT_CATEGORY, Category.CHAT_DETAIL)
        eventDataLayer.putString(TrackAppUtils.EVENT_LABEL, eventLabel)
        eventDataLayer.putString(TRACKER_ID, "14824")
        eventDataLayer.putString(KEY_BUSINESS_UNIT, COMMUNICATION)
        eventDataLayer.putString(KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA)
        eventDataLayer.putString(ITEM_LIST, productIdList.joinToString())
        eventDataLayer.putParcelableArrayList(
            AddToCartExternalAnalytics.EE_VALUE_ITEMS,
            itemBundleList
        )
        eventDataLayer.putString(USER_ID, setValueOrDefault(user.userId))

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(VIEW_ITEM_LIST, eventDataLayer)
    }

    fun trackSuccessDoBuyAndAtc(
        element: AddToCartParam,
        data: DataModel?,
        shopName: String,
        eventAction: String,
        userId: String
    ) {
        val itemBundle = Bundle()
        itemBundle.putString(CATEGORY_ID, setValueOrDefault(""))
        itemBundle.putString(DIMENSION_45, setValueOrDefault(data?.cartId.toString()))
        itemBundle.putString(ITEM_BRAND, setValueOrDefault(""))
        itemBundle.putString(ITEM_CATEGORY, setValueOrDefault(element.category))
        itemBundle.putString(ITEM_ID, setValueOrDefault(data?.productId.toString()))
        itemBundle.putString(ITEM_NAME, setValueOrDefault(element.productName))
        itemBundle.putString(ITEM_VARIANT, setValueOrDefault(""))
        itemBundle.putString(PRICE, setValueOrDefault(element.price.toString()))
        itemBundle.putString(QUANTITY, setValueOrDefault(element.minOrder.toString()))
        itemBundle.putString(SHOP_ID, setValueOrDefault(data?.shopId.toString()))
        itemBundle.putString(SHOP_NAME, setValueOrDefault(shopName))
        itemBundle.putString(SHOP_TYPE, "")

        val eventDataLayer = Bundle()
        eventDataLayer.putString(TrackAppUtils.EVENT, ATC)
        eventDataLayer.putString(TrackAppUtils.EVENT_ACTION, eventAction)
        eventDataLayer.putString(TrackAppUtils.EVENT_CATEGORY, Category.CHAT_DETAIL)
        eventDataLayer.putString(
            TrackAppUtils.EVENT_LABEL,
            element.getAtcDimension40(sourcePage) + "-" + element.blastId
        )
        when (eventAction) {
            EVENT_ACTION_ATC -> {
                eventDataLayer.putString(TRACKER_ID, "14826")
            }
            EVENT_ACTION_BUY -> {
                eventDataLayer.putString(TRACKER_ID, "14827")
            }
        }
        eventDataLayer.putString(KEY_BUSINESS_UNIT, COMMUNICATION_MEDIA)
        eventDataLayer.putString(KEY_CURRENT_SITE, CURRENT_SITE)
        eventDataLayer.putParcelableArrayList(
            AddToCartExternalAnalytics.EE_VALUE_ITEMS,
            arrayListOf(itemBundle)
        )
        eventDataLayer.putString(USER_ID, setValueOrDefault(userId))

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(ATC, eventDataLayer)
    }

    fun eventViewOperationalInsightTicker(
        shopId: String,
        stateReport: String
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createGeneralEvent(
                event = Event.VIEW_COMMUNICATION_IRIS,
                category = Category.INBOX_CHAT,
                action = Action.SELLER_IMPRESS_REPORT_TICKER,
                label = "$shopId - $stateReport",
                businessUnit = COMMUNICATION,
                currentSite = CURRENT_SITE_TOKOPEDIA,
                trackerId = "33142"
            )
        )
    }

    fun eventClickOperationalInsightTicker(
        shopId: String,
        stateReport: String
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createGeneralEvent(
                event = Event.CLICK_COMMUNICATION,
                category = Category.INBOX_CHAT,
                action = Action.SELLER_CLICK_REPORT_TICKER,
                label = "$shopId - $stateReport",
                businessUnit = COMMUNICATION,
                currentSite = CURRENT_SITE_TOKOPEDIA,
                trackerId = "33143"
            )
        )
    }

    fun eventClickCloseOperationalInsightTicker(
        shopId: String,
        stateReport: String
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createGeneralEvent(
                event = Event.CLICK_COMMUNICATION,
                category = Category.INBOX_CHAT,
                action = Action.SELLER_CLICK_CLOSE_REPORT_TICKER,
                label = "$shopId - $stateReport",
                businessUnit = COMMUNICATION,
                currentSite = CURRENT_SITE_TOKOPEDIA,
                trackerId = "33144"
            )
        )
    }

    fun eventViewOperationalInsightBottomSheet(
        shopId: String,
        stateReport: String,
        replyChatRate: String,
        targetReplyChatRate: String,
        replyChatSpeed: String,
        targetReplyChatSpeed: String
    ) {
        val label = generateOperationalInsightLabel(
            shopId,
            stateReport,
            replyChatRate,
            targetReplyChatRate,
            replyChatSpeed,
            targetReplyChatSpeed
        )

        TrackApp.getInstance().gtm.sendGeneralEvent(
            createGeneralEvent(
                event = Event.VIEW_COMMUNICATION_IRIS,
                category = Category.INBOX_CHAT,
                action = Action.SELLER_IMPRESS_TICKER_BOTTOMSHEET,
                label = label,
                businessUnit = COMMUNICATION,
                currentSite = CURRENT_SITE_TOKOPEDIA,
                trackerId = "33145"
            )
        )
    }

    fun eventClickShopPerformanceOperationalInsightBottomSheet(
        shopId: String,
        stateReport: String,
        replyChatRate: String,
        targetReplyChatRate: String,
        replyChatSpeed: String,
        targetReplyChatSpeed: String
    ) {
        val label = generateOperationalInsightLabel(
            shopId,
            stateReport,
            replyChatRate,
            targetReplyChatRate,
            replyChatSpeed,
            targetReplyChatSpeed
        )

        TrackApp.getInstance().gtm.sendGeneralEvent(
            createGeneralEvent(
                event = Event.CLICK_COMMUNICATION,
                category = Category.INBOX_CHAT,
                action = Action.SELLER_CLICK_SHOP_PERFORMANCE,
                label = label,
                businessUnit = COMMUNICATION,
                currentSite = CURRENT_SITE_TOKOPEDIA,
                trackerId = "33146"
            )
        )
    }

    fun eventClickOperationalInsightCta(
        shopId: String,
        stateReport: String,
        replyChatRate: String,
        targetReplyChatRate: String,
        replyChatSpeed: String,
        targetReplyChatSpeed: String
    ) {
        val label = generateOperationalInsightLabel(
            shopId,
            stateReport,
            replyChatRate,
            targetReplyChatRate,
            replyChatSpeed,
            targetReplyChatSpeed
        )

        TrackApp.getInstance().gtm.sendGeneralEvent(
            createGeneralEvent(
                event = Event.CLICK_COMMUNICATION,
                category = Category.INBOX_CHAT,
                action = Action.SELLER_CLICK_OPERATIONAL_INSIGHT_CTA,
                label = label,
                businessUnit = COMMUNICATION,
                currentSite = CURRENT_SITE_TOKOPEDIA,
                trackerId = "33147"
            )
        )
    }

    private fun generateOperationalInsightLabel(
        shopId: String,
        stateReport: String,
        replyChatRate: String,
        targetReplyChatRate: String,
        replyChatSpeed: String,
        targetReplyChatSpeed: String
    ): String {
        return """
            $shopId - $stateReport - $replyChatRate - $targetReplyChatRate - $replyChatSpeed - $targetReplyChatSpeed
        """.trimIndent()
    }

    fun eventViewTicker(
        tickerType: String,
        isSeller: Boolean,
        msgId: String,
        lastReplyId: String
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createGeneralEvent(
                event = Event.VIEW_COMMUNICATION_IRIS,
                category = Category.CHAT_DETAIL,
                action = Action.VIEW_TICKER,
                label = "$tickerType - ${getRole(isSeller)} - $msgId - $lastReplyId",
                businessUnit = COMMUNICATION,
                currentSite = CURRENT_SITE_TOKOPEDIA,
                trackerId = "35989"
            )
        )
    }

    fun eventClickLinkTicker(
        tickerType: String,
        isSeller: Boolean,
        msgId: String,
        lastReplyId: String
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createGeneralEvent(
                event = Event.CLICK_COMMUNICATION,
                category = Category.CHAT_DETAIL,
                action = Action.CLICK_LINK_INSIDE_TICKER,
                label = "$tickerType - ${getRole(isSeller)} - $msgId - $lastReplyId",
                businessUnit = COMMUNICATION,
                currentSite = CURRENT_SITE_TOKOPEDIA,
                trackerId = "35990"
            )
        )
    }

    fun eventClickCloseTicker(
        tickerType: String,
        isSeller: Boolean,
        msgId: String,
        lastReplyId: String
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createGeneralEvent(
                event = Event.CLICK_COMMUNICATION,
                category = Category.CHAT_DETAIL,
                action = Action.CLICK_CLOSE_TICKER,
                label = "$tickerType - ${getRole(isSeller)}  - $msgId - $lastReplyId",
                businessUnit = COMMUNICATION,
                currentSite = CURRENT_SITE_TOKOPEDIA,
                trackerId = "35991"
            )
        )
    }

    /*
    * Bubbles Tracker
    */
    fun eventViewReadMsgFromBubble(lastReplyId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createGeneralEvent(
                event = Event.VIEW_COMMUNICATION_IRIS,
                category = Bubbles.BUBBLE_CHAT_DETAIL,
                action = Bubbles.READ_FROM_BUBBLE_CHAT_ROOM,
                label = lastReplyId,
                businessUnit = COMMUNICATION,
                currentSite = CURRENT_SITE_TOKOPEDIA,
                trackerId = Bubbles.TRACKER_ID_37516
            )
        )
    }

    fun clickAddAttachmentFromBubble(shopId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createGeneralEvent(
                event = Event.CLICK_COMMUNICATION,
                category = Bubbles.BUBBLE_CHAT_DETAIL,
                action = Bubbles.CLICK_ADD_ATTACHMENT_BUTTON,
                label = shopId,
                businessUnit = COMMUNICATION,
                currentSite = CURRENT_SITE_TOKOPEDIA,
                trackerId = Bubbles.TRACKER_ID_37517
            )
        )
    }

    fun clickVoucherFromBubble(shopId: String, voucherId: Int) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createGeneralEvent(
                event = Event.CLICK_COMMUNICATION,
                category = Bubbles.BUBBLE_CHAT_DETAIL,
                action = Bubbles.CLICK_VOUCHER,
                label = "$shopId - $voucherId",
                businessUnit = COMMUNICATION,
                currentSite = CURRENT_SITE_TOKOPEDIA,
                trackerId = Bubbles.TRACKER_ID_37518
            )
        )
    }

    fun clickAddAttachmentVoucherFromBubble(shopId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createGeneralEvent(
                event = Event.CLICK_COMMUNICATION,
                category = Bubbles.BUBBLE_CHAT_DETAIL,
                action = Bubbles.CLICK_ADD_ATTACHMENT_VOUCHER,
                label = shopId,
                businessUnit = COMMUNICATION,
                currentSite = CURRENT_SITE_TOKOPEDIA,
                trackerId = Bubbles.TRACKER_ID_37519
            )
        )
    }

    fun clickAddAttachmentProductFromBubble(shopId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createGeneralEvent(
                event = Event.CLICK_COMMUNICATION,
                category = Bubbles.BUBBLE_CHAT_DETAIL,
                action = Bubbles.CLICK_ADD_ATTACHMENT_PRODUCT,
                label = shopId,
                businessUnit = COMMUNICATION,
                currentSite = CURRENT_SITE_TOKOPEDIA,
                trackerId = Bubbles.TRACKER_ID_37520
            )
        )
    }

    fun clickAddAttachmentImageFromBubble(shopId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createGeneralEvent(
                event = Event.CLICK_COMMUNICATION,
                category = Bubbles.BUBBLE_CHAT_DETAIL,
                action = Bubbles.CLICK_ADD_ATTACHMENT_IMAGE,
                label = shopId,
                businessUnit = COMMUNICATION,
                currentSite = CURRENT_SITE_TOKOPEDIA,
                trackerId = Bubbles.TRACKER_ID_37521
            )
        )
    }

    fun clickAddAttachmentInvoiceFromBubble(shopId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createGeneralEvent(
                event = Event.CLICK_COMMUNICATION,
                category = Bubbles.BUBBLE_CHAT_DETAIL,
                action = Bubbles.CLICK_ADD_ATTACHMENT_INVOICE,
                label = shopId,
                businessUnit = COMMUNICATION,
                currentSite = CURRENT_SITE_TOKOPEDIA,
                trackerId = Bubbles.TRACKER_ID_37522
            )
        )
    }

    fun clickChangeStockFromBubble(productId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createGeneralEvent(
                event = Event.CLICK_COMMUNICATION,
                category = Bubbles.BUBBLE_CHAT_DETAIL,
                action = Bubbles.CLICK_CHANGE_STOCK_CTA,
                label = productId,
                businessUnit = COMMUNICATION,
                currentSite = CURRENT_SITE_TOKOPEDIA,
                trackerId = Bubbles.TRACKER_ID_37523
            )
        )
    }

    fun clickSaveStockFromBubble(productId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createGeneralEvent(
                event = Event.CLICK_COMMUNICATION,
                category = Bubbles.BUBBLE_CHAT_DETAIL,
                action = Bubbles.CLICK_SAVE_STOCK,
                label = productId,
                businessUnit = COMMUNICATION,
                currentSite = CURRENT_SITE_TOKOPEDIA,
                trackerId = Bubbles.TRACKER_ID_37692
            )
        )
    }

    fun clickLongHoldMessageFromBubble(replyId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createGeneralEvent(
                event = Event.CLICK_COMMUNICATION,
                category = Bubbles.BUBBLE_CHAT_DETAIL,
                action = Bubbles.CLICK_LONG_HOLD_SELECTED_MESSAGE,
                label = replyId,
                businessUnit = COMMUNICATION,
                currentSite = CURRENT_SITE_TOKOPEDIA,
                trackerId = Bubbles.TRACKER_ID_37693
            )
        )
    }

    fun clickReplySelectedMessageFromBubble(replyId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createGeneralEvent(
                event = Event.CLICK_COMMUNICATION,
                category = Bubbles.BUBBLE_CHAT_DETAIL,
                action = Bubbles.CLICK_REPLY_SELECTED_MESSAGE,
                label = replyId,
                businessUnit = COMMUNICATION,
                currentSite = CURRENT_SITE_TOKOPEDIA,
                trackerId = Bubbles.TRACKER_ID_37698
            )
        )
    }

    fun clickDeleteSelectedMessageFromBubble(replyId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createGeneralEvent(
                event = Event.CLICK_COMMUNICATION,
                category = Bubbles.BUBBLE_CHAT_DETAIL,
                action = Bubbles.CLICK_DELETE_SELECTED_MESSAGE,
                label = replyId,
                businessUnit = COMMUNICATION,
                currentSite = CURRENT_SITE_TOKOPEDIA,
                trackerId = Bubbles.TRACKER_ID_37699
            )
        )
    }

    fun clickCopySelectedMessageFromBubble(replyId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createGeneralEvent(
                event = Event.CLICK_COMMUNICATION,
                category = Bubbles.BUBBLE_CHAT_DETAIL,
                action = Bubbles.CLICK_COPY_SELECTED_MESSAGE,
                label = replyId,
                businessUnit = COMMUNICATION,
                currentSite = CURRENT_SITE_TOKOPEDIA,
                trackerId = Bubbles.TRACKER_ID_37700
            )
        )
    }

    fun clickConfirmDeleteSelectedMessageFromBubble(replyId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createGeneralEvent(
                event = Event.CLICK_COMMUNICATION,
                category = Bubbles.BUBBLE_CHAT_DETAIL,
                action = Bubbles.CLICK_CONFIRM_DELETE_SELECTED_MESSAGE,
                label = replyId,
                businessUnit = COMMUNICATION,
                currentSite = CURRENT_SITE_TOKOPEDIA,
                trackerId = Bubbles.TRACKER_ID_37701
            )
        )
    }

    fun eventClickHeaderMenuBubble(shopId: String?) {
        val bubbleEvent = createBubbleEvent(
            Event.CLICK_COMMUNICATION,
            Bubbles.BUBBLE_CHAT_DETAIL,
            Bubbles.CLICK_HEADER_THREE_BULLETS,
            shopId!!,
            COMMUNICATION,
            CURRENT_SITE_TOKOPEDIA,
            Bubbles.TRACKER_ID_37704
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(bubbleEvent)
    }

    fun eventClickHeaderMenuItemBubble(clickedMenuTitle: String?) {
        val bubbleEvent = createBubbleEvent(
            Event.CLICK_COMMUNICATION,
            Bubbles.BUBBLE_CHAT_DETAIL,
            Bubbles.CLICK_SETTINGS_MENU_CHOICES,
            clickedMenuTitle!!,
            COMMUNICATION,
            CURRENT_SITE_TOKOPEDIA,
            Bubbles.TRACKER_ID_37705
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(bubbleEvent)
    }

    fun eventClickBubbleChat(shopId: String, userId: String, messageId: String) {
        val eventLabel = "$shopId - $messageId - $userId"
        val bubbleEvent = createBubbleEvent(
            Event.CLICK_COMMUNICATION,
            Bubbles.BUBBLE_CHAT_DETAIL,
            Bubbles.CLICK_NEW_NOTIFICATION_BUBBLE_CHAT,
            eventLabel,
            COMMUNICATION,
            CURRENT_SITE_TOKOPEDIA,
            Bubbles.TRACKER_ID_37707
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(bubbleEvent)
    }

    fun eventDismissBubbleChat(shopId: String, userId: String, messageId: String) {
        val eventLabel = "$shopId - $messageId - $userId"
        val bubbleEvent = createBubbleEvent(
            Event.CLICK_COMMUNICATION,
            Bubbles.BUBBLE_CHAT_DETAIL,
            Bubbles.CLICK_DISMISS_NEW_NOTIFICATION_BUBBLE_CHAT,
            eventLabel,
            COMMUNICATION,
            CURRENT_SITE_TOKOPEDIA,
            Bubbles.TRACKER_ID_37708
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(bubbleEvent)
    }

    fun eventClickStickerBubble() {
        val bubbleEvent = createBubbleEvent(
            Event.CLICK_COMMUNICATION,
            Bubbles.BUBBLE_CHAT_DETAIL,
            Bubbles.CLICK_ADD_STICKER,
            "",
            COMMUNICATION,
            CURRENT_SITE_TOKOPEDIA,
            Bubbles.TRACKER_ID_38044
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(bubbleEvent)
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

    private fun getRole(isSeller: Boolean): String {
        return if (isSeller) {
            "seller"
        } else {
            "buyer"
        }
    }

    private fun createBubbleEvent(
        event: String,
        category: String,
        action: String,
        label: String,
        businessUnit: String,
        currentSite: String,
        trackerId: String
    ): Map<String?, Any?>? {
        return mutableMapOf(
            TrackAppUtils.EVENT to event,
            TrackAppUtils.EVENT_CATEGORY to category,
            TrackAppUtils.EVENT_ACTION to action,
            TrackAppUtils.EVENT_LABEL to label,
            KEY_BUSINESS_UNIT to businessUnit,
            KEY_CURRENT_SITE to currentSite,
            TRACKER_ID to trackerId
        )
    }

    object Event {
        const val CHAT_DETAIL = "clickChatDetail"
        const val CLICK_COMMUNICATION = "clickCommunication"
        const val VIEW_COMMUNICATION_IRIS = "viewCommunicationIris"
    }

    object Category {
        const val CHAT_DETAIL = "chat detail"
        const val INBOX_CHAT = "inbox-chat"
        const val PUSH_NOTIF_CHAT = "push notification chat"
    }

    object Bubbles {
        const val BUBBLE_CHAT_DETAIL = "bubble chat detail"

        const val READ_FROM_BUBBLE_CHAT_ROOM = "read from bubble chatroom"
        const val CLICK_ADD_ATTACHMENT_BUTTON = "click add attachment button"
        const val CLICK_VOUCHER = "click voucher"
        const val CLICK_ADD_ATTACHMENT_VOUCHER = "click add attachment - voucher"
        const val CLICK_ADD_ATTACHMENT_PRODUCT = "click add attachment - product"
        const val CLICK_ADD_ATTACHMENT_IMAGE = "click add attachment - image"
        const val CLICK_ADD_ATTACHMENT_INVOICE = "click add attachment - invoice"
        const val CLICK_CHANGE_STOCK_CTA = "click ubah stok CTA"
        const val CLICK_SAVE_STOCK = "click simpan stok"
        const val CLICK_LONG_HOLD_SELECTED_MESSAGE = "long hold on selected message"
        const val CLICK_REPLY_SELECTED_MESSAGE = "click reply on selected message"
        const val CLICK_DELETE_SELECTED_MESSAGE = "click delete on selected message"
        const val CLICK_COPY_SELECTED_MESSAGE = "click salin on selected message"
        const val CLICK_CONFIRM_DELETE_SELECTED_MESSAGE = "click confirm delete selected message"
        const val CLICK_HEADER_THREE_BULLETS = "click header three bullets"
        const val CLICK_SETTINGS_MENU_CHOICES = "click settings menu choices"
        const val CLICK_NEW_NOTIFICATION_BUBBLE_CHAT = "click on new notification bubble chat"
        const val CLICK_DISMISS_NEW_NOTIFICATION_BUBBLE_CHAT = "click dismiss new notification bubble chat"
        const val CLICK_ADD_STICKER = "click add sticker"

        // tracker ID
        const val TRACKER_ID_37516 = "37516"
        const val TRACKER_ID_37517 = "37517"
        const val TRACKER_ID_37518 = "37518"
        const val TRACKER_ID_37519 = "37519"
        const val TRACKER_ID_37520 = "37520"
        const val TRACKER_ID_37521 = "37521"
        const val TRACKER_ID_37522 = "37522"
        const val TRACKER_ID_37523 = "37523"
        const val TRACKER_ID_37692 = "37692"
        const val TRACKER_ID_37693 = "37693"
        const val TRACKER_ID_37698 = "37698"
        const val TRACKER_ID_37699 = "37699"
        const val TRACKER_ID_37700 = "37700"
        const val TRACKER_ID_37701 = "37701"
        const val TRACKER_ID_37704 = "37704"
        const val TRACKER_ID_37705 = "37705"
        const val TRACKER_ID_37707 = "37707"
        const val TRACKER_ID_37708 = "37708"
        const val TRACKER_ID_38044 = "38044"
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
        const val SELLER_IMPRESS_REPORT_TICKER = "seller impress on report ticker"
        const val CLICK_SEND_MSG_ON_NOTIF = "click sent msg on notifpush"
        const val VIEW_BUNDLE_CARD_CHATROOM = "view on bundle card in chatroom"
        const val CLICK_PRODUCT_BUNDLE = "click on product attachment on bundle card"
        const val CLICK_ADD_TO_CART_BUNDLE = "click on add to cart from bundle card"
        const val SELLER_CLICK_REPORT_TICKER = "seller click on report ticker"
        const val SELLER_CLICK_CLOSE_REPORT_TICKER = "seller click close on report ticker"
        const val SELLER_IMPRESS_TICKER_BOTTOMSHEET = "seller impress ticker bottomsheet"
        const val SELLER_CLICK_SHOP_PERFORMANCE = "seller click performa toko in bottomsheet"
        const val SELLER_CLICK_OPERATIONAL_INSIGHT_CTA = "seller click cta wawasan in bottomsheet"
        const val VIEW_TICKER = "user impress ticker"
        const val CLICK_LINK_INSIDE_TICKER = "user click link inside ticker"
        const val CLICK_CLOSE_TICKER = "user click close ticker"
        const val VIEW_ON_PRODUCT_THUMBNAIL = "view on product thumbnail"
        const val CLICK_TAB_REPLY = "click tab tulis pesan"
        const val CLICK_TAB_SRW = "click tab smart reply"
    }

    private const val PRODUCT_INDEX = "0"

    // default value
    private const val EE_VALUE_NONE_OTHER = "none / other"

    // Event Name
    private const val ATC = "add_to_cart"
    private const val VIEW_ITEM_LIST = "view_item_list"
    private const val SELECT_CONTENT = "select_content"

    // Event Category
    private const val EVENT_CATEGORY_CHAT = "chat"

    // Event Action
    private const val CLICK_OCC = "click - Beli Langsung on chat"

    // Event Label
    private const val OCC_LABEL = "fitur : OCC"

    // Other
    private const val CURRENT_SITE = "topchat"
    private const val COMMUNICATION_MEDIA = "Communication & Media"
    private const val CURRENT_SITE_TOKOPEDIA = "tokopediamarketplace"
    private const val COMMUNICATION = "communication"

    // General Keys
    private const val KEY_BUSINESS_UNIT = "businessUnit"
    private const val KEY_CURRENT_SITE = "currentSite"

    // OCC Product Keys
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
    private const val DIMENSION_87 = "dimension87"
    private const val DIMENSION_117 = "dimension117"
    private const val DIMENSION_118 = "dimension118"
    private const val ITEM_BRAND = "item_brand"
    private const val ITEM_CATEGORY = "item_category"
    private const val ITEM_ID = "item_id"
    private const val ITEM_NAME = "item_name"
    private const val ITEM_VARIANT = "item_variant"
    private const val ITEM_LIST = "item_list"
    private const val PRICE = "price"
    private const val QUANTITY = "quantity"
    private const val SHOP_ID = "shop_id"
    private const val SHOP_NAME = "shop_name"
    private const val SHOP_TYPE = "shop_type"
    private const val PRODUCT_ID = "productId"
    private const val USER_ID = "userId"
    private const val TRACKER_ID = "trackerId"
    private const val SHOPID = "shopId"
    private const val INDEX = "index"
}
