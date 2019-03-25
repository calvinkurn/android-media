package com.tokopedia.product.detail.data.util

import android.net.Uri
import android.text.TextUtils
import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.product.detail.common.data.model.Category
import com.tokopedia.product.detail.common.data.model.ProductInfo
import com.tokopedia.product.detail.data.model.shop.ShopInfo
import com.tokopedia.topads.sdk.domain.model.Product
import com.tokopedia.track.TrackApp
import java.util.*

class ProductDetailTracking() {

    fun sendScreen(shopID: String, shopType: String, productId: String) {
        TrackApp.getInstance().gtm.sendScreenAuthenticated(
            PRODUCT_DETAIL_SCREEN_NAME,
            shopID, shopType, "/product", productId)
    }

    fun eventTalkClicked() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
            ProductTrackingConstant.Category.PDP,
            ProductTrackingConstant.Action.CLICK,
            ProductTrackingConstant.ProductTalk.TALK)
    }

    fun eventClickBuy(productId: String, isVariant: Boolean) {
        if (productId.isEmpty()) return
        eventClickBuyOrAddToCart(productId, isVariant,
            "click - beli")
    }

    fun eventClickAddToCart(productId: String, isVariant: Boolean) {
        if (productId.isEmpty()) return
        eventClickBuyOrAddToCart(productId, isVariant,
            "click - tambah ke keranjang")
    }

    private fun eventClickBuyOrAddToCart(productId: String, isVariant: Boolean,
                                         action: String) {
        if (productId.isEmpty()) return
        TrackApp.getInstance()?.gtm?.sendGeneralEvent(
            ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
            ProductTrackingConstant.Category.PDP,
            action,
            "$productId - " + if (isVariant) {
                "variant"
            } else {
                "non variant"
            })
    }

    fun eventReviewClicked() {
        TrackApp.getInstance()?.gtm?.sendGeneralEvent(ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
            ProductTrackingConstant.Category.PDP,
            ProductTrackingConstant.Action.CLICK,
            ProductTrackingConstant.ProductReview.REVIEW)
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

    fun eventCartMenuClicked(variant: String?) {
        TrackApp.getInstance()?.gtm?.sendGeneralEvent(ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
            ProductTrackingConstant.Category.PDP,
            ProductTrackingConstant.Action.CLICK_CART_BUTTON_VARIANT,
            variant ?: "")
    }

    fun eventClickMerchantVoucherUse(merchantVoucherViewModel: MerchantVoucherViewModel, position: Int) {
        TrackApp.getInstance()?.gtm?.sendEnhanceECommerceEvent(createEventMVCClick(ProductTrackingConstant.MerchantVoucher.EVENT,
            ProductTrackingConstant.Category.PDP,
            listOf(ProductTrackingConstant.MerchantVoucher.ACTION,
                ProductTrackingConstant.Action.CLICK).joinToString(" "),
            merchantVoucherViewModel, position))
    }

    fun eventImpressionMerchantVoucherUse(merchantVoucherViewModelList: List<MerchantVoucherViewModel>) {
        val map = createMvcImpressionMap(
            "promoView",
            ProductTrackingConstant.Category.PDP,
            "promo banner impression",
            "use voucher",
            merchantVoucherViewModelList)
        map?.run {
            TrackApp.getInstance()?.gtm?.sendEnhanceECommerceEvent(this)
        }
    }

    private fun createMvcImpressionMap(event: String, category: String, action: String, label: String,
                                       viewModelList: List<MerchantVoucherViewModel>): MutableMap<String, Any>? {
        val mvcListMap = createMvcListMap(viewModelList, 0)
        if (mvcListMap.isNotEmpty()) {
            val eventMap = createMap(event, category, action, label)
            eventMap.put(KEY_ECOMMERCE, DataLayer.mapOf(
                "promoView", DataLayer.mapOf(
                "promotions", mvcListMap)))
            return eventMap
        } else {
            return null
        }
    }

    private fun createMap(event: String, category: String, action: String, label: String):
        MutableMap<String, Any> {
        return mutableMapOf(
            KEY_EVENT to event,
            KEY_CATEGORY to category,
            KEY_ACTION to action,
            KEY_LABEL to label
        )
    }

    private fun createMvcListMap(viewModelList: List<MerchantVoucherViewModel>, startIndex: Int): List<Any> {
        val list = mutableListOf<Any>()
        for (i in viewModelList.indices) {
            val viewModel = viewModelList[i]
            if (viewModel.isAvailable()) {
                list.add(
                    DataLayer.mapOf(
                        ID, viewModel.voucherId,
                        PROMO_NAME, viewModel.voucherName,
                        PROMO_POSITION, (startIndex + i + 1).toString(),
                        PROMO_ID, viewModel.voucherId,
                        PROMO_CODE, viewModel.voucherCode
                    )
                )
            }
        }
        return list
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
        return list
    }

    fun eventAtcClickLihat(productId: String?) {
        if (productId.isNullOrEmpty()) {
            return
        }
        TrackApp.getInstance()?.gtm?.sendGeneralEvent(
            ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
            ProductTrackingConstant.Category.PDP,
            "click - cek keranjang",
            productId)
    }

    fun eventClickVariant(eventLabel: String) {
        TrackApp.getInstance()?.gtm?.sendGeneralEvent(
            ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
            ProductTrackingConstant.Category.PDP,
            "click - variants",
            eventLabel)
    }

    fun eventClickMerchantVoucherSeeDetail(id: Int) {
        TrackApp.getInstance()?.gtm?.sendGeneralEvent(ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
            ProductTrackingConstant.Category.PDP,
            listOf(ProductTrackingConstant.Action.CLICK, ProductTrackingConstant.MerchantVoucher.MERCHANT_VOUCHER,
                ProductTrackingConstant.MerchantVoucher.DETAIL).joinToString(" - "),
            id.toString())
    }

    fun eventClickMerchantVoucherSeeAll(id: Int) {
        TrackApp.getInstance()?.gtm?.sendGeneralEvent(ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
            ProductTrackingConstant.Category.PDP,
            listOf(ProductTrackingConstant.Action.CLICK, ProductTrackingConstant.MerchantVoucher.MERCHANT_VOUCHER,
                ProductTrackingConstant.MerchantVoucher.SEE_ALL).joinToString(" - "),
            id.toString())
    }

    fun eventTopAdsClicked(product: Product, position: Int) {
        TrackApp.getInstance()?.gtm?.sendEnhanceECommerceEvent(
            DataLayer.mapOf(KEY_EVENT, ProductTrackingConstant.Action.PRODUCT_CLICK,
                KEY_CATEGORY, ProductTrackingConstant.Category.PDP,
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
                KEY_CATEGORY, ProductTrackingConstant.Category.PDP,
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

    fun eventClickWishlistOnAffiliate(userId: String,
                                      productId: String) {
        TrackApp.getInstance()?.gtm?.sendGeneralEvent(
            mutableMapOf<String, Any>(KEY_EVENT to ProductTrackingConstant.Affiliate.CLICK_AFFILIATE,
                KEY_CATEGORY to ProductTrackingConstant.Affiliate.CATEGORY,
                KEY_ACTION to ProductTrackingConstant.Affiliate.ACTION_CLICK_WISHLIST,
                KEY_LABEL to productId,
                KEY_USER_ID to userId))
    }

    fun eventClickAffiliate(userId: String, shopID: Int, productId: String, isRegularPdp: Boolean = false) {
        val params: MutableMap<String, Any> = if (isRegularPdp) {
            mutableMapOf(KEY_EVENT to ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                KEY_CATEGORY to ProductTrackingConstant.Category.PDP,
                KEY_ACTION to ProductTrackingConstant.Action.CLICK_BY_ME,
                KEY_LABEL to "$shopID - $productId")
        } else {
            mutableMapOf(KEY_EVENT to ProductTrackingConstant.Affiliate.CLICK_AFFILIATE,
                KEY_CATEGORY to ProductTrackingConstant.Affiliate.CATEGORY,
                KEY_ACTION to ProductTrackingConstant.Affiliate.ACTION,
                KEY_LABEL to productId)
        }
        params.put(KEY_USER_ID, userId)
        TrackApp.getInstance().gtm.sendGeneralEvent(params)
    }

    fun eventSendMessage() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            ProductTrackingConstant.Message.EVENT,
            ProductTrackingConstant.Category.PDP,
            ProductTrackingConstant.Action.CLICK,
            ProductTrackingConstant.Message.LABEL
        )
    }

    fun eventSendChat() {
        TrackApp.getInstance()?.gtm?.sendGeneralEvent(
            ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
            ProductTrackingConstant.Category.PRODUCT_PAGE.toLowerCase(),
            ProductTrackingConstant.Action.CLICK,
            ProductTrackingConstant.Message.LABEL.toLowerCase()
        )
    }

    fun eventPDPAddToWishlist(productId: String?) {
        if (productId.isNullOrEmpty()) return
        TrackApp.getInstance()?.gtm?.sendGeneralEvent(
            ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
            ProductTrackingConstant.Category.PDP,
            "add wishlist",
            productId)
    }

    fun eventPDPAddToWishlistNonLogin(productId: String?) {
        if (productId.isNullOrEmpty()) return
        TrackApp.getInstance()?.gtm?.sendGeneralEvent(
            ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
            ProductTrackingConstant.Category.PDP,
            "add wishlist - non logged in",
            productId)
    }

    fun eventPDPRemoveToWishlist(productId: String?) {
        if (productId.isNullOrEmpty()) return
        TrackApp.getInstance()?.gtm?.sendGeneralEvent(
            ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
            ProductTrackingConstant.Category.PDP,
            "remove wishlist",
            productId)
    }

    fun eventClickReviewOnSeeAllImage(productId: Int) {
        TrackApp.getInstance()?.gtm?.sendGeneralEvent(
            ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
            ProductTrackingConstant.Category.PDP,
            ProductTrackingConstant.ImageReview.ACTION_SEE_ALL,
            productId.toString()
        )
    }

    fun eventClickReviewOnBuyersImage(productId: Int, reviewId: String?) {
        TrackApp.getInstance()?.gtm?.sendGeneralEvent(
            ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
            ProductTrackingConstant.Category.PDP,
            ProductTrackingConstant.ImageReview.ACTION_SEE_ITEM,
            "product_id: $productId - review_id : $reviewId"
        )
    }

    fun eventClickReviewOnMostHelpfulReview(productId: Int?, reviewId: String?) {
        TrackApp.getInstance()?.gtm?.sendGeneralEvent(
            ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
            ProductTrackingConstant.Category.PDP,
            "click - review gallery on most helpful review",
            "product_id: $productId - review_id : $reviewId"
        )
    }

    private fun getEnhanceCategoryFormatted(detail: List<Category.Detail>?): String {
        val list = ArrayList<String>()
        if (detail != null) {
            for (i in 0 until detail.size) {
                list.add(detail.get(i).name)
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
        if (!TextUtils.isEmpty(url)) {
            val uri = Uri.parse(url)
            return uri.lastPathSegment
        }
        return ""
    }

    fun eventEnhanceEcommerceProductDetail(trackerListName: String?, productInfo: ProductInfo?, shopInfo: ShopInfo?, trackerAttribution: String?) {
        TrackApp.getInstance()?.gtm?.sendEnhanceECommerceEvent(DataLayer.mapOf(
            "event", "viewProduct",
            "eventCategory", "product page",
            "eventAction", "view product page",
            "eventLabel", getEnhanceShopType(shopInfo?.goldOS) + " - " + shopInfo?.shopCore?.name + " - " + productInfo?.basic?.name,
            "ecommerce", DataLayer.mapOf(
            "currencyCode", "IDR",
            "detail", DataLayer.mapOf(
            "products", DataLayer.listOf(
            DataLayer.mapOf(
                "name", productInfo?.basic?.name,
                "id", productInfo?.basic?.id,
                "price", productInfo?.basic?.price,
                "brand", "none / other",
                "category", getEnhanceCategoryFormatted(productInfo?.category?.detail),
                "variant", "none / other",
                "dimension38", trackerAttribution ?: "none / other"))).apply {
            if (trackerListName?.isNotEmpty() == true) {
                put("actionField", DataLayer.mapOf("list", trackerListName))
            }
        }),
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

    ////////////////////////////////////////////////////////////////
    // APPSFYLER START
    ////////////////////////////////////////////////////////////////
    fun eventPDPWishlistAppsFyler(productInfo: ProductInfo) {
        eventAppsFyler(productInfo, "af_add_to_wishlist")
    }

    fun eventAppsFylerOpenProduct(productInfo: ProductInfo) {
        eventAppsFyler(productInfo, "af_content_view")
    }

    private fun eventAppsFyler(productInfo: ProductInfo, eventName:String) {
        TrackApp.getInstance()?.appsFlyer?.run {
            sendEvent(eventName,
                mutableMapOf<String, Any>(
                    "af_description" to "productView",
                    "af_content_id" to productInfo.basic.id,
                    "af_content_type" to "product",
                    "af_price" to productInfo.basic.price,
                    "af_currency" to "IDR",
                    "af_quantity" to 1.toString()
                ).apply {
                    if (productInfo.category.detail.isNotEmpty()) {
                        val size = productInfo.category.detail.size
                        for (i in 1..size) {
                            put("level" + i + "_name", productInfo.category.detail.get(size - i).name)
                            put("level" + i + "_id", productInfo.category.detail.get(size - i).id)
                        }
                    }
                }
            )
        }
    }

    ////////////////////////////////////////////////////////////////
    // APPSFYLER END
    ////////////////////////////////////////////////////////////////


    ////////////////////////////////////////////////////////////////
    // MOENGAGE START
    ////////////////////////////////////////////////////////////////
    fun sendMoEngagePDPReferralCodeShareEvent() {
        TrackApp.getInstance()?.moEngage?.sendEvent("Share_Event",
            mutableMapOf<String, Any>(
                "channel" to "lainnya",
                "source" to "pdp_share"
            )
        )
    }

    fun sendMoEngageClickReview(productInfo: ProductInfo, isOfficialStore: Boolean, shopName: String) {
        sendMoEngage(productInfo, isOfficialStore, shopName, "Clicked_Ulasan_Pdp")
    }

    fun sendMoEngageOpenProduct(productInfo: ProductInfo, isOfficialStore: Boolean, shopName: String) {
        sendMoEngage(productInfo, isOfficialStore, shopName, "Product_Page_Opened")
    }

    fun sendMoEngageClickDiskusi(productInfo: ProductInfo, isOfficialStore: Boolean, shopName: String) {
        sendMoEngage(productInfo, isOfficialStore, shopName, "Clicked_Diskusi_Pdp")
    }

    private fun sendMoEngage(productInfo: ProductInfo,
                             isOfficialStore: Boolean,
                             shopName: String,
                             eventName: String) {
        productInfo.category.breadcrumbUrl
        TrackApp.getInstance()?.moEngage?.sendEvent(
            eventName,
            mutableMapOf<String, Any>().apply {
                if (productInfo.category.detail.isNotEmpty()) {
                    put("category", productInfo.category.detail[0].name)
                    put("category_id", productInfo.category.detail[0].id)
                }
                if (productInfo.category.detail.size > 1) {
                    put("subcategory", productInfo.category.detail[1].name)
                    put("subcategory_id", productInfo.category.detail[1].id)
                }
                put("product_name", productInfo.basic.name)
                put("product_id", productInfo.basic.id)
                put("product_url", productInfo.basic.url)
                put("product_price", productInfo.basic.price)
                put("is_official_store", isOfficialStore)
                put("shop_id", productInfo.basic.shopID)
                put("shop_name", shopName)
                if (productInfo.pictures?.isNotEmpty() == true) {
                    put("product_image_url", productInfo.pictures?.get(0)?.urlOriginal ?: "")
                }
            }
        )
    }

    ////////////////////////////////////////////////////////////////
    // MOENGAGE END
    ////////////////////////////////////////////////////////////////

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
