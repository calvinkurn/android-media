package com.tokopedia.analytics.byteio

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.util.Log
import com.bytedance.applog.AppLog
import com.bytedance.applog.util.EventsSenderUtils
import com.bytedance.frameworks.baselib.network.http.cronet.impl.TTNetDetectInfo
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
    var sourcePageType: SourcePageType? = SourcePageType.TESTAPP_SOURCE // TODO: this is TEMPORARY

    // TODO check how to make this null again
    @JvmField
    var globalTrackId: String? = null

    // TODO check how to make this null again
    @JvmField
    var globalRequestId: String? = null

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

    fun sendEnterPage(product: TrackProductDetail) {
        if (sourcePageType == null) {
            return
        }
        // TODO check if track id exist

        send(EventName.ENTER_PRODUCT_DETAIL, JSONObject().also {
            it.addPage()
            it.put("product_id", product.productId)
            it.put("product_category", product.productCategory)
//            it.put("entrance_info", ) TODO
            it.put("product_type", product.productType.type)
            it.put("original_price", product.originalPrice)
            it.put("sale_price", product.salePrice)
            it.put("track_id", globalTrackId)
        })
    }

    fun sendProductImpression(product: TrackProduct) {
        send(EventName.PRODUCT_SHOW, JSONObject().also {
            it.addPage()
            it.put("product_id", product)
            it.put("is_ad", if (product.isAd) "1" else "0")
            val reqId = product.requestId ?: ""
            it.put("request_id", reqId)
            it.put("track_id", reqId + "_" + product.requestId + product.orderFrom1)
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
            it.put("product_id", product.productId)
            it.put("product_category", product.productCategory)
//            it.put("entrance_info", ) TODO
            it.put("product_type", product.productType.type)
            it.put("original_price_value", product.originalPrice)
            it.put("sale_price_value", product.salePrice)
            it.put("sku_id", product.skuId)
            it.put("currency", product.currency)
            it.put("add_sku_num", product.addSkuNum)
            it.put("sku_num_before", product.skuNumBefore)
            it.put("sku_num_after", product.skuNumAfter)
            it.put("request_id", globalRequestId)
            it.put("track_id", globalTrackId)
        })
    }

    fun sendConfirmCartResult(product: TrackConfirmCartResult) {
        send(EventName.CONFIRM_CART_RESULT, JSONObject().also {
            it.addPage()
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
            it.put("is_success", product.isSuccess)
            it.put("fail_reason", product.failReason)
            it.put("request_id", globalRequestId)
            it.put("track_id", globalTrackId)
        })
    }

    private fun JSONObject.addPage() {
        put("previous_page", previousPageName())
        put("page_name", currentPageName())
        put("source_page_type", sourcePageType?.str)
        put("entrance_form", EntranceForm.GRID_GOODS_CARD.str)
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

    fun sendClickProduct(inputPageType: SourcePageType?, product: TrackProduct) {
        if (sourcePageType == null && inputPageType != null) {
            sourcePageType = inputPageType
            //TODO Deeplink activity too
        }
        if (sourcePageType == null) {
            return
        }
        send(EventName.PRODUCT_CLICK, JSONObject().also {
            it.addPage()
            it.put("product_id", product)
            it.put("is_ad", if (product.isAd) "1" else "0")
            val reqId = product.requestId ?: ""
            globalRequestId = reqId
            it.put("request_id", reqId)
            val trackId = reqId + "_" + product.requestId + product.orderFrom1
            it.put("track_id", trackId)
            globalTrackId = trackId
        })
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
            it.put("stay_time", durationInMs)
            it.put("is_load_data", if (product.isLoadData) 1 else 0)
            it.put("quit_type", quitType)
            it.put("source_module",/*TODO*/ "")
            it.put("product_id", product.productId)
            it.put("product_category", product.productCategory)
//            it.put("entrance_info", ) TODO
            it.put("product_type", product.productType.type)
            it.put("original_price", product.originalPrice)
            it.put("sale_price", product.salePrice)
            it.put("track_id", globalTrackId)
            it.put("is_sku_selected", product.isSkuSelected)
            it.put("is_add_cart", product.isAddCartSelected)
        })
    }

    fun send(event: String, params: JSONObject) {
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
