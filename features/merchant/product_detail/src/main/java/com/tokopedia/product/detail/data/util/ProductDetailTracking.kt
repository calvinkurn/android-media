package com.tokopedia.product.detail.data.util

import android.net.Uri
import android.text.TextUtils
import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.product.detail.common.data.model.product.Category
import com.tokopedia.product.detail.common.data.model.product.ProductInfo
import com.tokopedia.product.detail.data.model.shop.ShopInfo
import com.tokopedia.topads.sdk.domain.model.Product
import com.tokopedia.track.TrackApp
import java.util.*

class ProductDetailTracking() {

    fun eventTalkClicked() {
        TrackApp.getInstance()?.gtm?.sendGeneralEvent(ProductTrackingConstant.PDP.EVENT,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.CLICK,
                ProductTrackingConstant.ProductTalk.EVENT_LABEL)
    }

    fun eventReviewClicked() {
        TrackApp.getInstance()?.gtm?.sendGeneralEvent(ProductTrackingConstant.PDP.EVENT,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.CLICK,
                ProductTrackingConstant.ProductReview.EVENT_LABEL)
    }

    fun eventReportLogin() {
        TrackApp.getInstance()?.gtm?.sendGeneralEvent(ProductTrackingConstant.Report.EVENT,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.CLICK,
                ProductTrackingConstant.Report.EVENT_LABEL)
    }

    fun eventReportNoLogin() {
        TrackApp.getInstance()?.gtm?.sendGeneralEvent(ProductTrackingConstant.Report.EVENT,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.CLICK,
                ProductTrackingConstant.Report.NOT_LOGIN_EVENT_LABEL)
    }

    fun eventCartMenuClicked(variant: String) {
        TrackApp.getInstance()?.gtm?.sendGeneralEvent(ProductTrackingConstant.PDP.EVENT,
                ProductTrackingConstant.Category.PDP.toLowerCase(),
                ProductTrackingConstant.Action.CLICK_CART_BUTTON_VARIANT,
                variant)
    }

