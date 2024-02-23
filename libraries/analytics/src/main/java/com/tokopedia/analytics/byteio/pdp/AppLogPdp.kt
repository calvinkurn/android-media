package com.tokopedia.analytics.byteio.pdp

import android.annotation.SuppressLint
import com.tokopedia.analytics.byteio.AppLogAnalytics
import com.tokopedia.analytics.byteio.AppLogAnalytics.addEntranceForm
import com.tokopedia.analytics.byteio.AppLogAnalytics.addPage
import com.tokopedia.analytics.byteio.AppLogAnalytics.addSourceModule
import com.tokopedia.analytics.byteio.AppLogAnalytics.addSourcePageType
import com.tokopedia.analytics.byteio.AppLogParam
import com.tokopedia.analytics.byteio.CartClickAnalyticsModel
import com.tokopedia.analytics.byteio.EventName
import com.tokopedia.analytics.byteio.PageName
import com.tokopedia.analytics.byteio.SubmitOrderResult
import com.tokopedia.analytics.byteio.TrackConfirmCart
import com.tokopedia.analytics.byteio.TrackConfirmCartResult
import com.tokopedia.analytics.byteio.TrackConfirmSku
import com.tokopedia.analytics.byteio.TrackProductDetail
import com.tokopedia.analytics.byteio.TrackStayProductDetail
import org.json.JSONObject

object AppLogPdp {

    fun sendPDPEnterPage(product: TrackProductDetail?) {
        if (AppLogAnalytics.sourcePageType == null || product == null) {
            return
        }
        // TODO check if track id exist

        AppLogAnalytics.send(EventName.ENTER_PRODUCT_DETAIL, JSONObject().also {
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
            it.put("track_id", AppLogAnalytics.globalTrackId)
        })
    }

    internal fun sendStayProductDetail(
        durationInMs: Long,
        product: TrackStayProductDetail,
        quitType: String
    ) {
        if (AppLogAnalytics.sourcePageType == null) {
            return
        }
        AppLogAnalytics.send(EventName.STAY_PRODUCT_DETAIL, JSONObject().also {
            it.put(AppLogParam.PREVIOUS_PAGE, AppLogAnalytics.previousPageName(2))
            it.put(AppLogParam.PAGE_NAME, PageName.PDP)
            it.addSourcePageType()
            it.addEntranceForm()
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
            it.put("track_id", AppLogAnalytics.globalTrackId)
            it.put("is_single_sku", if (product.isSingleSku) 1 else 0)
            it.put("is_sku_selected", product.isSkuSelected)
            it.put("is_add_cart", product.isAddCartSelected)
        })
    }

    @SuppressLint("PII Data Exposure")
    fun sendConfirmSku(product: TrackConfirmSku) {
        AppLogAnalytics.send(EventName.CONFIRM_SKU, JSONObject().also {
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
            it.put("request_id", AppLogAnalytics.globalRequestId)
            it.put("track_id", AppLogAnalytics.globalTrackId)
        })
    }

    fun sendConfirmCart(product: TrackConfirmCart) {
        AppLogAnalytics.send(EventName.CONFIRM_CART, JSONObject().also {
            it.addPage()
            it.addEntranceForm()
            it.addSourcePageType()
            it.put("product_id", product.productId)
            it.put("product_category", product.productCategory)
//            it.put("entrance_info", ) TODO
            it.put("product_type", product.productType.type)
            it.put("original_price_value", product.originalPrice)
            it.put("sale_price_value", product.salePrice)
            it.put("button_type", product.buttonType)
            it.put("sku_id", product.skuId)
            it.put("currency", product.currency)
            it.put("add_sku_num", product.addSkuNum)
//            it.put("sku_num_before", product.skuNumBefore)
//            it.put("sku_num_after", product.skuNumAfter)
            it.put("request_id", AppLogAnalytics.globalRequestId)
            it.put("track_id", AppLogAnalytics.globalTrackId)
        })
    }

    fun sendConfirmCartResult(product: TrackConfirmCartResult) {
        AppLogAnalytics.send(EventName.CONFIRM_CART_RESULT, JSONObject().also {
            it.addPage()
            it.addEntranceForm()
            it.addSourcePageType()
            it.put("product_id", product.productId)
            it.put("product_category", product.productCategory)
//            it.put("entrance_info", ) TODO
            it.put("product_type", product.productType.type)
            it.put("original_price_value", product.originalPrice)
            it.put("sale_price_value", product.salePrice)
            it.put("button_type", product.buttonType)
            it.put("sku_id", product.skuId)
            it.put("currency", product.currency)
            it.put("add_sku_num", product.addSkuNum)
//            it.put("sku_num_before", product.skuNumBefore)
//            it.put("sku_num_after", product.skuNumAfter)
            it.put("cart_item_id", product.cartItemId)
            it.put("is_success", if (product.isSuccess == true) 1 else 0)
            it.put("fail_reason", product.failReason)
            it.put("request_id", AppLogAnalytics.globalRequestId)
            it.put("track_id", AppLogAnalytics.globalTrackId)
        })
    }

    fun sendCartEnterPage(cartCount: Int, cartUnavailCount: Int) {
        AppLogAnalytics.send(EventName.ENTER_PAGE, JSONObject().also {
            it.addPage()
            it.put("cart_item_cnt", cartCount)
            it.put("cart_unavailable_cnt", cartUnavailCount)
        })
    }

    fun sendCartButtonClick(model: CartClickAnalyticsModel) {
        AppLogAnalytics.send(EventName.BUTTON_CLICK, JSONObject().also {
            it.addPage()
            it.addEntranceForm()
            it.put("button_name", model.buttonName)
            it.put("cart_item_id", model.cartItemId)
            it.put("original_price_value", model.originalPriceValue)
            it.put("sale_price_value", model.salePriceValue)
            it.put("discounted_amount", model.discountedAmount)
            it.put("currency", "IDR")
            it.put("item_cnt", model.ItemCnt)
            it.put("sku_num", model.skuNum)
            it.put("product_id", model.productId)
            it.put("sku_id", model.skuId)
        })
    }

    fun sendSubmitOrderResult(model: SubmitOrderResult) {
        AppLogAnalytics.send(EventName.SUBMIT_ORDER_RESULT, JSONObject().also {
            it.addPage()
            it.addEntranceForm()
            it.addSourcePageType()
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

}
