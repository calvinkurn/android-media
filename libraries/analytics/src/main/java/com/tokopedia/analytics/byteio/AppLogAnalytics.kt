package com.tokopedia.analytics.byteio

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.util.Log
import com.bytedance.applog.AppLog
import com.bytedance.applog.util.EventsSenderUtils
import com.tokopedia.analytics.byteio.AppLogParam.ENTRANCE_FORM
import com.tokopedia.analytics.byteio.AppLogParam.PAGE_NAME
import com.tokopedia.analytics.byteio.AppLogParam.PREVIOUS_PAGE
import com.tokopedia.analytics.byteio.AppLogParam.SOURCE_MODULE
import com.tokopedia.analytics.byteio.AppLogParam.SOURCE_PAGE_TYPE
import com.tokopedia.analytics.byteio.Constants.EVENT_ORIGIN_FEATURE_KEY
import com.tokopedia.analytics.byteio.Constants.EVENT_ORIGIN_FEATURE_VALUE
import com.tokopedia.analyticsdebugger.cassava.Cassava
import org.json.JSONObject
import java.lang.ref.WeakReference


object AppLogAnalytics {

    @JvmField
    var currentActivityReference: WeakReference<Activity>? = null

    @JvmField
    var currentActivityName: String = ""

    /**
     * key = activity name
     * value = page name.
     * We store activity name to prevent the newly created activity
     * to override value for current page name
     */
    @JvmField
    var pageNames = mutableListOf<Pair<String, String?>>()

    @JvmField
    var activityCount: Int = 0

    // TODO check how to make this null again
    @JvmField
    var sourcePageType: SourcePageType? = null

    // TODO check how to make this null again
    @JvmField
    var globalTrackId: String? = null

    // TODO check how to make this null again
    @JvmField
    var globalRequestId: String? = null

    // TODO check how to make this null again
    @JvmField
    var entranceForm: EntranceForm? = null

    // TODO check how to make this null again
    @JvmField
    var sourceModule: SourceModule? = null

    private val lock = Any()

    internal fun addPageName(activity: Activity) {
        val actName = activity.javaClass.simpleName
        if (activity is IAppLogActivity) {
            synchronized(lock) {
                pageNames.add(actName to activity.getPageName())
            }
        } else {
            synchronized(lock) {
                pageNames.add(actName to null)
            }
        }
    }

    internal fun removePageName(activity: Activity) {
        synchronized(lock) {
            val pageNameToRemove =
                pageNames.findLast { it.first == activity.javaClass.simpleName }
            if (pageNameToRemove != null) {
                pageNames.remove(pageNameToRemove)
            }
        }
    }

    internal val Boolean.intValue
        get() = if (this) 1 else 0

    fun sendEnterPDPPage(product: TrackProductDetail?) {
        if (sourcePageType == null || product == null) {
            return
        }
        // TODO check if track id exist

        send(EventName.ENTER_PRODUCT_DETAIL, JSONObject().also {
            it.addPage()
            it.addEntranceForm()
            it.addSourcePageType()
            it.addSourceModule()
            it.put("product_id", product.productId)
            it.put("product_category", product.productCategory)
//            it.put("entrance_info", ) TODO
            it.put("product_type", product.productType.type)
            it.put("original_price", product.originalPrice)
            it.put("sale_price", product.salePrice)
            it.put("is_single_sku", if (product.isSingleSku) 1 else 0)
            it.put("track_id", globalTrackId)
        })
    }

    @SuppressLint("PII Data Exposure")
    fun sendConfirmSku(product: TrackConfirmSku) {
        send(EventName.CONFIRM_SKU, JSONObject().also {
            it.addPage()
            it.put("product_id", product.productId)
            it.put("product_category", product.productCategory)
//            it.put("entrance_info", ) TODO
            it.put("product_type", product.productType.type)
            it.put("original_price_value", product.originalPrice)
            it.put("sale_price_value", product.salePrice)
            it.put("sku_id", product.skuId)
            it.put("is_single_sku", if (product.isSingleSku) 1 else 0)
            it.put("currency", product.currency)
            it.put("quantity", product.qty)
            it.put("is_have_address", (if (product.isHaveAddress) 1 else 0))
            it.put("request_id", globalRequestId)
            it.put("track_id", globalTrackId)
        })
    }

    fun sendConfirmCart(product: TrackConfirmCart) {
        send(EventName.CONFIRM_CART, JSONObject().also {
            it.addPage()
            it.addEntranceForm()
            it.addSourcePageType()
            it.addSourceModule()
            it.put("product_id", product.productId)
            it.put("product_category", product.productCategory)
//            it.put("entrance_info", ) TODO
            it.put("product_type", product.productType.type)
            it.put("original_price_value", product.originalPrice)
            it.put("sale_price_value", product.salePrice)
            it.put("sku_id", product.skuId)
            it.put("currency", product.currency)
            it.put("add_sku_num", product.addSkuNum)
//            it.put("sku_num_before", product.skuNumBefore)
//            it.put("sku_num_after", product.skuNumAfter)
            it.put("request_id", globalRequestId)
            it.put("track_id", globalTrackId)
        })
    }

