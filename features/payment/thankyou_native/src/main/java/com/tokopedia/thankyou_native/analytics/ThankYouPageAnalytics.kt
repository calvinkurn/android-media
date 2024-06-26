package com.tokopedia.thankyou_native.analytics

import android.os.Bundle
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.reflect.TypeToken
import com.tokopedia.analytics.byteio.SubmitOrderResult
import com.tokopedia.analytics.byteio.pdp.AppLogPdp
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.thankyou_native.analytics.EnhancedEcommerceKey.KEY_CREATIVE_NAME
import com.tokopedia.thankyou_native.analytics.EnhancedEcommerceKey.KEY_CREATIVE_SLOT
import com.tokopedia.thankyou_native.analytics.EnhancedEcommerceKey.KEY_ITEM_ID
import com.tokopedia.thankyou_native.analytics.EnhancedEcommerceKey.KEY_ITEM_NAME
import com.tokopedia.thankyou_native.analytics.ParentTrackingKey.KEY_BUSINESS_UNIT
import com.tokopedia.thankyou_native.analytics.ParentTrackingKey.KEY_BUSINESS_UNIT_NON_E_COMMERCE_VALUE
import com.tokopedia.thankyou_native.analytics.ParentTrackingKey.KEY_CURRENT_SITE
import com.tokopedia.thankyou_native.analytics.ParentTrackingKey.KEY_ENVIRONMENT
import com.tokopedia.thankyou_native.analytics.ParentTrackingKey.KEY_EVENT
import com.tokopedia.thankyou_native.analytics.ParentTrackingKey.KEY_EVENT_ACTION
import com.tokopedia.thankyou_native.analytics.ParentTrackingKey.KEY_EVENT_CATEGORY
import com.tokopedia.thankyou_native.analytics.ParentTrackingKey.KEY_EVENT_LABEL
import com.tokopedia.thankyou_native.analytics.ParentTrackingKey.KEY_EVENT_SELECT_CONTENT
import com.tokopedia.thankyou_native.analytics.ParentTrackingKey.KEY_EVENT_VIEW_ITEM
import com.tokopedia.thankyou_native.analytics.ParentTrackingKey.KEY_MERCHANT_CODE
import com.tokopedia.thankyou_native.analytics.ParentTrackingKey.KEY_PAYMENT_ID
import com.tokopedia.thankyou_native.analytics.ParentTrackingKey.KEY_PAYMENT_METHOD
import com.tokopedia.thankyou_native.analytics.ParentTrackingKey.KEY_PROMOTIONS
import com.tokopedia.thankyou_native.analytics.ParentTrackingKey.KEY_SCROOGE_ID
import com.tokopedia.thankyou_native.analytics.ParentTrackingKey.KEY_TRACKER_ID
import com.tokopedia.thankyou_native.data.mapper.*
import com.tokopedia.thankyou_native.di.qualifier.CoroutineBackgroundDispatcher
import com.tokopedia.thankyou_native.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.thankyou_native.domain.model.PurchaseItem
import com.tokopedia.thankyou_native.domain.model.ShopOrder
import com.tokopedia.thankyou_native.domain.model.ThanksPageData
import com.tokopedia.thankyou_native.presentation.adapter.model.BannerItem
import com.tokopedia.track.AFInAppEventParameterName
import com.tokopedia.track.AFInAppEventType
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.interfaces.ContextAnalytics
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext
import org.json.JSONArray
import javax.inject.Inject

