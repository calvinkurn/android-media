package com.tokopedia.analytics.byteio.pdp

import com.tokopedia.analytics.byteio.AppLogAnalytics
import com.tokopedia.analytics.byteio.AppLogAnalytics.addAuthorId
import com.tokopedia.analytics.byteio.AppLogAnalytics.addEnterFrom
import com.tokopedia.analytics.byteio.AppLogAnalytics.addEnterFromInfo
import com.tokopedia.analytics.byteio.AppLogAnalytics.addEnterMethod
import com.tokopedia.analytics.byteio.AppLogAnalytics.addEntranceForm
import com.tokopedia.analytics.byteio.AppLogAnalytics.addEntranceInfo
import com.tokopedia.analytics.byteio.AppLogAnalytics.addEntranceInfoCart
import com.tokopedia.analytics.byteio.AppLogAnalytics.addPage
import com.tokopedia.analytics.byteio.AppLogAnalytics.addRequestId
import com.tokopedia.analytics.byteio.AppLogAnalytics.addSourceModulePdp
import com.tokopedia.analytics.byteio.AppLogAnalytics.addSourcePageType
import com.tokopedia.analytics.byteio.AppLogAnalytics.addSourcePreviousPage
import com.tokopedia.analytics.byteio.AppLogAnalytics.addTrackId
import com.tokopedia.analytics.byteio.AppLogAnalytics.getLastData
import com.tokopedia.analytics.byteio.AppLogAnalytics.getLastDataBeforeCurrent
import com.tokopedia.analytics.byteio.AppLogAnalytics.getLastDataBeforeHash
import com.tokopedia.analytics.byteio.AppLogAnalytics.intValue
import com.tokopedia.analytics.byteio.AppLogParam
import com.tokopedia.analytics.byteio.AppLogParam.ENTER_FROM
import com.tokopedia.analytics.byteio.AppLogParam.PAGE_NAME
import com.tokopedia.analytics.byteio.AppLogParam.PREVIOUS_PAGE
import com.tokopedia.analytics.byteio.AppLogParam.SOURCE_MODULE
import com.tokopedia.analytics.byteio.AppLogParam.SOURCE_PAGE_TYPE
import com.tokopedia.analytics.byteio.AppLogParam.SOURCE_PREVIOUS_PAGE
import com.tokopedia.analytics.byteio.ButtonClickAnalyticData
import com.tokopedia.analytics.byteio.ButtonClickCompletedAnalyticData
import com.tokopedia.analytics.byteio.ButtonShowAnalyticData
import com.tokopedia.analytics.byteio.CartClickAnalyticsModel
import com.tokopedia.analytics.byteio.EventName
import com.tokopedia.analytics.byteio.PageName
import com.tokopedia.analytics.byteio.SubmitOrderResult
import com.tokopedia.analytics.byteio.TAG
import com.tokopedia.analytics.byteio.TrackConfirmCart
import com.tokopedia.analytics.byteio.TrackConfirmCartResult
import com.tokopedia.analytics.byteio.TrackConfirmSku
import com.tokopedia.analytics.byteio.TrackProductDetail
import com.tokopedia.analytics.byteio.TrackStayProductDetail
import org.json.JSONObject
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean

object AppLogPdp {

    val addToCart = AtomicBoolean(false)

    /**
     * Later should be removed, use thanksPageData event label instead
     * */
    private val confirmSku = AtomicBoolean(false)

    fun sendPDPEnterPage(product: TrackProductDetail?) {
        if (product == null) {
            Timber.d("%s Enter PDP was not sent because of product is null", TAG)
            return
        }

        AppLogAnalytics.send(
            EventName.ENTER_PRODUCT_DETAIL,
            JSONObject().also {
                it.put(PAGE_NAME, getLastData(PAGE_NAME))
                it.addEntranceForm()
                it.addSourcePageType()
                it.addSourceModulePdp()
                it.addEntranceInfo()
                it.addEnterFromInfo()
                it.put("product_id", product.productId)
                it.put("product_category", product.productCategory)
                it.put("product_type", product.productType.type)
                it.put("original_price", product.originalPrice)
                it.put("sale_price", product.salePrice)
                it.put("is_single_sku", if (product.isSingleSku) 1 else 0)
                it.addTrackId()
            }
        )
    }

