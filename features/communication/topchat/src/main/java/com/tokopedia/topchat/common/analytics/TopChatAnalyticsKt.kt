package com.tokopedia.topchat.common.analytics

import android.content.Context
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
import kotlin.collections.ArrayList

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
        itemBundle.putString(DIMENSION_40, element.getProductSource(sourcePage))
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
            EE_VALUE_NONE_OTHER
        } else value
    }

    private fun <T> getValueOrEmpty(value: List<T>): String {
        return if (value.isEmpty()) {
            EE_VALUE_NONE_OTHER
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
    private fun getItemBundle(
        bundleItems: List<BundleItem>,
        bundleId: String,
        bundleType: String,
        source: String,
        quantity: Boolean = false,
        shop_id: String = "",
        shop_name: String = "",
        shop_type: Boolean = false,
    ): ArrayList<Bundle>{
        var listItemBundles = ArrayList<Bundle>()
        for (item in bundleItems){
            var itemBundle = Bundle()
            itemBundle.putString(DIMENSION_117, setValueOrDefault(bundleType))
            itemBundle.putString(DIMENSION_118, setValueOrDefault(bundleId))
            itemBundle.putString(
                DIMENSION_40,
                setValueOrDefault("/$source - product bundling - $bundleType")
            )
            itemBundle.putString(DIMENSION_87, setValueOrDefault(source))
            itemBundle.putString("index",bundleItems.indexOf(item).toString())
            itemBundle.putString(ITEM_BRAND, EE_VALUE_NONE_OTHER)
            itemBundle.putString(ITEM_CATEGORY, setValueOrDefault(""))
            itemBundle.putString(ITEM_ID, setValueOrDefault(item.productId))
            itemBundle.putString(ITEM_NAME, setValueOrDefault(item.name))
            itemBundle.putString(ITEM_VARIANT, EE_VALUE_NONE_OTHER)
            itemBundle.putString(PRICE, item.price.toString())
            if(quantity){
                itemBundle.putString(QUANTITY, item.quantity)
            }
            if(shop_id.isNotEmpty()){
                itemBundle.putString(SHOP_ID, shop_id)
            }
            if(shop_name.isNotEmpty()){
                itemBundle.putString(SHOP_NAME, shop_name)
            }
            if(shop_type){
                itemBundle.putString(SHOP_TYPE, "")
            }
            listItemBundles.add(itemBundle)
        }

        return listItemBundles
    }

    private fun getItemIdList(bundleItems: List<BundleItem>): List<String>{
        var productIdList = listOf<String>()
        for(item in bundleItems){
            productIdList += setValueOrDefault(item.productId)
        }
        return  productIdList
    }

    fun eventViewProductBundlingBroadcast(
        blastId: String,
        statusBundle: String,
        bundleId: String,
        bundleType: Int,
        source: String,
        bundleItems: List<BundleItem>,
        shopId: String,
        userId: String
    ) {
        var bundleTypeString = bundleTypeMapper(bundleType)
        var listItemBundles = getItemBundle(bundleItems, bundleId, bundleTypeString, source)

        var eventDataLayer = Bundle()

        eventDataLayer.putString(TrackAppUtils.EVENT, VIL)
        eventDataLayer.putString(TrackAppUtils.EVENT_ACTION, Action.VIEW_BUNDLE_CART_CHATROOM)
        eventDataLayer.putString(TrackAppUtils.EVENT_CATEGORY, Category.CHAT_DETAIL)
        eventDataLayer.putString(TrackAppUtils.EVENT_LABEL, "$blastId - $statusBundle - $bundleId - bundling")
        eventDataLayer.putString(TRACKER_ID, "35596")
        eventDataLayer.putString(KEY_BUSINESS_UNIT, COMMUNICATION_MEDIA)
        eventDataLayer.putString(KEY_CURRENT_SITE, CURRENT_SITE)

        var productIdList = getItemIdList(bundleItems)

        eventDataLayer.putString(ITEM_LIST, productIdList.toString())
        eventDataLayer.putParcelableArrayList(
            AddToCartExternalAnalytics.EE_VALUE_ITEMS,
            listItemBundles
        )
        eventDataLayer.putString(USER_ID, userId)
        eventDataLayer.putString(SHOP_ID, shopId)

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(VIL,eventDataLayer)


    }
    fun eventClickBundle(
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
                businessUnit = COMMUNICATION,
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
        bundleType: Int,
        source: String,
        bundleItems: List<BundleItem>,
        shopId: String,
        userId: String
    ) {
        var bundleTypeString = bundleTypeMapper(bundleType)
        var listItemBundles = getItemBundle(bundleItems, bundleId, bundleTypeString, source)

        var eventDataLayer = Bundle()

        eventDataLayer.putString(TrackAppUtils.EVENT, SC)
        eventDataLayer.putString(TrackAppUtils.EVENT_ACTION, Action.CLICK_PRODUCT_BUNDLE)
        eventDataLayer.putString(TrackAppUtils.EVENT_CATEGORY, Category.CHAT_DETAIL)
        eventDataLayer.putString(TrackAppUtils.EVENT_LABEL, "$blastId - $statusBundle - $bundleId - bundling - ${bundleItems[0].productId.toString()}")
        eventDataLayer.putString(TRACKER_ID, "35598")
        eventDataLayer.putString(KEY_BUSINESS_UNIT, COMMUNICATION_MEDIA)
        eventDataLayer.putString(KEY_CURRENT_SITE, CURRENT_SITE)

        var productIdList = getItemIdList(bundleItems)

        eventDataLayer.putString(ITEM_LIST, productIdList.toString())
        eventDataLayer.putParcelableArrayList(
            AddToCartExternalAnalytics.EE_VALUE_ITEMS,
            listItemBundles
        )
        eventDataLayer.putString(USER_ID, userId)
        eventDataLayer.putString(SHOP_ID, shopId)

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(SC,eventDataLayer)

    }

    fun eventClickCtaOnProductBundlingBroadcast(
        blastId: String,
        statusBundle: String,
        bundleId: String,
        bundleItems: List<BundleItem>,
        bundleType: Int,
        source: String,
        shopId: String,
        shopName: String,
        userId: String
    ) {
        var bundleTypeString = bundleTypeMapper(bundleType)
        var listItemBundles = getItemBundle(
            bundleItems,
            bundleId,
            bundleTypeString,
            source,
            true,
            shopId,
            shopName,
            true
        )

        var eventDataLayer = Bundle()

        eventDataLayer.putString(TrackAppUtils.EVENT, ATC)
        eventDataLayer.putString(TrackAppUtils.EVENT_ACTION, Action.CLICK_ADD_TO_CART_BUNDLE)
        eventDataLayer.putString(TrackAppUtils.EVENT_CATEGORY, Category.CHAT_DETAIL)
        eventDataLayer.putString(TrackAppUtils.EVENT_LABEL, "$blastId - $statusBundle - $bundleId - bundling - ${bundleItems[0].productId.toString()}")
        eventDataLayer.putString(TRACKER_ID, "35599")
        eventDataLayer.putString(KEY_BUSINESS_UNIT, COMMUNICATION_MEDIA)
        eventDataLayer.putString(KEY_CURRENT_SITE, CURRENT_SITE)

        eventDataLayer.putParcelableArrayList(
            AddToCartExternalAnalytics.EE_VALUE_ITEMS,
            listItemBundles
        )
        eventDataLayer.putString(USER_ID, userId)
        eventDataLayer.putString(SHOP_ID, shopId)

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(SC,eventDataLayer)



    }
    fun eventSeenProductAttachment(
        context: Context,
        product : ProductAttachmentUiModel,
        user : UserSessionInterface,
        amISeller : Boolean
    ){
        var eventLabel = product.getEventLabelImpression(amISeller)
        var itemBundle = Bundle()
        itemBundle.putString("index", PRODUCT_INDEX.toString())
        itemBundle.putString(ITEM_BRAND,"none / other")
        itemBundle.putString(ITEM_CATEGORY, setValueOrDefault(product.category))
        itemBundle.putString(ITEM_ID, setValueOrDefault(product.productId))
        itemBundle.putString(ITEM_NAME, setValueOrDefault(product.productName))
        itemBundle.putString(ITEM_VARIANT, EE_VALUE_NONE_OTHER)
        itemBundle.putString(PRICE, product.productPrice + 0.0)

        var eventDataLayer = Bundle()
        eventDataLayer.putString(TrackAppUtils.EVENT, VIL)
        eventDataLayer.putString(TrackAppUtils.EVENT_ACTION, Action.VIEW_ON_PRODUCT_THUMBNAIL)
        eventDataLayer.putString(TrackAppUtils.EVENT_CATEGORY, Category.CHAT_DETAIL)
        eventDataLayer.putString(TrackAppUtils.EVENT_LABEL, eventLabel)
        eventDataLayer.putString(TRACKER_ID, "14824")
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
        eventDataLayer.putString(USER_ID, setValueOrDefault(user.userId))

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(VIL,eventDataLayer)
    }

    fun trackSuccessDoBuyAndAtc(
        element: AddToCartParam,
        data: DataModel?,
        shopName: String,
        eventAction: String,
        userId: String
    ){
        var itemBundle = Bundle()
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

        var eventDataLayer = Bundle()
        eventDataLayer.putString(TrackAppUtils.EVENT, ATC)
        eventDataLayer.putString(TrackAppUtils.EVENT_ACTION, eventAction)
        eventDataLayer.putString(TrackAppUtils.EVENT_CATEGORY, Category.CHAT_DETAIL)
        eventDataLayer.putString(TrackAppUtils.EVENT_LABEL, element.getAtcDimension40(sourcePage)+"-"+element.blastId)
        if(eventAction == EVENT_ACTION_ATC) {
            eventDataLayer.putString(TRACKER_ID, "14826")
        }
        else if (eventAction == EVENT_ACTION_BUY){
            eventDataLayer.putString(TRACKER_ID,"14827")
        }
        else{
            eventDataLayer.putString(TRACKER_ID,"Error")
        }
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
        eventDataLayer.putString(USER_ID, setValueOrDefault(userId))

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(ATC,eventDataLayer)

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

    private fun bundleTypeMapper(bundleType: Int): String{
        when(bundleType){
            1 -> return "single"
            2 -> return "multiple"
        }
        return ""
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
        const val CLICK_SEND_AFTER_ADD_PRODUCT_VARIANT = "click kirim after pilih product variant"
        const val VIEW_ON_PRODUCT_THUMBNAIL = "view on product thumbnail"
    }

    const val PRODUCT_INDEX = 1

    // default value
    private const val EE_VALUE_NONE_OTHER = "none / other"

    //Event Name
    private const val ATC = "add_to_cart"
    private const val VIL = "view_item_list"
    private const val SC = "select_content"

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
}