class ThankYouPageAnalytics @Inject constructor(
    val userSession: dagger.Lazy<UserSessionInterface>,
    @CoroutineMainDispatcher val mainDispatcher: CoroutineDispatcher,
    @CoroutineBackgroundDispatcher val bgDispatcher: CoroutineDispatcher
) {

    private val IDR = "IDR"

    private lateinit var thanksPageData: ThanksPageData

    private val analyticTracker: ContextAnalytics
        get() = TrackApp.getInstance().gtm

    fun sendScreenAuthenticatedEvent(
        paymentId: String?,
        merchantCode: String?,
        screenName: String
    ) {
        val customDimension: MutableMap<String, String> = mutableMapOf()
        customDimension[KEY_PAYMENT_ID] = paymentId ?: ""
        customDimension[KEY_MERCHANT_CODE] = merchantCode ?: ""
        TrackApp.getInstance().gtm.sendScreenAuthenticated(screenName, customDimension)
    }

    fun postThankYouPageLoadedEvent(thanksPageData: ThanksPageData) {
        if (thanksPageData.pushGtm) {
            when (ThankPageTypeMapper.getThankPageType(thanksPageData)) {
                MarketPlaceThankPage -> sendThankYouPageDataLoadEvent(thanksPageData)
                else -> senDigitalThankYouPageDataLoadEvent(thanksPageData)
            }
            appsFlyerPurchaseEvent(thanksPageData)
            sendBranchIOEvent(thanksPageData)
        } else {
            sendPushGtmFalseEvent(thanksPageData.profileCode, thanksPageData.paymentID)
        }
    }

    fun sendSubmitOrderByteIoTracker(data: ThanksPageData) {
        if (ThankPageTypeMapper.getThankPageType(data) != MarketPlaceThankPage) return
        val prodId = data.shopOrder.joinToString(",") { shop ->
            shop.purchaseItemList.joinToString(",") { item ->
                item.parentProductId?.takeIf { it.isNotEmpty() || !it.contains("null") }
                    ?: item.productId
            }
        }
        AppLogPdp.sendSubmitOrderResult(
            data.eventLabel,
            SubmitOrderResult(
                shippingPrice = data.shopOrder.sumOf { it.shippingAmount.toDouble() },
                discountedShippingPrice = data.shopOrder.sumOf { it.discountShippingAmount.toDouble() },
                totalPayment = data.amount.toDouble(),
                discountedAmount = data.shopOrder.sumOf { it.discountAmount.toDouble() },
                payType = data.gatewayName,
                cartItemId = data.shopOrder.joinToString(",") { shop ->
                    shop.purchaseItemList.joinToString(",") { it.cartId }
                },
                orderId = data.shopOrder.joinToString(",") { it.orderId },
                comboId = data.paymentID,
                summaryInfo = data.customDataOther?.summaryInfo.orEmpty(),
                deliveryInfo = SubmitOrderResult.DeliveryInfo(
                    shippingType = data.shopOrder.joinToString(",") { it.shippingDesc },
                    eta = data.shopOrder.joinToString(",") { it.logisticETA.toString() }
                ).toJsonString(),
                productId = prodId,
            )
        )
    }

    fun sendThankYouPageDataLoadEvent(thanksPageData: ThanksPageData) {
        this.thanksPageData = thanksPageData
        CoroutineScope(mainDispatcher).launchCatchError(block = {
            withContext(bgDispatcher) {
                thanksPageData.shopOrder.forEach { shopOrder ->
                    val data = getParentTrackingNode(thanksPageData, shopOrder)
                    data[ParentTrackingKey.KEY_SHOP_ID] = shopOrder.storeId
                    data[ParentTrackingKey.KEY_SHOP_TYPE] = shopOrder.storeType
                    data[ParentTrackingKey.KEY_LOGISTIC_TYPE] = shopOrder.logisticType
                    data[ParentTrackingKey.KEY_ECOMMERCE] = getEnhancedECommerceNode(shopOrder)
                    data[ParentTrackingKey.IS_NEW_USER] = thanksPageData.isNewUser.toString()
                    data[ParentTrackingKey.NEW_CUSTOMER] = thanksPageData.isNewUser.toString()
                    analyticTracker.sendEnhanceEcommerceEvent(data)
                }
            }
        }, onError = {
            it.printStackTrace()
        })
    }

    private fun senDigitalThankYouPageDataLoadEvent(thanksPageData: ThanksPageData) {
        this.thanksPageData = thanksPageData
        CoroutineScope(mainDispatcher).launchCatchError(block = {
            withContext(bgDispatcher) {
                thanksPageData.customDataOther?.apply {
                    trackingData?.let {
                        processDataForGTM(it)
                    }
                }
            }
        }, onError = {
            it.printStackTrace()
        })
    }

    private fun processDataForGTM(eventData: String) {
        val gson = Gson()
        val eventList: JsonArray = gson.fromJson(eventData, object : TypeToken<JsonArray>() {}.type)

        eventList.forEach { data ->
            val eventMap: MutableMap<String, Any> =
                gson.fromJson(data, object : TypeToken<Map<String, Any>>() {}.type)
            analyticTracker.sendEnhanceEcommerceEvent(eventMap)
        }
    }

    private fun getParentTrackingNode(
        thanksPageData: ThanksPageData,
        shopOrder: ShopOrder
    ): MutableMap<String, Any> {
        return mutableMapOf(
            ParentTrackingKey.KEY_EVENT to thanksPageData.event,
            ParentTrackingKey.KEY_EVENT_CATEGORY to thanksPageData.eventCategory,
            ParentTrackingKey.KEY_EVENT_ACTION to shopOrder.storeType,
            ParentTrackingKey.KEY_EVENT_LABEL to thanksPageData.eventLabel,
            ParentTrackingKey.KEY_PAYMENT_ID to thanksPageData.paymentID,
            ParentTrackingKey.KEY_PAYMENT_STATUS to thanksPageData.paymentStatus,
            ParentTrackingKey.KEY_PAYMENT_TYPE to thanksPageData.paymentType,
            KEY_CURRENT_SITE to thanksPageData.currentSite,
            KEY_BUSINESS_UNIT to thanksPageData.businessUnit,
            ParentTrackingKey.KEY_PROFILE_ID to thanksPageData.profileCode
        )
    }

    private fun getEnhancedECommerceNode(orderedItem: ShopOrder): Map<String, Any> {
        return mapOf(
            ECommerceNodeTrackingKey.KEY_CURRENCY_CODE to IDR,
            ECommerceNodeTrackingKey.KEY_PURCHASE to getPurchasedEventNodeByShop(orderedItem)
        )
    }

    private fun getPurchasedEventNodeByShop(orderedItem: ShopOrder): Map<String, Any> {
        return mapOf(
            PurchaseNodeTrackingKey.KEY_ACTION_FIELD to getActionFieldNode(orderedItem),
            PurchaseNodeTrackingKey.KEY_PRODUCTS to getECommerceProductNodeList(orderedItem.purchaseItemList)
        )
    }

    private fun getActionFieldNode(orderedItem: ShopOrder): Map<String, Any?> {
        val storeName = orderedItem.storeName ?: ""
        return mapOf(
            ParentTrackingKey.KEY_ID to orderedItem.orderId,
            ActionFieldNodeTrackingKey.KEY_AFFILIATION to storeName,
            ActionFieldNodeTrackingKey.KEY_REVENUE to orderedItem.revenue.toString(),
            ActionFieldNodeTrackingKey.KEY_TAX to if (orderedItem.tax > 0) orderedItem.tax else null,
            ActionFieldNodeTrackingKey.KEY_SHIPPING to orderedItem.shippingAmount.toString(),
            ActionFieldNodeTrackingKey.KEY_COUPON to orderedItem.coupon
        )
    }

    private fun getECommerceProductNodeList(purchaseItemList: ArrayList<PurchaseItem>): ArrayList<Map<String, Any?>> {
        val productNodeList: ArrayList<Map<String, Any?>> = arrayListOf()
        purchaseItemList.forEach { item ->
            val productNodeMap = HashMap<String, Any?>()
            productNodeMap[ProductNodeTrackingKey.KEY_NAME] = item.productName
            productNodeMap[ParentTrackingKey.KEY_ID] = item.productId
            productNodeMap[ProductNodeTrackingKey.KEY_PRICE] = item.price.toString()
            productNodeMap[ProductNodeTrackingKey.KEY_BRAND] = item.productBrand
            productNodeMap[ParentTrackingKey.AF_KEY_CATEGORY_NAME] =
                addSlashInCategory(item.category)
            productNodeMap[ProductNodeTrackingKey.KEY_VARIANT] = item.variant
            productNodeMap[ParentTrackingKey.KEY_QTY] = item.quantity
            productNodeMap[ProductNodeTrackingKey.KEY_DIMENSION83] = item.bebasOngkirDimension
            productNodeList.add(productNodeMap)
        }
        return productNodeList
    }

    fun sendBackPressedEvent(profileId: String, paymentId: String) {
        val map = TrackAppUtils.gtmData(
            EVENT_NAME_CLICK_ORDER,
            EVENT_CATEGORY_ORDER_COMPLETE,
            EVENT_ACTION_CLICK_BACK,
            ""
        )
        map[ParentTrackingKey.KEY_PROFILE_ID] = profileId
        addCommonTrackingData(map, paymentId)
        analyticTracker.sendGeneralEvent(map)
    }

    fun sendLihatDetailClickEvent(profileId: String, pageType: PageType?, paymentId: String) {
        val eventLabel = when (pageType) {
            is InstantPaymentPage -> EVENT_LABEL_INSTANT
            is ProcessingPaymentPage -> EVENT_LABEL_PROCESSING
            is WaitingPaymentPage -> EVENT_LABEL_DEFERRED
            else -> ""
        }
        val map = TrackAppUtils.gtmData(
            EVENT_NAME_CLICK_ORDER,
            EVENT_CATEGORY_ORDER_COMPLETE,
            EVENT_ACTION_LIHAT_DETAIL,
            eventLabel
        )
        map[ParentTrackingKey.KEY_PROFILE_ID] = profileId
        addCommonTrackingData(map, paymentId)
        analyticTracker.sendGeneralEvent(map)
    }

    fun sendCheckTransactionListEvent(profileId: String, paymentId: String) {
        val map = TrackAppUtils.gtmData(
            EVENT_NAME_CLICK_ORDER,
            EVENT_CATEGORY_ORDER_COMPLETE,
            EVENT_ACTION_CHECK_TRANSACTION_LIST,
            ""
        )
        map[ParentTrackingKey.KEY_PROFILE_ID] = profileId
        addCommonTrackingData(map, paymentId)
        analyticTracker.sendGeneralEvent(map)
    }

    fun onCheckPaymentStatusClick(profileId: String, paymentId: String) {
        val map = TrackAppUtils.gtmData(
            EVENT_NAME_CLICK_ORDER,
            EVENT_CATEGORY_ORDER_COMPLETE,
            EVENT_ACTION_CLICK_CHECK_PAYMENT_STATUS,
            ""
        )
        addCommonTrackingData(map, paymentId)
        map[ParentTrackingKey.KEY_PROFILE_ID] = profileId
        analyticTracker.sendGeneralEvent(map)
    }

    fun sendBelanjaLagiClickEvent(profileId: String, pageType: PageType?, paymentId: String) {
        val eventLabel = when (pageType) {
            is InstantPaymentPage -> EVENT_LABEL_INSTANT
            is ProcessingPaymentPage -> EVENT_LABEL_PROCESSING
            is WaitingPaymentPage -> EVENT_LABEL_DEFERRED
            else -> ""
        }
        val map = TrackAppUtils.gtmData(
            EVENT_NAME_CLICK_ORDER,
            EVENT_CATEGORY_ORDER_COMPLETE,
            EVENT_ACTION_BELANJA_LAGI,
            eventLabel
        )
        map[ParentTrackingKey.KEY_PROFILE_ID] = profileId
        addCommonTrackingData(map, paymentId)
        analyticTracker.sendGeneralEvent(map)
    }

    fun sendSalinButtonClickEvent(profileId: String, paymentMethod: String, paymentId: String) {
        val map = TrackAppUtils.gtmData(
            EVENT_NAME_CLICK_ORDER,
            EVENT_CATEGORY_ORDER_COMPLETE,
            EVENT_ACTION_SALIN_CLICK,
            paymentMethod
        )
        map[ParentTrackingKey.KEY_PROFILE_ID] = profileId
        addCommonTrackingData(map, paymentId)
        analyticTracker.sendGeneralEvent(map)
    }

    fun sendOnHowtoPayClickEvent(profileId: String, paymentId: String) {
        val map = TrackAppUtils.gtmData(
            EVENT_NAME_CLICK_ORDER,
            EVENT_CATEGORY_ORDER_COMPLETE,
            EVENT_ACTION_LIHAT_CARA_PEMBARYAN_CLICK,
            ""
        )
        map[ParentTrackingKey.KEY_PROFILE_ID] = profileId
        addCommonTrackingData(map, paymentId)
        analyticTracker.sendGeneralEvent(map)
    }

    fun sendBannerClickEvent(thanksPageData: ThanksPageData, banner: BannerItem, position: Int) {
        val promotionBundle = getEnhancedECommerceBanner(banner, position)
        val bundle = Bundle().apply {
            putString(KEY_EVENT, KEY_EVENT_SELECT_CONTENT)
            putString(KEY_EVENT_CATEGORY, EVENT_CATEGORY_ORDER_COMPLETE)
            putString(KEY_EVENT_ACTION, EVENT_ACTION_CLICK_BANNER)
            putString(KEY_EVENT_LABEL, String.EMPTY)
            putString(KEY_TRACKER_ID, TRACKER_45032)
            putString(KEY_CURRENT_SITE, thanksPageData.currentSite)
            putParcelableArrayList(KEY_PROMOTIONS, arrayListOf(promotionBundle))
        }

        analyticTracker.sendEnhanceEcommerceEvent(KEY_EVENT_SELECT_CONTENT, bundle)
    }

    fun sendBannerImpressionEvent(
        thanksPageData: ThanksPageData,
        banner: BannerItem,
        position: Int
    ) {
        val promotionBundle = getEnhancedECommerceBanner(banner, position)
        val bundle = Bundle().apply {
            putString(KEY_EVENT, KEY_EVENT_VIEW_ITEM)
            putString(KEY_EVENT_CATEGORY, EVENT_CATEGORY_ORDER_COMPLETE)
            putString(KEY_EVENT_ACTION, EVENT_ACTION_VIEW_BANNER)
            putString(KEY_EVENT_LABEL, String.EMPTY)
            putString(KEY_TRACKER_ID, TRACKER_45031)
            putString(KEY_CURRENT_SITE, thanksPageData.currentSite)
            putParcelableArrayList(KEY_PROMOTIONS, arrayListOf(promotionBundle))
        }

        analyticTracker.sendEnhanceEcommerceEvent(KEY_EVENT_VIEW_ITEM, bundle)
    }

    private fun getEnhancedECommerceBanner(
        banner: BannerItem,
        position: Int
    ): Bundle {
        return Bundle().apply {
            putString(KEY_ITEM_NAME, banner.applink)
            putString(KEY_ITEM_ID, banner.itemId)
            putString(KEY_CREATIVE_NAME, banner.assetUrl)
            putString(KEY_CREATIVE_SLOT, position.toString())
        }
    }

    private fun addCommonTrackingData(map: MutableMap<String, Any>, paymentId: String) {
        map[ParentTrackingKey.KEY_USER_ID] = userSession.get().userId
        map[ParentTrackingKey.KEY_PAYMENT_ID_NON_E_COMMERCE] = paymentId
        map[KEY_BUSINESS_UNIT] = KEY_BUSINESS_UNIT_NON_E_COMMERCE_VALUE
    }

    fun sendPushGtmFalseEvent(profileId: String, paymentId: String) {
        val map = TrackAppUtils.gtmData(
            EVENT_NAME_CLICK_ORDER,
            EVENT_CATEGORY_ORDER_COMPLETE,
            EVENT_ACTION_PUSH_GTM_FALSE,
            paymentId
        )
        map[ParentTrackingKey.KEY_PROFILE_ID] = profileId
        analyticTracker.sendGeneralEvent(map)
    }

    fun appsFlyerPurchaseEvent(thanksPageData: ThanksPageData) {
        CoroutineScope(mainDispatcher).launchCatchError(block = {
            withContext(bgDispatcher) {
                val orderIds: MutableList<String> = arrayListOf()

                val afValue: MutableMap<String, Any> = mutableMapOf()
                var quantity = 0
                val productList: MutableList<String> = arrayListOf()
                val productIds: MutableList<String> = arrayListOf()
                val productCategory: MutableList<String> = arrayListOf()
                val productArray = JSONArray()

                var shipping = 0f

                thanksPageData.shopOrder.forEach { shopOrder ->
                    orderIds.add(shopOrder.orderId)
                    shipping += shopOrder.shippingAmount
                    shopOrder.purchaseItemList.forEach { productItem ->
                        val productObj: org.json.JSONObject = org.json.JSONObject()
                        productIds.add(productItem.productId)
                        productList.add(productItem.productName)
                        productCategory.add(productItem.category)
                        productObj.put(ParentTrackingKey.KEY_ID, productItem.productId)
                        productObj.put(ParentTrackingKey.KEY_QTY, productItem.quantity)
                        quantity += productItem.quantity
                        productArray.put(productObj)
                    }
                }

                afValue[AFInAppEventParameterName.REVENUE] = thanksPageData.amount
                afValue[AFInAppEventParameterName.CONTENT_ID] = productIds
                afValue[AFInAppEventParameterName.QUANTITY] = quantity
                afValue[AFInAppEventParameterName.RECEIPT_ID] = thanksPageData.paymentID
                afValue[AFInAppEventType.ORDER_ID] = orderIds
                afValue[ParentTrackingKey.AF_SHIPPING_PRICE] = shipping
                afValue[ParentTrackingKey.AF_PURCHASE_SITE] =
                    when (ThankPageTypeMapper.getThankPageType(thanksPageData)) {
                        MarketPlaceThankPage -> MARKET_PLACE
                        else -> DIGITAL
                    }
                afValue[AFInAppEventParameterName.CURRENCY] = ParentTrackingKey.VALUE_IDR
                afValue[ParentTrackingKey.AF_VALUE_PRODUCTTYPE] = productList
                afValue[ParentTrackingKey.AF_KEY_CATEGORY_NAME] = productCategory
                afValue[AFInAppEventParameterName.CONTENT_TYPE] =
                    ParentTrackingKey.AF_VALUE_PRODUCTTYPE

                val criteoAfValue: Map<String, Any> = java.util.HashMap(afValue)
                if (productArray.length() > 0) {
                    val afContent: String = productArray.toString()
                    afValue[AFInAppEventParameterName.CONTENT] = afContent
                }
                TrackApp.getInstance().appsFlyer.sendTrackEvent(AFInAppEventType.PURCHASE, afValue)
                TrackApp.getInstance().appsFlyer.sendTrackEvent(
                    ParentTrackingKey.AF_KEY_CRITEO,
                    criteoAfValue
                )
            }
        }, onError = { it.printStackTrace() })
    }

    fun sendBranchIOEvent(thanksPageData: ThanksPageData) {
        CoroutineScope(mainDispatcher).launchCatchError(block = {
            withContext(bgDispatcher) {
                BranchPurchaseEvent(userSession.get(), thanksPageData).sendBranchPurchaseEvent()
            }
        }, onError = { it.printStackTrace() })
    }

    fun sendViewShareIcon(
        userShareType: String = "general",
        orderIdList: String
    ) {
        val map = TrackAppUtils.gtmData(
            EVENT_NAME_VIEW_COMMUNICATION,
            EVENT_CATEGORY_SHARE,
            EVENT_ACTION_VIEW_SHARE,
            "$userShareType - $orderIdList"
        )
        map[KEY_TRACKER_ID] = TRACKER_46217
        map[KEY_BUSINESS_UNIT] = BUSINESS_UNIT_SHARE
        map[KEY_CURRENT_SITE] = CURRENT_SITE_MARKETPLACE
        analyticTracker.sendGeneralEvent(map)
    }

    fun sendClickShareIcon(
        userId: String,
        orderIdList: String
    ) {
        val map = TrackAppUtils.gtmData(
            EVENT_NAME_CLICK_COMMUNICATION,
            EVENT_CATEGORY_SHARE,
            EVENT_ACTION_CLICK_SHARE,
            "$userId - $orderIdList"
        )
        map[KEY_TRACKER_ID] = TRACKER_46218
        map[KEY_BUSINESS_UNIT] = BUSINESS_UNIT_SHARE
        map[KEY_CURRENT_SITE] = CURRENT_SITE_MARKETPLACE
        analyticTracker.sendGeneralEvent(map)
    }

    private fun addSlashInCategory(category: String?): String {
        return if (category.isNullOrBlank()) {
            ""
        } else {
            category.replace("_", " / ")
        }
    }

    fun sendCtaClickAnalytic(isPrimary: Boolean, text: String, applink: String, paymentId: String, merchantCode: String?, paymentMethod: String) {
        val map = TrackAppUtils.gtmData(
            EVENT_NAME_CLICK_PAMENT,
            EVENT_CATEGORY_ORDER_COMPLETE,
            if (isPrimary) EVENT_ACTION_CLICK_PRIMARY_CTA else EVENT_ACTION_CLICK_SECONDARY_CTA,
            "$text - $applink"
        )
        val trackerId = if (isPrimary) TRACKER_50686 else TRACKER_50687
        map[KEY_TRACKER_ID] = trackerId
        map[KEY_CURRENT_SITE] = CURRENT_SITE_MARKETPLACE
        map[KEY_MERCHANT_CODE] = merchantCode.orEmpty()
        map[KEY_SCROOGE_ID] = String.EMPTY
        map[KEY_PAYMENT_METHOD] = paymentMethod
        map[KEY_ENVIRONMENT] = ANDROID
        addCommonTrackingData(map, paymentId)
        analyticTracker.sendGeneralEvent(map)
    }

    companion object {
        const val EVENT_NAME_CLICK_ORDER = "clickOrder"
        const val EVENT_NAME_CLICK_PAMENT = "clickPayment"
        const val EVENT_NAME_VIEW_COMMUNICATION = "viewCommunicationIris"
        const val EVENT_NAME_CLICK_COMMUNICATION = "clickCommunication"

        const val EVENT_CATEGORY_ORDER_COMPLETE = "order complete"
        const val EVENT_CATEGORY_SHARE = "share - thank you page"

        const val EVENT_ACTION_CLICK_BACK = "click back arrow"
        const val EVENT_ACTION_LIHAT_DETAIL = "click lihat detail tagihan"
        const val EVENT_ACTION_CHECK_TRANSACTION_LIST = "click check transactions list"
        const val EVENT_ACTION_BELANJA_LAGI = "click belanja lagi"
        const val EVENT_ACTION_SALIN_CLICK = "click salin kode pembayaran"
        const val EVENT_ACTION_LIHAT_CARA_PEMBARYAN_CLICK = "click lihat cara pembayaran"
        const val EVENT_ACTION_CLICK_CHECK_PAYMENT_STATUS = "click cek status pembayaran"
        const val EVENT_ACTION_CLICK_BANNER = "click banner in thank you page"
        const val EVENT_ACTION_VIEW_BANNER = "view banner in thank you page"
        const val EVENT_ACTION_VIEW_SHARE = "view - share on thank you page"
        const val EVENT_ACTION_CLICK_SHARE = "click - share on thank you page"
        const val EVENT_ACTION_CLICK_PRIMARY_CTA = "click primary button (right button)"
        const val EVENT_ACTION_CLICK_SECONDARY_CTA = "click secondary button (left button)"

        const val EVENT_LABEL_INSTANT = "instant"
        const val EVENT_LABEL_DEFERRED = "deffer"
        const val EVENT_LABEL_PROCESSING = "processing"

        const val EVENT_ACTION_PUSH_GTM_FALSE = "push false gtm"
        const val TRACKER_45032 = "45032"
        const val TRACKER_45031 = "45031"
        const val TRACKER_46217 = "46217"
        const val TRACKER_46218 = "46218"
        const val TRACKER_50686 = "50686"
        const val TRACKER_50687 = "50687"

        const val BUSINESS_UNIT_SHARE = "sharingexperience"
        const val CURRENT_SITE_MARKETPLACE = "tokopediamarketplace"
        const val ANDROID = "android"
    }
}

