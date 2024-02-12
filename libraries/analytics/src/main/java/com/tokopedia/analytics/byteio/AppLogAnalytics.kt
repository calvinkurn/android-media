package com.tokopedia.analytics.byteio

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.util.Log
import com.bytedance.applog.AppLog
import com.bytedance.applog.util.EventsSenderUtils
import com.tokopedia.analyticsdebugger.cassava.Cassava
import org.json.JSONObject
import java.lang.ref.WeakReference


data class TrackProduct(
    val productId: String,
    val isAd: Boolean,
    val orderFrom1: Int,
    val requestId: String? = ""
)

data class TrackProductDetail(
    val productId: String,
    val productCategory: String,
//    val entrance_info: TODO
    val productType: ProductType,
    val originalPrice: String,
    val salePrice: String,
)

data class TrackStayProductDetail(
    val productId: String,
    val productCategory: String,
//    val entrance_info: TODO
    val productType: ProductType,
    val originalPrice: String,
    val salePrice: String,
    val isLoadData: Boolean,
    val isSkuSelected: Boolean,
    val isAddCartSelected: Boolean,
)

data class TrackConfirmSku(
    val productId: String,
    val productCategory: String,
//    val entrance_info: TODO
    val productType: ProductType,
    val originalPrice: String,
    val salePrice: String,
    val skuId: String,
    val currency: String,
    val isSkuSelected: Boolean,
    val isAddCartSelected: Boolean,
    val qty: String,
    val isHaveAddress: Boolean,
)

data class TrackConfirmCart(
    val productId: String,
    val productCategory: String,
//    val entrance_info: TODO
    val productType: ProductType,
    val originalPrice: String,
    val salePrice: String,
    val skuId: String,
    val currency: String,
    val addSkuNum: Int,
    val skuNumBefore: Int,
    val skuNumAfter: Int
)

data class TrackConfirmCartResult(
    val productId: String,
    val productCategory: String,
//    val entrance_info: TODO
    val productType: ProductType,
    val originalPrice: String,
    val salePrice: String,
    val skuId: String,
    val currency: String,
    val addSkuNum: Int,
    val skuNumBefore: Int,
    val skuNumAfter: Int,
    val isSuccess: String,
    val failReason: String
)


enum class ProductType(val type: Int) {
    AVAILABLE(1),
    SOLD_OUT(2),
    NOT_AVAILABLE(3),
    LIVE_REGION_NOT_AVAILABLE(4),
    NON_LIVE_REGION_NOT_AVAILABLE_OR_REMOVED(5),
}

object PageName {
    const val MAINPAGE = "MainPage"
    const val PDP = "PDP"
}

object QuitType {
    const val RETURN = "return"
    const val NEXT = "next"
    const val CLOSE = "close"
}


enum class EntranceForm(val str: String) {
    HORIZONTAL_GOODS_CARD("horizontal_goods_card"),
    GRID_GOODS_CARD("grid_goods_card")
}

enum class SourcePageType(val str: String) {
    HOME_FOR_YOU("mainPage_foryou")
}

object EventName {
    const val PRODUCT_SHOW = "tiktokec_product_show"
    const val PRODUCT_CLICK = "tiktokec_product_click"
    const val ENTER_PRODUCT_DETAIL = "tiktokec_enter_product_detail"
    const val STAY_PRODUCT_DETAIL = "tiktokec_stay_product_detail"
    const val CONFIRM_SKU = "tiktokec_confirm_sku"
    const val CONFIRM_CART = "tiktokec_confirm_cart"
    const val CONFIRM_CART_RESULT = "tiktokec_confirm_cart_result"
}

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

    private val lock = Any()

    fun addPageName(activity: Activity) {
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

    fun removePageName(activity: Activity) {
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
            it.put("sku_num_before", product.skuNumBefore)
            it.put("sku_num_after", product.skuNumAfter)
            it.put("is_success", product.isSuccess)
            it.put("fail_reason", product.failReason)
            it.put("request_id", globalRequestId)
            it.put("track_id", globalTrackId)
        })
    }

    private fun JSONObject.addPage() {
        put("previous_page", previousPageName())
        put("page_name", currentPageName())
        put("source_page_type", sourcePageType)
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
            it.put("product_id", product)
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
    }

    @JvmStatic
    fun init(application: Application) {
        initAppLog(application.applicationContext)
        EventsSenderUtils.setEventsSenderEnable("573733", true, application)
        EventsSenderUtils.setEventVerifyHost("573733", "https://log.byteoversea.net")
        Log.d("BYTEIO", "applog dId: ${AppLog.getDid()}")
    }

}
