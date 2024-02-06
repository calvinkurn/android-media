package com.tokopedia.analytics.byteio

import android.app.Activity
import android.app.Application
import android.content.Context
import com.bytedance.applog.AppLog
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


enum class ProductType(val type: Int) {
    AVAILABLE(1),
    SOLD_OUT(2),
    NOT_AVAILABLE(3),
    LIVE_REGION_NOT_AVAILABLE(4),
    NON_LIVE_REGION_NOT_AVAILABLE_OR_REMOVED(5),
}


enum class EntranceForm(val str: String) {
    HORIZONTAL_GOODS_CARD("horizontal_goods_card"),
    GRID_GOODS_CARD("grid_goods_card")
}

enum class SourcePageType(val str: String) {
    HOME_FOR_YOU("mainPage_foryou")
}

enum class EventName(val str: String) {
    PRODUCT_SHOW("tiktokec_product_show"),
    PRODUCT_CLICK("tiktokec_product_click"),
    ENTER_PRODUCT_DETAIL("tiktokec_enter_product_detail"),
    STAY_PRODUCT_DETAIL("tiktokec_stay_product_detail"),
}

object AppLogAnalytics {

    @JvmField
    var currentActivityReference: WeakReference<Activity>? = null

    @JvmField
    var pageNames = mutableListOf<String?>()

    // TODO check how to make this null again
    @JvmField
    var sourcePageType: SourcePageType? = null

    // TODO check how to make this null again
    @JvmField
    var globalTrackId: String? = null

    @JvmField
    var startTime = 0L

    fun sendEnterPage(product: TrackProductDetail) {
        if (sourcePageType == null) {
            return
        }
        send(EventName.ENTER_PRODUCT_DETAIL.str, JSONObject().also {
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
        if (currentPage() == null) {
            return
        }
        send(EventName.PRODUCT_SHOW.str, JSONObject().also {
            it.addPage()
            it.put("product_id", product)
            it.put("is_ad", if (product.isAd) "1" else "0")
            val reqId = product.requestId ?: ""
            it.put("request_id", reqId)
            it.put("track_id", reqId + "_" + product.requestId + product.orderFrom1)
        })
    }

    private fun JSONObject.addPage() {
        put("previous_page", pageNames.getOrNull(pageNames.size - 2) ?: "")
        put("page_name", currentPage())
        put("source_page_type", sourcePageType)
        put("entrance_form", EntranceForm.GRID_GOODS_CARD.str)
    }

    private fun currentPage() = pageNames.last()

    fun sendClickProduct(inputPageType: SourcePageType?, product: TrackProduct) {
        if (sourcePageType == null && inputPageType != null) {
            sourcePageType = inputPageType
            //TODO Deeplink activity juga
        }
        if (sourcePageType == null) {
            return
        }
        send(EventName.PRODUCT_CLICK.str, JSONObject().also {
            it.addPage()
            it.put("product_id", product)
            it.put("is_ad", if (product.isAd) "1" else "0")
            val reqId = product.requestId ?: ""
            it.put("request_id", reqId)
            val trackId = reqId + "_" + product.requestId + product.orderFrom1
            it.put("track_id", trackId)
            globalTrackId = trackId
        })
    }

    fun sendStay(product: TrackStayProductDetail) {
        if (sourcePageType == null) {
            return
        }
        send(EventName.STAY_PRODUCT_DETAIL.str, JSONObject().also {
            it.addPage()
            it.put("stay_time", System.currentTimeMillis() - startTime)
            it.put("is_load_data", if (product.isLoadData) 1 else 0)
            it.put("quit_type", /*TODO*/ 1)
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
    }

}