object ParentTrackingKey {
    val KEY_EVENT = "event"
    val KEY_EVENT_CATEGORY = "eventCategory"
    val KEY_EVENT_ACTION = "eventAction"
    val KEY_EVENT_LABEL = "eventLabel"
    val KEY_PAYMENT_ID = "payment_id"
    val KEY_MERCHANT_CODE = "merchantCode"
    val KEY_PAYMENT_STATUS = "payment_status"
    val KEY_PAYMENT_TYPE = "payment_type"
    val KEY_EVENT_VIEW_ITEM = "view_item"
    val KEY_EVENT_SELECT_CONTENT = "select_content"
    val PROMO_CLICK = "promoClick"
    val PROMO_VIEW = "promoView"
    val KEY_SCROOGE_ID = "scroogeId"
    val KEY_PAYMENT_METHOD = "paymentMethod"
    val KEY_ENVIRONMENT = "environment"

    val KEY_SHOP_ID = "shopId"
    val KEY_SHOP_TYPE = "shopType"
    val KEY_LOGISTIC_TYPE = "logistic_type"
    val KEY_ECOMMERCE = "ecommerce"
    val KEY_CURRENT_SITE = "currentSite"
    val KEY_BUSINESS_UNIT = "businessUnit"
    val KEY_PROMOTIONS = "promotions"
    const val IS_NEW_USER = "isNewUser"
    const val NEW_CUSTOMER = "new_customer"
    const val KEY_ID = "id"
    const val KEY_QTY = "quantity"
    const val AF_SHIPPING_PRICE = "af_shipping_price"
    const val AF_PURCHASE_SITE = "af_purchase_site"
    const val AF_VALUE_PRODUCTTYPE = "product"
    const val VALUE_IDR = "IDR"
    const val AF_KEY_CATEGORY_NAME = "category"
    const val AF_KEY_CRITEO = "criteo_track_transaction"

