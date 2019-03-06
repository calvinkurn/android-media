package com.tokopedia.product.detail.data.util

import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.topads.sdk.domain.model.Product
import com.tokopedia.track.TrackApp

class ProductDetailTracking(private val analyticTracker: AnalyticTracker?){

    fun eventTalkClicked(){
        analyticTracker?.sendEventTracking(ProductTrackingConstant.PDP.EVENT,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.CLICK,
                ProductTrackingConstant.ProductTalk.EVENT_LABEL)
    }

    fun eventReviewClicked(){
        analyticTracker?.sendEventTracking(ProductTrackingConstant.PDP.EVENT,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.CLICK,
                ProductTrackingConstant.ProductReview.EVENT_LABEL)
    }

    fun eventReportLogin(){
        analyticTracker?.sendEventTracking(ProductTrackingConstant.Report.EVENT,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.CLICK,
                ProductTrackingConstant.Report.EVENT_LABEL)
    }

    fun eventReportNoLogin(){
        analyticTracker?.sendEventTracking(ProductTrackingConstant.Report.EVENT,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.CLICK,
                ProductTrackingConstant.Report.NOT_LOGIN_EVENT_LABEL)
    }

    fun eventCartMenuClicked(variant: String){
        analyticTracker?.sendEventTracking(ProductTrackingConstant.PDP.EVENT,
                ProductTrackingConstant.Category.PDP.toLowerCase(),
                ProductTrackingConstant.Action.CLICK_CART_BUTTON_VARIANT,
                variant)
    }

    fun eventClickMerchantVoucherUse(merchantVoucherViewModel: MerchantVoucherViewModel, position: Int) {
        analyticTracker?.sendEventTracking(createEventMVCClick(ProductTrackingConstant.MerchantVoucher.EVENT,
                ProductTrackingConstant.Category.PDP.toLowerCase(),
                listOf(ProductTrackingConstant.MerchantVoucher.ACTION,
                        ProductTrackingConstant.Action.CLICK).joinToString(" "),
                merchantVoucherViewModel, position))
    }

    private fun createEventMVCClick(event: String, category: String, action: String,
                                    merchantVoucherViewModel: MerchantVoucherViewModel, position: Int): Map<String, Any?> {
        return mapOf(KEY_EVENT to event, KEY_CATEGORY to category, KEY_ACTION to action,
                KEY_LABEL to merchantVoucherViewModel.voucherName,
                KEY_ECOMMERCE to DataLayer.mapOf(KEY_PRODUCT_PROMO,
                        DataLayer.mapOf(KEY_PROMOTIONS, createMVCMap(listOf(merchantVoucherViewModel), position))))
    }

    private fun createMVCMap(vouchers: List<MerchantVoucherViewModel>, position: Int): List<Any> {
        val list = vouchers.withIndex().filter { it.value.isAvailable() }.map {
            DataLayer.mapOf(ID, it.value.voucherId,
                    PROMO_NAME, it.value.voucherName,
                    PROMO_POSITION, (position + it.index + 1).toString(),
                    PROMO_ID, it.value.voucherId,
                    PROMO_CODE, it.value.voucherCode)
        }
        if (list.isEmpty()) return list
        return DataLayer.listOf(list.toTypedArray())
    }

    fun eventClickMerchantVoucherSeeDetail(id: Int) {
        analyticTracker?.sendEventTracking(ProductTrackingConstant.PDP.EVENT,
                ProductTrackingConstant.Category.PDP.toLowerCase(),
                listOf(ProductTrackingConstant.Action.CLICK, ProductTrackingConstant.MerchantVoucher.MERCHANT_VOUCHER,
                        ProductTrackingConstant.MerchantVoucher.DETAIL).joinToString(" - "),
                id.toString())
    }

    fun eventClickMerchantVoucherSeeAll(id: Int) {
        analyticTracker?.sendEventTracking(ProductTrackingConstant.PDP.EVENT,
                ProductTrackingConstant.Category.PDP.toLowerCase(),
                listOf(ProductTrackingConstant.Action.CLICK, ProductTrackingConstant.MerchantVoucher.MERCHANT_VOUCHER,
                        ProductTrackingConstant.MerchantVoucher.SEE_ALL).joinToString(" - "),
                id.toString())
    }

    fun eventTopAdsClicked(product: Product, position: Int) {
        analyticTracker?.sendEnhancedEcommerce(
                DataLayer.mapOf(KEY_EVENT, ProductTrackingConstant.Action.PRODUCT_CLICK,
                        KEY_CATEGORY, ProductTrackingConstant.Category.PDP.toLowerCase(),
                        KEY_ACTION, ProductTrackingConstant.Action.TOPADS_CLICK,
                        KEY_LABEL, "",
                        KEY_ECOMMERCE, DataLayer.mapOf(ProductTrackingConstant.Action.CLICK,
                            DataLayer.mapOf(ACTION_FIELD, DataLayer.mapOf(LIST, ProductTrackingConstant.TopAds.PDP_TOPADS),
                                    PRODUCTS, DataLayer.listOf(
                                    DataLayer.mapOf(PROMO_NAME, product.name,
                                            ID, product.id, PRICE, product.priceFormat,
                                            BRAND, DEFAULT_VALUE,
                                            CATEGORY, product.category.id,
                                            VARIANT, DEFAULT_VALUE,
                                            PROMO_POSITION, position + 1)
                            ))
                ))
        )
    }