    fun eventClickMerchantVoucherUse(merchantVoucherViewModel: MerchantVoucherViewModel, position: Int) {
        TrackApp.getInstance()?.gtm?.sendGeneralEvent(createEventMVCClick(ProductTrackingConstant.MerchantVoucher.EVENT,
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
        TrackApp.getInstance()?.gtm?.sendGeneralEvent(ProductTrackingConstant.PDP.EVENT,
                ProductTrackingConstant.Category.PDP.toLowerCase(),
                listOf(ProductTrackingConstant.Action.CLICK, ProductTrackingConstant.MerchantVoucher.MERCHANT_VOUCHER,
                        ProductTrackingConstant.MerchantVoucher.DETAIL).joinToString(" - "),
                id.toString())
    }

    fun eventClickMerchantVoucherSeeAll(id: Int) {
        TrackApp.getInstance()?.gtm?.sendGeneralEvent(ProductTrackingConstant.PDP.EVENT,
                ProductTrackingConstant.Category.PDP.toLowerCase(),
                listOf(ProductTrackingConstant.Action.CLICK, ProductTrackingConstant.MerchantVoucher.MERCHANT_VOUCHER,
                        ProductTrackingConstant.MerchantVoucher.SEE_ALL).joinToString(" - "),
                id.toString())
    }

    fun eventTopAdsClicked(product: Product, position: Int) {
        TrackApp.getInstance()?.gtm?.sendEnhanceECommerceEvent(
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
        TrackApp.getInstance()?.gtm?.sendEnhanceECommerceEvent(
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
        val params: MutableMap<String, Any> = if (isRegularPdp) {
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
        TrackApp.getInstance()?.gtm?.sendGeneralEvent(params)
    }

    fun eventSendMessage() {
        TrackApp.getInstance()?.gtm?.sendGeneralEvent(
            ProductTrackingConstant.Message.EVENT,
            ProductTrackingConstant.Category.PDP,
            ProductTrackingConstant.Action.CLICK,
            ProductTrackingConstant.Message.LABEL
        )
    }

    fun eventSendChat() {
        TrackApp.getInstance()?.gtm?.sendGeneralEvent(
            ProductTrackingConstant.PDP.EVENT,
            ProductTrackingConstant.Category.PRODUCT_PAGE.toLowerCase(),
            ProductTrackingConstant.Action.CLICK,
            ProductTrackingConstant.Message.LABEL.toLowerCase()
        )
    }

    fun eventPDPWishlit() {
        TrackApp.getInstance()?.gtm?.sendGeneralEvent(
                ProductTrackingConstant.Wishlist.EVENT,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.CLICK,
                ProductTrackingConstant.Wishlist.LABEL
        )
    }

    fun eventPDPAddToWishlist(name: String) {
        TrackApp.getInstance()?.gtm?.sendGeneralEvent(
            ProductTrackingConstant.Wishlist.EVENT,
            ProductTrackingConstant.Wishlist.CATEGORY,
            ProductTrackingConstant.Action.CLICK,
            "${ProductTrackingConstant.Wishlist.LABEL} - ${MethodChecker.fromHtml(name)}"
        )
    }

    fun eventClickReviewOnSeeAllImage(productId: Int) {
        TrackApp.getInstance()?.gtm?.sendGeneralEvent(
            ProductTrackingConstant.PDP.EVENT,
            ProductTrackingConstant.Category.PDP.toLowerCase(),
            ProductTrackingConstant.ImageReview.ACTION_SEE_ALL,
            productId.toString()
        )
    }

    fun eventClickReviewOnBuyersImage(productId: Int, reviewId: String?) {
        TrackApp.getInstance()?.gtm?.sendGeneralEvent(
            ProductTrackingConstant.PDP.EVENT,
            ProductTrackingConstant.Category.PDP.toLowerCase(),
            ProductTrackingConstant.ImageReview.ACTION_SEE_ITEM,
            "product_id: $productId - review_id : $reviewId"
        )
    }

    fun eventClickReviewOnMostHelpfulReview(productId: Int?, reviewId: String?) {
        TrackApp.getInstance()?.gtm?.sendGeneralEvent(
            ProductTrackingConstant.PDP.EVENT,
            ProductTrackingConstant.Category.PDP.toLowerCase(),
            ProductTrackingConstant.ImageReview.ACTION_MOST_HELPFULL,
            "product_id: $productId - review_id : $reviewId"
        )
    }

    private fun getEnhanceCategoryFormatted(detail: List<Category.Detail>?): String {
        val list = ArrayList<String>()
        if(detail!= null) {
            for (i in 0 until detail.size) {
                list.add(detail.get(i).name ?: "")
            }
        }
        return TextUtils.join("/", list)
    }

    private fun getEnhanceShopType(goldOS: ShopInfo.GoldOS?): String {
        return if (goldOS?.isOfficial == 1) {
            "official_store"
        } else if (goldOS?.isGold == 1) {
            "gold_merchant"
        } else {
            "regular"
        }
    }

    private fun getEnhanceUrl(url: String?): String? {
        if(!TextUtils.isEmpty(url)) {
            val uri = Uri.parse(url)
            return uri.lastPathSegment
        }
        return ""
    }

    fun eventEnhanceEcommerceProductDetail(trackerListName: String?, productInfo: ProductInfo?, shopInfo: ShopInfo?, trackerAttribution: String?) {
        var detail: Map<String, Any>
        if (TextUtils.isEmpty(trackerListName)) {
            detail = DataLayer.mapOf(
                    "products", DataLayer.listOf(
                    DataLayer.mapOf(
                            "name", productInfo?.basic?.name,
                            "id", productInfo?.basic?.id,
                            "price", productInfo?.basic?.price,
                            "brand", "none / other",
                            "category", getEnhanceCategoryFormatted(productInfo?.category?.detail),
                            "variant", "none / other",
                            "dimension38", trackerAttribution?: "none / other"
                    )
            )
            )
        } else {
            detail = DataLayer.mapOf(
                    "actionField", DataLayer.mapOf("list", trackerListName),
                    "products", DataLayer.listOf(
                    DataLayer.mapOf(
                            "name", productInfo?.basic?.name,
                            "id", productInfo?.basic?.id,
                            "price", productInfo?.basic?.price,
                            "brand", "none / other",
                            "category", getEnhanceCategoryFormatted(productInfo?.category?.detail),
                            "variant", "none / other",
                            "dimension38", trackerAttribution?: "none / other"
                    )
            )
            )
        }
        TrackApp.getInstance()?.gtm?.sendEnhanceECommerceEvent(DataLayer.mapOf(
                "event", "viewProduct",
                "eventCategory", "product page",
                "eventAction", "view product page",
                "eventLabel", String.format(
                Locale.getDefault(),
                "%s - %s - %s",
                getEnhanceShopType(shopInfo?.goldOS), shopInfo?.shopCore?.name, productInfo?.basic?.name
        ),
                "ecommerce", DataLayer.mapOf(
                "currencyCode", "IDR",
                "detail", detail
        ),
                "key", getEnhanceUrl(productInfo?.basic?.url),
                "shopName", shopInfo?.shopCore?.name,
                "shopId", productInfo?.basic?.shopID,
                "shopDomain", shopInfo?.shopCore?.domain,
                "shopLocation", shopInfo?.location,
                "shopIsGold", shopInfo?.goldOS?.isGoldBadge.toString(),
                "categoryId", productInfo?.category?.id,
                "url", productInfo?.basic?.url,
                "shopType", getEnhanceShopType(shopInfo?.goldOS)
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

        const val PRODUCT_DETAIL_SCREEN_NAME = "Product Info"

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