    const val KEY_USER_ID = "userId"
    const val KEY_PROFILE_ID = "profileId"
    const val KEY_PAYMENT_ID_NON_E_COMMERCE = "paymentId"
    const val KEY_BUSINESS_UNIT_NON_E_COMMERCE_VALUE = "payment"
    const val KEY_TRACKER_ID = "trackerId"
}

object ECommerceNodeTrackingKey {
    val KEY_CURRENCY_CODE = "currencyCode"
    val KEY_PURCHASE = "purchase"
}

object PurchaseNodeTrackingKey {
    val KEY_ACTION_FIELD = "actionField"
    val KEY_PRODUCTS = "products"
}

object ActionFieldNodeTrackingKey {
    val KEY_AFFILIATION = "affiliation"
    val KEY_REVENUE = "revenue"
    val KEY_TAX = "tax"
    val KEY_SHIPPING = "shipping"
    val KEY_COUPON = "coupon"
}

object ProductNodeTrackingKey {
    val KEY_NAME = "name"
    val KEY_PRICE = "price"
    val KEY_BRAND = "brand"
    val KEY_VARIANT = "variant"
    val KEY_DIMENSION83 = "dimension83"
}

object EnhancedEcommerceKey {
    val KEY_CREATIVE_NAME = "creative_name"
    val KEY_CREATIVE_SLOT = "creative_slot"
    val KEY_ITEM_ID = "item_id"
    val KEY_ITEM_NAME = "item_name"
}