    fun eventTopAdsImpression(position: Int, product: Product) {
        analyticTracker?.sendEnhancedEcommerce(
                DataLayer.mapOf(KEY_EVENT, "productView",
                        KEY_CATEGORY, ProductTrackingConstant.Category.PDP.toLowerCase(),
                        KEY_ACTION, ProductTrackingConstant.Action.TOPADS_IMPRESSION,
                        KEY_LABEL, "",
                        KEY_ECOMMERCE, DataLayer.mapOf("currencyCode", "IDR", "impression",
                        DataLayer.listOf(
                                DataLayer.mapOf(PROMO_NAME, product.name,
                                        ID, product.id, PRICE, product.priceFormat,
                                        BRAND, DEFAULT_VALUE,
                                        CATEGORY, product.category.id,
                                        VARIANT, DEFAULT_VALUE,
                                        PROMO_POSITION, position + 1)
                        ))
        ))
    }

    fun eventClickAffiliate(userId: String, shopID: Int, productId: String, isRegularPdp: Boolean = false) {
        val params: MutableMap<String, Any> = if (isRegularPdp){
            mutableMapOf(KEY_EVENT to ProductTrackingConstant.PDP.EVENT,
                    KEY_CATEGORY to ProductTrackingConstant.Category.PDP.toLowerCase(),
                    KEY_ACTION to ProductTrackingConstant.Action.CLICK_BY_ME,
                    KEY_LABEL to "$shopID - $productId")
        } else {
            mutableMapOf(KEY_EVENT to ProductTrackingConstant.Affiliate.EVENT,
                    KEY_CATEGORY to ProductTrackingConstant.Affiliate.CATEGORY,
                    KEY_ACTION to ProductTrackingConstant.Affiliate.ACTION,
                    KEY_LABEL to productId)
        }
        params.put(KEY_USER_ID, userId)
        analyticTracker?.sendEventTracking(params)
    }

    fun eventSendMessage() {
        analyticTracker?.sendEventTracking(ProductTrackingConstant.Message.EVENT,
                ProductTrackingConstant.Category.PDP, ProductTrackingConstant.Action.CLICK,
                ProductTrackingConstant.Message.LABEL)
    }

    fun eventSendChat() {
        analyticTracker?.sendEventTracking(ProductTrackingConstant.PDP.EVENT,
                ProductTrackingConstant.Category.PRODUCT_PAGE.toLowerCase(),
                ProductTrackingConstant.Action.CLICK,
                ProductTrackingConstant.Message.LABEL.toLowerCase())
    }

    fun eventPDPWishlit() {
        TrackApp.getInstance()?.gtm?.sendGeneralEvent(mapOf(
                KEY_EVENT to ProductTrackingConstant.Wishlist.EVENT,
                KEY_CATEGORY to ProductTrackingConstant.Category.PDP,
                KEY_ACTION to ProductTrackingConstant.Action.CLICK,
                KEY_LABEL to ProductTrackingConstant.Wishlist.LABEL
        ))
    }

    fun eventPDPAddToWishlist(name: String) {
        TrackApp.getInstance()?.gtm?.sendGeneralEvent(mapOf(
                KEY_EVENT to ProductTrackingConstant.Wishlist.EVENT,
                KEY_CATEGORY to ProductTrackingConstant.Wishlist.CATEGORY,
                KEY_ACTION to ProductTrackingConstant.Action.CLICK,
                KEY_LABEL to "${ProductTrackingConstant.Wishlist.LABEL} - ${MethodChecker.fromHtml(name)}"
        ))
    }

    companion object {
        private const val KEY_EVENT = "event"
        private const val KEY_CATEGORY = "eventCategory"
        private const val KEY_ACTION = "eventAction"
        private const val KEY_LABEL = "eventLabel"
        private const val KEY_ECOMMERCE = "ecommerce"
        private const val KEY_PRODUCT_PROMO = "promoClick"
        private const val KEY_PROMOTIONS = "promotions"
        private const val KEY_USER_ID = "user_id"

        private const val ID = "id"
        private const val PROMO_NAME = "name"
        private const val PROMO_POSITION = "position"
        private const val PROMO_ID = "promo_id"
        private const val PROMO_CODE = "promo_id"

        private const val ACTION_FIELD = "actionField"
        private const val LIST = "list"
        private const val PRODUCTS = "products"
        private const val PRICE = "price"
        private const val BRAND = "brand"
        private const val DEFAULT_VALUE = "none / other"
        private const val VARIANT = "variant"
        private const val CATEGORY = "category"
    }

}