    internal fun sendStayProductDetail(
        durationInMs: Long,
        product: TrackStayProductDetail,
        quitType: String,
        hash: Int
    ) {
        val isAddToCart = product.isAddCartSelected || addToCart.getAndSet(false)
        AppLogAnalytics.send(
            EventName.STAY_PRODUCT_DETAIL,
            JSONObject().also {
                it.put(PREVIOUS_PAGE, getLastDataBeforeHash(PAGE_NAME, hash))
                it.put(PAGE_NAME, PageName.PDP)
                it.addSourcePageType()
                it.addEntranceForm()
                it.addSourceModulePdp()
                it.addTrackId()
                it.addEntranceInfo()
                it.addEnterFromInfo()
                it.put("stay_time", durationInMs)
                it.put("is_load_data", if (product.isLoadData) 1 else 0)
                it.put("quit_type", quitType)
                it.put("product_id", product.productId)
                it.put("product_category", product.productCategory)
                it.put("main_photo_view_cnt", product.mainPhotoViewCount)
                it.put("sku_photo_view_cnt", product.skuPhotoViewCount)
                it.put("product_type", product.productType.type)
                it.put("original_price", product.originalPrice)
                it.put("sale_price", product.salePrice)
                it.put("is_single_sku", if (product.isSingleSku) 1 else 0)
                it.put("is_sku_selected", if (product.isSkuSelected) 1 else 0)
                it.put("is_add_cart", isAddToCart.intValue)
            }
        )
    }

    fun sendConfirmSku(product: TrackConfirmSku) {
        AppLogAnalytics.send(
            EventName.CONFIRM_SKU,
            JSONObject().also {
                it.addPage()
                it.addTrackId()
                it.addRequestId()
                it.addSourcePageType()
                it.addEntranceForm()
                it.addSourceModulePdp()
                it.addEnterFromInfo()
                it.addEntranceInfo()
                it.put("product_id", product.productId)
                it.put("product_category", product.productCategory)
                it.put("product_type", product.productType.type)
                it.put("original_price_value", product.originalPrice)
                it.put("sale_price_value", product.salePrice)
                it.put("sku_id", product.skuId)
                it.put("is_single_sku", if (product.isSingleSku) 1 else 0)
                it.put("currency", product.currency)
                it.put("quantity", product.qty)
//            it.put("is_have_address", (if (product.isHaveAddress) 1 else 0)) // removed
            }
        )
        confirmSku.set(true)
    }

    fun sendConfirmCart(product: TrackConfirmCart) {
        AppLogAnalytics.send(
            EventName.CONFIRM_CART,
            JSONObject().also {
                it.addPage()
                it.addTrackId()
                it.addSourcePageType()
                it.addEntranceForm()
                it.addSourceModulePdp()
                it.addSourcePreviousPage()
                it.addEnterFromInfo()
                it.addRequestId()
                it.addEntranceInfo()
                it.put("product_id", product.productId)
                it.put("product_category", product.productCategory)
                it.put("product_type", product.productType.type)
                it.put("original_price_value", product.originalPrice)
                it.put("sale_price_value", product.salePrice)
                it.put("button_type", product.buttonType)
                it.put("sku_id", product.skuId)
                it.put("currency", product.currency)
                it.put("add_sku_num", product.addSkuNum)
//            it.put("sku_num_before", product.skuNumBefore)
//            it.put("sku_num_after", product.skuNumAfter)
            }
        )
    }

    fun sendConfirmCartResult(product: TrackConfirmCartResult) {
        AppLogAnalytics.send(
            EventName.CONFIRM_CART_RESULT,
            JSONObject().also {
                it.addPage()
                it.addTrackId()
                it.addSourcePageType()
                it.addEntranceForm()
                it.addSourceModulePdp()
                it.addEnterFromInfo()
                it.addSourcePreviousPage()
                it.addRequestId()
                it.addEntranceInfo()
                it.put("product_id", product.productId)
                it.put("product_category", product.productCategory)
                it.put("product_type", product.productType.type)
                it.put("original_price_value", product.originalPrice)
                it.put("sale_price_value", product.salePrice)
                it.put("button_type", product.buttonType)
                it.put("sku_id", product.skuId)
                it.put("currency", product.currency)
                it.put("add_sku_num", product.addSkuNum)
                /**
                 * Not sending these because it requires BE, not at the current priority
                 *
                 * it.put("sku_num_before", product.skuNumBefore)
                 * it.put("sku_num_after", product.skuNumAfter)
                 * */
                it.put("cart_item_id", product.cartItemId)
                it.put("is_success", if (product.isSuccess == true) 1 else 0)
                it.put("fail_reason", product.failReason)
            }
        )
    }