    fun sendConfirmCartResult(product: TrackConfirmCartResult) {
        send(EventName.CONFIRM_CART_RESULT, JSONObject().also {
            it.addPage()
            it.addEntranceForm()
            it.addSourcePageType()
            it.addSourceModule()
            it.put("product_id", product.productId)
            it.put("product_category", product.productCategory)
//            it.put("entrance_info", ) TODO
            it.put("product_type", product.productType.type)
            it.put("original_price_value", product.originalPrice)
            it.put("sale_price_value", product.salePrice)
            it.put("sku_id", product.skuId)
            it.put("currency", product.currency)
            it.put("add_sku_num", product.addSkuNum)
            it.put("button_type", product.buttonType)
//            it.put("sku_num_before", product.skuNumBefore)
//            it.put("sku_num_after", product.skuNumAfter)
            it.put("cart_item_id", product.cartItemId)
            it.put("is_success", if (product.isSuccess == true) 1 else 0)
            it.put("fail_reason", product.failReason)
            it.put("request_id", globalRequestId)
            it.put("track_id", globalTrackId)
        })
    }

    fun sendSubmitOrderResult(model: SubmitOrderResult) {
        send(EventName.SUBMIT_ORDER_RESULT, JSONObject().also {
            it.addPage()
            it.addEntranceForm()
            it.addSourcePageType()
            it.addSourceModule()
            it.put("is_success", if (model.isSuccess) 1 else 0)
            it.put("fail_reason", model.failReason)
            it.put("shipping_price", model.shippingPrice)
            it.put("discounted_shipping_price", model.discountedShippingPrice)
            it.put("total_payment", model.totalPayment)
            it.put("discounted_amount", model.discountedAmount)
            it.put("total_tax", model.totalTax)
            it.put("summary_info", model.summaryInfo)
            it.put("currency", model.currency)
            it.put("delivery_info", model.deliveryInfo)
            it.put("pay_type", model.payType)
            it.put("cart_item_id", model.cartItemId)
            it.put("sku_id", model.skuId)
            it.put("order_id", model.orderId)
            it.put("combo_id", model.comboId)
            it.put("product_id", model.productId)
        })
    }

    internal fun JSONObject.addPage() {
        put(PREVIOUS_PAGE, previousPageName())
        put(PAGE_NAME, currentPageName())
    }

    internal fun JSONObject.addEntranceForm() {
        put(ENTRANCE_FORM, entranceForm?.str)
    }

    internal fun JSONObject.addSourcePageType() {
        put(
            SOURCE_PAGE_TYPE,
            if(sourcePageType == SourcePageType.PRODUCT_CARD) previousPageName()
            else sourcePageType?.str
        )
    }

    internal fun JSONObject.addSourceModule() {
        put(SOURCE_MODULE, sourceModule?.str)
    }

    private fun currentPageName(): String {
        return synchronized(lock) {
            pageNames.findLast { it.first == currentActivityName }?.second ?: ""
        }
    }

    private fun previousPageName(): String {
        return synchronized(lock) {
            (pageNames.getOrNull(pageNames.indexOf(pageNames.findLast { it.first == currentActivityName }) - 1)?.second)
                ?: ""
        }
    }

    internal fun sendStayProductDetail(
        durationInMs: Long,
        product: TrackStayProductDetail,
        quitType: String
    ) {
        if (sourcePageType == null) {
            return
        }
        send(EventName.STAY_PRODUCT_DETAIL, JSONObject().also {
            it.addPage()
            it.addEntranceForm()
            it.addSourcePageType()
            it.addSourceModule()
            it.put("stay_time", durationInMs)
            it.put("is_load_data", if (product.isLoadData) 1 else 0)
            it.put("quit_type", quitType)
            it.put("source_module",/*TODO*/ "")
            it.put("product_id", product.productId)
            it.put("is_single_sku", if (product.isSingleSku) 1 else 0)
            it.put("product_category", product.productCategory)
//            it.put("entrance_info", ) TODO
            it.put("main_photo_view_cnt", product.mainPhotoViewCount)
            it.put("sku_photo_view_cnt", product.skuPhotoViewCount)
            it.put("product_type", product.productType.type)
            it.put("original_price", product.originalPrice)
            it.put("sale_price", product.salePrice)
            it.put("track_id", globalTrackId)
            it.put("is_single_sku", if (product.isSingleSku) 1 else 0)
            it.put("is_sku_selected", product.isSkuSelected)
            it.put("is_add_cart", product.isAddCartSelected)
        })
    }

    fun send(event: String, params: JSONObject) {
        params.put(EVENT_ORIGIN_FEATURE_KEY, EVENT_ORIGIN_FEATURE_VALUE)
        Cassava.save(params, event, "ByteIO")
        AppLog.onEventV3(event, params)
        Log.d(TAG, "sending event ($event), value: ${params.toString(2)} ")
    }

    @JvmStatic
    fun init(application: Application) {
        initAppLog(application.applicationContext)
        EventsSenderUtils.setEventsSenderEnable("573733", true, application)
        EventsSenderUtils.setEventVerifyHost("573733", "https://log.byteoversea.net")
        Log.d(TAG, "AppLog dId: ${AppLog.getDid()} userUniqueId: ${AppLog.getUserUniqueID()} userId: ${AppLog.getUserUniqueID()}")
    }

}
