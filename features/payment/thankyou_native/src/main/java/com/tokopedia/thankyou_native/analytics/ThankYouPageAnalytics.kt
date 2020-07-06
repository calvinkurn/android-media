package com.tokopedia.thankyou_native.analytics

import android.os.Bundle
import com.appsflyer.AFInAppEventParameterName
import com.appsflyer.AFInAppEventType
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.reflect.TypeToken
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.linker.LinkerConstants
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.model.LinkerCommerceData
import com.tokopedia.thankyou_native.data.mapper.*
import com.tokopedia.thankyou_native.di.qualifier.CoroutineBackgroundDispatcher
import com.tokopedia.thankyou_native.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.thankyou_native.domain.model.PurchaseItem
import com.tokopedia.thankyou_native.domain.model.ShopOrder
import com.tokopedia.thankyou_native.domain.model.ThanksPageData
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.interfaces.ContextAnalytics
import com.tokopedia.user.session.UserSession
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext
import org.json.JSONArray
import javax.inject.Inject


class ThankYouPageAnalytics @Inject constructor(
        @CoroutineMainDispatcher val mainDispatcher: CoroutineDispatcher,
        @CoroutineBackgroundDispatcher val bgDispatcher: CoroutineDispatcher
) {

    private val IDR = "IDR"

    private lateinit var thanksPageData: ThanksPageData

    private val analyticTracker: ContextAnalytics
        get() = TrackApp.getInstance().gtm

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
                    analyticTracker.sendEnhanceEcommerceEvent(data)
                }
            }
        }, onError = {
            it.printStackTrace()
        })
    }

    fun sendigitalThankYouPageDataLoadEvent(thanksPageData: ThanksPageData) {
        this.thanksPageData = thanksPageData
        CoroutineScope(mainDispatcher).launchCatchError(block = {
            withContext(bgDispatcher) {
                thanksPageData.shopOrder.forEach { shopOrder ->
                    processDataForGTM(thanksPageData.thanksCustomization.trackingData)
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
            val eventMap: MutableMap<String, Any> = gson.fromJson(data, object : TypeToken<Map<String, Any>>(){}.type)
            analyticTracker.sendEnhanceEcommerceEvent(eventMap)
        }
    }

    private fun getBundleFromMap(dataMap: Map<String, Any> ): Bundle {
        var bundle = Bundle()
        for (entry in dataMap.entries) {
            bundle.putString(entry.key, entry.value?.toString())
        }

        return bundle
    }

    private fun getParentTrackingNode(thanksPageData: ThanksPageData, shopOrder: ShopOrder): MutableMap<String, Any> {
        return mutableMapOf(
                ParentTrackingKey.KEY_EVENT to thanksPageData.event,
                ParentTrackingKey.KEY_EVENT_CATEGORY to thanksPageData.eventCategory,
                ParentTrackingKey.KEY_EVENT_ACTION to shopOrder.storeType,
                ParentTrackingKey.KEY_EVENT_LABEL to thanksPageData.eventLabel,
                ParentTrackingKey.KEY_PAYMENT_ID to thanksPageData.paymentID,
                ParentTrackingKey.KEY_PAYMENT_STATUS to thanksPageData.paymentStatus,
                ParentTrackingKey.KEY_PAYMENT_TYPE to thanksPageData.paymentType,
                ParentTrackingKey.KEY_CURRENT_SITE to thanksPageData.currentSite,
                ParentTrackingKey.KEY_BUSINESS_UNIT to thanksPageData.businessUnit
        )
    }

    private fun getEnhancedECommerceNode(orderedItem: ShopOrder): Map<String, Any> {
        return mapOf(ECommerceNodeTrackingKey.KEY_CURRENCY_CODE to IDR,
                ECommerceNodeTrackingKey.KEY_PURCHASE to getPurchasedEventNodeByShop(orderedItem))
    }

    private fun getPurchasedEventNodeByShop(orderedItem: ShopOrder): Map<String, Any> {
        return mapOf(PurchaseNodeTrackingKey.KEY_ACTION_FIELD to getActionFieldNode(orderedItem),
                PurchaseNodeTrackingKey.KEY_PRODUCTS to getECommerceProductNodeList(orderedItem.purchaseItemList))
    }

    private fun getActionFieldNode(orderedItem: ShopOrder): Map<String, Any?> {
        return mapOf(
                ParentTrackingKey.KEY_ID to orderedItem.orderId,
                ActionFieldNodeTrackingKey.KEY_AFFILIATION to orderedItem.storeName,
                ActionFieldNodeTrackingKey.KEY_REVENUE to thanksPageData.additionalInfo.revenue.toString(),
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
            productNodeMap[ParentTrackingKey.AF_KEY_CATEGORY_NAME] = addSlashInCategory(item.category)
            productNodeMap[ProductNodeTrackingKey.KEY_VARIANT] = item.variant
            productNodeMap[ParentTrackingKey.KEY_QTY] = item.quantity
            productNodeMap[ProductNodeTrackingKey.KEY_DIMENSION83] = item.bebasOngkirDimension
            productNodeList.add(productNodeMap)
        }
        return productNodeList
    }

    fun sendBackPressedEvent() {
        analyticTracker.sendGeneralEvent(
                TrackAppUtils.gtmData(EVENT_NAME_CLICK_ORDER,
                        EVENT_CATEGORY_ORDER_COMPLETE,
                        EVENT_ACTION_CLICK_BACK,
                        ""
                ))
    }

    fun sendLihatDetailClickEvent(pageType: PageType?) {
        val eventLabel = when (pageType) {
            is InstantPaymentPage -> EVENT_LABEL_INSTANT
            is ProcessingPaymentPage -> EVENT_LABEL_PROCESSING
            is WaitingPaymentPage -> EVENT_LABEL_DEFERRED
            else -> ""
        }
        analyticTracker.sendGeneralEvent(
                TrackAppUtils.gtmData(EVENT_NAME_CLICK_ORDER,
                        EVENT_CATEGORY_ORDER_COMPLETE,
                        EVENT_ACTION_LIHAT_DETAIL,
                        eventLabel
                ))
    }

    fun sendCheckTransactionListEvent() {
        analyticTracker.sendGeneralEvent(
                TrackAppUtils.gtmData(EVENT_NAME_CLICK_ORDER,
                        EVENT_CATEGORY_ORDER_COMPLETE,
                        EVENT_ACTION_CHECK_TRANSACTION_LIST,
                        ""
                ))
    }

    fun sendBelanjaLagiClickEvent() {
        analyticTracker.sendGeneralEvent(
                TrackAppUtils.gtmData(EVENT_NAME_CLICK_ORDER,
                        EVENT_CATEGORY_ORDER_COMPLETE,
                        EVENT_ACTION_BELANJA_LAGI,
                        ""
                ))
    }

    fun sendSalinButtonClickEvent(paymentMethod: String) {
        analyticTracker.sendGeneralEvent(
                TrackAppUtils.gtmData(EVENT_NAME_CLICK_ORDER,
                        EVENT_CATEGORY_ORDER_COMPLETE,
                        EVENT_ACTION_SALIN_CLICK,
                        paymentMethod
                ))
    }

    fun sendOnHowtoPayClickEvent() {
        analyticTracker.sendGeneralEvent(
                TrackAppUtils.gtmData(EVENT_NAME_CLICK_ORDER,
                        EVENT_CATEGORY_ORDER_COMPLETE,
                        EVENT_ACTION_LIHAT_CARA_PEMBARYAN_CLICK,
                        ""
                ))
    }

    fun sendPushGtmFalseEvent(paymentId:String) {
        analyticTracker.sendGeneralEvent(
                TrackAppUtils.gtmData(EVENT_NAME_CLICK_ORDER,
                        EVENT_CATEGORY_ORDER_COMPLETE,
                        EVENT_ACTION_PUSH_GTM_FALSE,
                        paymentId
                ))
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
                afValue[ParentTrackingKey.AF_PURCHASE_SITE] = when(ThankPageTypeMapper.getThankPageType(thanksPageData)){
                    MarketPlaceThankPage -> MARKET_PLACE
                    else -> DIGITAL
                }
                afValue[AFInAppEventParameterName.CURRENCY] = ParentTrackingKey.VALUE_IDR
                afValue[ParentTrackingKey.AF_VALUE_PRODUCTTYPE] = productList
                afValue[ParentTrackingKey.AF_KEY_CATEGORY_NAME] = productCategory
                afValue[AFInAppEventParameterName.CONTENT_TYPE] = ParentTrackingKey.AF_VALUE_PRODUCT_TYPE

                val criteoAfValue: Map<String, Any> = java.util.HashMap(afValue)
                if (productArray.length() > 0) {
                    val afContent: String = productArray.toString()
                    afValue[AFInAppEventParameterName.CONTENT] = afContent
                }
                TrackApp.getInstance().appsFlyer.sendTrackEvent(AFInAppEventType.PURCHASE, afValue)
                TrackApp.getInstance().appsFlyer.sendTrackEvent(ParentTrackingKey.AF_KEY_CRITEO, criteoAfValue)
            }
        }, onError = { it.printStackTrace() })
    }

    fun sendBranchIOEvent(thanksPageData: ThanksPageData) {
        CoroutineScope(mainDispatcher).launchCatchError(block = {
            withContext(bgDispatcher) {
                thanksPageData.shopOrder.forEach { shopOrder ->
                    val linkerCommerceData = LinkerCommerceData()
                    val userSession = UserSession(LinkerManager.getInstance().context)
                    val userData: com.tokopedia.linker.model.UserData = com.tokopedia.linker.model.UserData()
                    userData.userId = userSession.userId
                    userData.phoneNumber = userSession.phoneNumber
                    userData.name = userSession.name
                    userData.email = userSession.email
                    linkerCommerceData.userData = userData
                    val branchIOPayment: com.tokopedia.linker.model.PaymentData = com.tokopedia.linker.model.PaymentData()
                    branchIOPayment.setPaymentId(thanksPageData.paymentID.toString())
                    branchIOPayment.setOrderId(shopOrder.orderId)
                    branchIOPayment.setShipping(shopOrder.shippingAmount.toString())
                    branchIOPayment.setRevenue(thanksPageData.amount.toString())
                    branchIOPayment.setProductType(when (ThankPageTypeMapper.getThankPageType(thanksPageData)) {
                        DigitalThankPage -> LinkerConstants.PRODUCTTYPE_DIGITAL
                        else -> LinkerConstants.PRODUCTTYPE_MARKETPLACE
                    })
                    branchIOPayment.isNewBuyer = thanksPageData.isNewUser
                    branchIOPayment.isMonthlyNewBuyer = thanksPageData.isMonthlyNewUser
                    var price = 0F
                    shopOrder.purchaseItemList.forEach { productItem ->
                        val product = HashMap<String, String>()
                        product[LinkerConstants.ID] = productItem.productId
                        product[LinkerConstants.NAME] = productItem.productName
                        price += productItem.price
                        product[LinkerConstants.PRICE] = productItem.price.toString()
                        product[LinkerConstants.PRICE_IDR_TO_DOUBLE] = productItem.price.toString()
                        product[LinkerConstants.QTY] = productItem.quantity.toString()
                        product[LinkerConstants.CATEGORY] = getCategoryLevel1(productItem.category)
                        branchIOPayment.setProduct(product)
                    }
                    branchIOPayment.setItemPrice(price.toString())
                    linkerCommerceData.paymentData = branchIOPayment
                    LinkerManager.getInstance()
                            .sendEvent(LinkerUtils.createGenericRequest(LinkerConstants.EVENT_COMMERCE_VAL,
                                    linkerCommerceData))

                }
            }
        }, onError = { it.printStackTrace() })
    }

    private fun getCategoryLevel1(category: String?): String {
        return if (category.isNullOrBlank()) {
            ""
        } else {
            category.split("_")[0]
        }
    }

    private fun addSlashInCategory(category: String?): String {
        return if (category.isNullOrBlank()) {
            ""
        } else {
            category.replace("_", " / ")
        }
    }

    companion object {
        const val EVENT_NAME_CLICK_ORDER = "clickOrder"
        const val EVENT_CATEGORY_ORDER_COMPLETE = "order complete"
        const val EVENT_ACTION_CLICK_BACK = "click back arrow"

        const val EVENT_ACTION_LIHAT_DETAIL = "click lihat detail tagihan"
        const val EVENT_ACTION_CHECK_TRANSACTION_LIST = "click check transactions list"
        const val EVENT_ACTION_BELANJA_LAGI = "click check transactions list"
        const val EVENT_ACTION_SALIN_CLICK = "click check transactions list"
        const val EVENT_ACTION_LIHAT_CARA_PEMBARYAN_CLICK = "click lihat cara pembayaran"


        const val EVENT_LABEL_INSTANT = "instant"
        const val EVENT_LABEL_DEFERRED = "deffer"
        const val EVENT_LABEL_PROCESSING = "processing"

        const val EVENT_ACTION_PUSH_GTM_FALSE = "push false gtm"
    }
}