    fun sendCartEnterPage(cartCount: Int, cartUnavailCount: Int) {
        /**
         * Setting global param source previous page whenever enter the Cart Page, the value is
         * the previous page of the current Cart Page
         * */
        AppLogAnalytics.putPageData(
            SOURCE_PREVIOUS_PAGE,
            getLastDataBeforeCurrent(PAGE_NAME).toString()
        )
        AppLogAnalytics.send(EventName.ENTER_PAGE, JSONObject().also {
            it.addPage()
            it.put(ENTER_FROM, getLastDataBeforeCurrent(ENTER_FROM))
            it.addSourcePreviousPage()
            it.addEnterMethod()
            it.addEntranceInfoCart()
            it.put("cart_item_cnt", cartCount)
            it.put("cart_unavailable_cnt", cartUnavailCount)
        })
    }

    fun sendCartButtonClick(model: CartClickAnalyticsModel) {
        AppLogAnalytics.send(
            EventName.BUTTON_CLICK,
            JSONObject().also {
                it.addPage()
                it.addSourcePreviousPage()
                it.addEnterFrom()
                it.addEntranceForm()
                it.addEntranceInfoCart()
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
            }
        )
    }

    //region https://bytedance.sg.larkoffice.com/sheets/YVaGsNyMfhqbjzt7HJvlH4FIgof
    fun sendButtonShow(analyticData: ButtonShowAnalyticData) {
        AppLogAnalytics.send(
            EventName.PDP_BUTTON_SHOW,
            JSONObject().apply {
                addPage()
                addEntranceInfo()
                addEnterFromInfo()
                addEnterFrom()
                put("button_name", analyticData.buttonName)
                put("product_id", analyticData.productId)
                put("is_single_sku", analyticData.isSingleSku.intValue)
                put("buy_type", analyticData.buyType.code)
                put("shop_id", analyticData.shopId)
            }
        )
    }

    fun sendButtonClick(analyticData: ButtonClickAnalyticData) {
        AppLogAnalytics.send(
            EventName.PDP_BUTTON_CLICK,
            JSONObject().apply {
                addPage()
                addEntranceInfo()
                addEnterFromInfo()
                addEnterFrom()
                put("button_name", analyticData.buttonName)
                put("product_id", analyticData.productId)
                put("is_single_sku", analyticData.isSingleSku.intValue)
                put("buy_type", analyticData.buyType.code)
                put("shop_id", analyticData.shopId)
            }
        )
    }

    fun sendButtonConfirmSku(analyticData: ButtonClickCompletedAnalyticData) {
        AppLogAnalytics.send(
            EventName.PDP_BUTTON_CLICK_COMPLETED,
            JSONObject().apply {
                addPage()
                addEntranceForm()
                addTrackId()
                addSourcePageType()
                addEnterFromInfo()
                addEntranceInfo()
                addEnterFrom()
                addAuthorId()
                put("product_id", analyticData.productId)
                put("is_single_sku", analyticData.isSingleSku.intValue)
                put("sku_id", analyticData.skuId)
                put("quantity", analyticData.quantity)
                put("product_type", analyticData.productType.type)
                put("original_price_value", analyticData.originalPrice)
                put("sale_price_value", analyticData.salePrice)
                put("follow_status", analyticData.followStatus.value)
                put("buy_type", analyticData.buyType.code)
                put("cart_id", analyticData.cartId)
                put("shop_id", analyticData.shopId)
            }
        )
    }
    //endregion

    fun sendSubmitOrderResult(buyType: String, model: SubmitOrderResult) {
        AppLogAnalytics.send(
            EventName.SUBMIT_ORDER_RESULT,
            JSONObject().also {
                if (buyType == ThanksDataEventLabel.REGULAR) {
                    it.addEntranceInfoCart()
                    it.put(SOURCE_PAGE_TYPE, PageName.CART)
                } else {
                    it.addTrackId()
                    it.put(SOURCE_MODULE, AppLogAnalytics.getPreviousDataOfProduct(SOURCE_MODULE))
                    it.addEntranceForm()
                    it.put(
                        AppLogParam.ENTRANCE_INFO,
                        AppLogAnalytics.generateEntranceInfoJson().toString()
                    )
                    it.addSourcePageType()
                }
                it.addPage()
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
//            it.put("sku_id", model.skuId) // removed
                it.put("order_id", model.orderId)
                it.put("combo_id", model.comboId)
                it.put("product_id", model.productId)
            }
        )
    }
}