object ParentTrackingKey {
    val KEY_EVENT = "event"
    val KEY_EVENT_CATEGORY = "eventCategory"
    val KEY_EVENT_ACTION = "eventAction"
    val KEY_EVENT_LABEL = "eventLabel"
    val KEY_PAYMENT_ID = "payment_id"
    val KEY_PAYMENT_STATUS = "payment_status"
    val KEY_PAYMENT_TYPE = "payment_type"

    val KEY_SHOP_ID = "shopId"
    val KEY_SHOP_TYPE = "shopType"
    val KEY_LOGISTIC_TYPE = "logistic_type"
    val KEY_ECOMMERCE = "ecommerce"
    val KEY_CURRENT_SITE = "currentSite"
    val KEY_BUSINESS_UNIT = "businessUnit"
    const val KEY_ID = "id"
    const val KEY_QTY = "quantity"
    const val AF_SHIPPING_PRICE = "af_shipping_price"
    const val AF_PURCHASE_SITE = "af_purchase_site"
    const val AF_VALUE_PRODUCTTYPE = "product"
    const val AF_VALUE_PRODUCT_TYPE = "productType"
    const val VALUE_IDR = "IDR"
    const val AF_KEY_CATEGORY_NAME = "category"
    const val AF_KEY_CRITEO = "criteo_track_transaction"


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