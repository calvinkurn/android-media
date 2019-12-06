package com.tokopedia.product.detail.data.util

import android.net.Uri
import android.text.TextUtils
import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.linker.LinkerConstants
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.product.detail.common.data.model.product.Category
import com.tokopedia.product.detail.common.data.model.product.ProductInfo
import com.tokopedia.product.detail.data.util.ProductTrackingConstant.Action.PRODUCT_VIEW
import com.tokopedia.product.detail.data.util.ProductTrackingConstant.Action.RECOMMENDATION_CLICK
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.topads.sdk.domain.model.Shop
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.trackingoptimizer.TrackingQueue
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class ProductDetailTracking @Inject constructor(private val trackingQueue: TrackingQueue) {

    private val currencyLable = "IDR"
    private val screenName = PRODUCT_DETAIL_SCREEN_NAME

    fun sendScreen(shopID: String, shopType: String, productId: String) {
        TrackApp.getInstance().gtm.sendScreenAuthenticated(screenName, shopID,
                shopType, "/product", productId)
    }

    fun trackTradeinBeforeDiagnotics(){
        sendGeneralEvent(" clickPDP",
                "product detail page",
                "click trade in widget",
                "before diagnostic")
    }

    fun trackTradeinAfterDiagnotics(){
        sendGeneralEvent(" clickPDP",
                "product detail page",
                "click trade in widget",
                "after diagnostic")
    }

    fun eventTalkClicked() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.CLICK,
                ProductTrackingConstant.ProductTalk.TALK)
    }

    fun eventShippingClicked(productId: String) {
        val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.CLICK_SHIPPING,
                ""
        )
        mapEvent[KEY_PRODUCT_ID] = productId
        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }

    fun eventShippingRateEstimationClicked() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.CLICK_SHIPPING_RATE_ESTIMATION,
                "")
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
        val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                action,
                "$productId - " + if (isVariant) {
                    "variant"
                } else {
                    "non variant"
                }
        )
        mapEvent[KEY_PRODUCT_ID] = productId
        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }

    fun eventReviewClickedIris(productInfo: ProductInfo?, deeplinkUrl: String,
                               isOfficial: Boolean, shopName: String) {

        var categoryNameLvl2 = ""
        var categoryIdLvl2 = ""
        if (productInfo?.category?.detail?.size ?: 0 >= 2) {
            productInfo?.category?.detail?.get(1)?.let {
                categoryIdLvl2 = it.id
                categoryNameLvl2 = it.name
            }
        }

        val imageUrl = productInfo?.media?.filter {
            it.type == "image"
        }?.firstOrNull()?.urlOriginal ?: ""

        val mapOfData: Map<String, Any?> = mapOf(KEY_EVENT to "clickPDP",
                KEY_CATEGORY to "product detail page",
                KEY_ACTION to "click",
                KEY_LABEL to "review",
                "subcategory" to categoryNameLvl2,
                "subcategoryId" to categoryIdLvl2,
                "category" to productInfo?.category?.name,
                "categoryId" to productInfo?.category?.id,
                "productName" to productInfo?.basic?.name,
                "productId" to productInfo?.basic?.id,
                "productUrl" to productInfo?.basic?.url,
                "productDepplinkUrl" to deeplinkUrl,
                "productImageUrl" to imageUrl,
                "productPrice" to productInfo?.basic?.price?.toInt(),
                "isOfficialStore" to isOfficial,
                "shopId" to productInfo?.basic?.shopID,
                "shopName" to shopName,
                "productPriceFormatted" to getFormattedPrice(productInfo?.basic?.price?.toInt() ?: 0)
        )

        TrackApp.getInstance().gtm.sendGeneralEvent(mapOfData)
    }

    fun eventDiscussionClickedIris(productInfo: ProductInfo?, deeplinkUrl: String,
                               isOfficial: Boolean, shopName: String) {

        var categoryNameLvl2 = ""
        var categoryIdLvl2 = ""
        if (productInfo?.category?.detail?.size ?: 0 >= 2) {
            productInfo?.category?.detail?.get(1)?.let {
                categoryIdLvl2 = it.id
                categoryNameLvl2 = it.name
            }
        }

        val imageUrl = productInfo?.media?.filter {
            it.type == "image"
        }?.firstOrNull()?.urlOriginal ?: ""

        val mapOfData: Map<String, Any?> =
                mapOf(KEY_EVENT to "clickPDP",
                KEY_CATEGORY to "product detail page",
                KEY_ACTION to "Click",
                KEY_LABEL to "Talk",
                "subcategory" to categoryNameLvl2,
                "subcategoryId" to categoryIdLvl2,
                "category" to productInfo?.category?.name,
                "categoryId" to productInfo?.category?.id,
                "productName" to productInfo?.basic?.name,
                "productId" to productInfo?.basic?.id,
                "productUrl" to productInfo?.basic?.url,
                "productDepplinkUrl" to deeplinkUrl,
                "productImageUrl" to imageUrl,
                "productPrice" to productInfo?.basic?.price?.toInt(),
                "isOfficialStore" to isOfficial,
                "shopId" to productInfo?.basic?.shopID,
                "shopName" to shopName,
                "productPriceFormatted" to getFormattedPrice(productInfo?.basic?.price?.toInt() ?: 0)
        )

        TrackApp.getInstance().gtm.sendGeneralEvent(mapOfData)
    }

    fun eventReviewClicked() {
        TrackApp.getInstance().gtm.sendGeneralEvent(ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.CLICK,
                ProductTrackingConstant.ProductReview.REVIEW)
    }

    fun eventReportLogin() {
        TrackApp.getInstance().gtm.sendGeneralEvent(ProductTrackingConstant.Report.EVENT,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.CLICK,
                ProductTrackingConstant.Report.EVENT_LABEL)
    }

    fun eventReportNoLogin() {
        TrackApp.getInstance().gtm.sendGeneralEvent(ProductTrackingConstant.Report.EVENT,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.CLICK,
                ProductTrackingConstant.Report.NOT_LOGIN_EVENT_LABEL)
    }

    fun eventCartMenuClicked(variant: String?, productId: String) {
        val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.CLICK_CART_BUTTON_VARIANT,
                variant ?: ""
        )
        mapEvent[KEY_PRODUCT_ID] = productId
        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }

    fun eventClickMerchantVoucherUse(merchantVoucherViewModel: MerchantVoucherViewModel, position: Int) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(createEventMVCClick(ProductTrackingConstant.MerchantVoucher.EVENT,
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
            TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(this)
        }
    }

    private fun createMvcImpressionMap(event: String, category: String, action: String, label: String,
                                       viewModelList: List<MerchantVoucherViewModel>): MutableMap<String, Any>? {
        val mvcListMap = createMvcListMap(viewModelList, 0)
        return if (mvcListMap.isNotEmpty()) {
            val eventMap = createMap(event, category, action, label)
            eventMap[KEY_ECOMMERCE] = DataLayer.mapOf(
                    "promoView", DataLayer.mapOf(
                    "promotions", mvcListMap))
            eventMap
        } else {
            null
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
        return vouchers.withIndex().filter { it.value.isAvailable() }.map {
            DataLayer.mapOf(ID, it.value.voucherId,
                    PROMO_NAME, it.value.voucherName,
                    PROMO_POSITION, (position + it.index + 1).toString(),
                    PROMO_ID, it.value.voucherId,
                    PROMO_CODE, it.value.voucherCode)
        }
    }

    fun eventAtcClickLihat(productId: String?) {
        if (productId.isNullOrEmpty()) {
            return
        }
        val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                "click - cek keranjang",
                productId
        )
        mapEvent[KEY_PRODUCT_ID] = productId
        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }

    fun eventClickVariant(eventLabel: String, productId: String) {
        val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                "click - variants",
                eventLabel
        )
        mapEvent[KEY_PRODUCT_ID] = productId
        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }

    fun eventClickMerchantVoucherSeeDetail(id: Int) {
        TrackApp.getInstance().gtm.sendGeneralEvent(ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                listOf(ProductTrackingConstant.Action.CLICK, ProductTrackingConstant.MerchantVoucher.MERCHANT_VOUCHER,
                        ProductTrackingConstant.MerchantVoucher.DETAIL).joinToString(" - "),
                id.toString())
    }

    fun eventClickMerchantVoucherSeeAll(id: Int) {
        TrackApp.getInstance().gtm.sendGeneralEvent(ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                listOf(ProductTrackingConstant.Action.CLICK, ProductTrackingConstant.MerchantVoucher.MERCHANT_VOUCHER,
                        ProductTrackingConstant.MerchantVoucher.SEE_ALL).joinToString(" - "),
                id.toString())
    }

    // e commerce, custom dimension, general
    fun eventRecommendationClick(product: RecommendationItem, position: Int, isSessionActive: Boolean, pageName: String, pageTitle: String) {
        val listValue = LIST_DEFAULT + pageName +
                (if (!isSessionActive) " - ${ProductTrackingConstant.USER_NON_LOGIN}" else "") +
                LIST_RECOMMENDATION + product.recommendationType + (if (product.isTopAds) " - product topads" else "")

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(KEY_EVENT, ProductTrackingConstant.Action.PRODUCT_CLICK,
                        KEY_CATEGORY, ProductTrackingConstant.Category.PDP,
                        KEY_ACTION, ProductTrackingConstant.Action.TOPADS_CLICK +
                        (if (!isSessionActive) " - ${ProductTrackingConstant.USER_NON_LOGIN}" else ""),
                        KEY_LABEL, pageTitle,
                        KEY_ECOMMERCE, DataLayer.mapOf(CURRENCY_CODE, CURRENCY_DEFAULT_VALUE,
                        ProductTrackingConstant.Action.CLICK,
                        DataLayer.mapOf(ACTION_FIELD, DataLayer.mapOf(LIST, listValue),
                                PRODUCTS, DataLayer.listOf(
                                DataLayer.mapOf(PROMO_NAME, product.name,
                                        ID, product.productId.toString(), PRICE, removeCurrencyPrice(product.price),
                                        BRAND, DEFAULT_VALUE,
                                        VARIANT, DEFAULT_VALUE,
                                        CATEGORY, product.categoryBreadcrumbs.toLowerCase(),
                                        PROMO_POSITION, position + 1,
                                        DATA_DIMENSION_83, if (product.isFreeOngkirActive) VALUE_BEBAS_ONGKIR else VALUE_NONE_OTHER,
                                        KEY_PRODUCT_ID, product.productId.toString()
                                )
                        ))
                ))
        )
    }

    fun eventAddToCartRecommendationClick(product: RecommendationItem, position: Int, isSessionActive: Boolean, pageName: String, pageTitle: String) {
        val valueLoginOrNotLogin = if (!isSessionActive)
            " ${ProductTrackingConstant.USER_NON_LOGIN} - "
        else ""
        val listValue = LIST_PRODUCT_AFTER_ATC + pageName + LIST_RECOMMENDATION + valueLoginOrNotLogin +
                product.recommendationType + (if (product.isTopAds) " - product topads" else "")
        val actionValuePostfix = if (!isSessionActive)
            " - ${ProductTrackingConstant.USER_NON_LOGIN}"
        else
            ""

        val data = DataLayer.mapOf(
                KEY_EVENT, ProductTrackingConstant.Action.PRODUCT_CLICK,
                KEY_CATEGORY, ProductTrackingConstant.Category.PDP_AFTER_ATC,
                KEY_ACTION, ProductTrackingConstant.Action.TOPADS_CLICK + actionValuePostfix,
                KEY_LABEL, pageTitle,
                KEY_ECOMMERCE, DataLayer.mapOf(ProductTrackingConstant.Action.CLICK, DataLayer.mapOf(
                ACTION_FIELD, DataLayer.mapOf(LIST, listValue),
                PRODUCTS, DataLayer.listOf(
                DataLayer.mapOf(
                        PROMO_NAME, product.name,
                        ID, product.productId.toString(),
                        PRICE, removeCurrencyPrice(product.price),
                        BRAND, DEFAULT_VALUE,
                        CATEGORY, product.categoryBreadcrumbs.toLowerCase(),
                        VARIANT, DEFAULT_VALUE,
                        PROMO_POSITION, position,
                        DATA_DIMENSION_83, if (product.isFreeOngkirActive) VALUE_BEBAS_ONGKIR else VALUE_NONE_OTHER
                )
        ))
        ))
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(data)
    }

    fun eventRecommendationImpression(position: Int, product: RecommendationItem, isSessionActive: Boolean, pageName: String, pageTitle: String) {
        val listValue = LIST_DEFAULT + pageName +
                (if (!isSessionActive) " - ${ProductTrackingConstant.USER_NON_LOGIN}" else "") +
                LIST_RECOMMENDATION + product.recommendationType + (if (product.isTopAds) " - product topads" else "")

        val enhanceEcommerceData = DataLayer.mapOf(KEY_EVENT, PRODUCT_VIEW,
                KEY_CATEGORY, ProductTrackingConstant.Category.PDP,
                KEY_ACTION, ProductTrackingConstant.Action.TOPADS_IMPRESSION +
                (if (!isSessionActive) " - ${ProductTrackingConstant.USER_NON_LOGIN}" else ""),
                KEY_LABEL, pageTitle,

                KEY_ECOMMERCE, DataLayer.mapOf(

                CURRENCY_CODE, CURRENCY_DEFAULT_VALUE,

                IMPRESSIONS, DataLayer.listOf(
                DataLayer.mapOf(PROMO_NAME, product.name,
                        ID, product.productId.toString(),
                        PRICE, removeCurrencyPrice(product.price),
                        BRAND, DEFAULT_VALUE,
                        VARIANT, DEFAULT_VALUE,
                        CATEGORY, product.categoryBreadcrumbs.toLowerCase(),
                        PROMO_POSITION, position + 1,
                        LIST, listValue,
                        DATA_DIMENSION_83, if (product.isFreeOngkirActive) VALUE_BEBAS_ONGKIR else VALUE_NONE_OTHER,
                        KEY_PRODUCT_ID, product.productId.toString()
                )
        ))
        )
        trackingQueue.putEETracking(enhanceEcommerceData as HashMap<String, Any>?)
    }

    fun eventAddToCartRecommendationImpression(position: Int, product: RecommendationItem, isSessionActive: Boolean, pageName: String, pageTitle: String, trackingQueue: TrackingQueue) {
        val valueLoginOrNotLogin = if (!isSessionActive)
            " ${ProductTrackingConstant.USER_NON_LOGIN} - "
        else ""
        val listValue = LIST_PRODUCT_AFTER_ATC + pageName + LIST_RECOMMENDATION + valueLoginOrNotLogin +
                product.recommendationType + (if (product.isTopAds) " - product topads" else "")
        val valueActionPostfix = if (!isSessionActive)
            " - ${ProductTrackingConstant.USER_NON_LOGIN}"
        else ""

        val enhanceEcommerceData = DataLayer.mapOf(
                KEY_EVENT, PRODUCT_VIEW,
                KEY_CATEGORY, ProductTrackingConstant.Category.PDP_AFTER_ATC,
                KEY_ACTION, ProductTrackingConstant.Action.TOPADS_IMPRESSION + valueActionPostfix,
                KEY_LABEL, pageTitle,
                KEY_ECOMMERCE, DataLayer.mapOf(
                CURRENCY_CODE, CURRENCY_DEFAULT_VALUE,
                IMPRESSIONS, DataLayer.listOf(
                DataLayer.mapOf(
                        PROMO_NAME, product.name,
                        ID, product.productId.toString(),
                        PRICE, removeCurrencyPrice(product.price),
                        BRAND, DEFAULT_VALUE,
                        CATEGORY, product.categoryBreadcrumbs.toLowerCase(),
                        VARIANT, DEFAULT_VALUE,
                        LIST, listValue,
                        PROMO_POSITION, position,
                        DATA_DIMENSION_83, if (product.isFreeOngkirActive) VALUE_BEBAS_ONGKIR else VALUE_NONE_OTHER
                )
        ))
        )
        trackingQueue.putEETracking(enhanceEcommerceData as HashMap<String, Any>?)
    }

    fun eventAddToCartRecommendationWishlist(product: RecommendationItem, isSessionActive: Boolean, isAddWishlist: Boolean) {
        val valueActionPostfix = if (!isSessionActive) " - ${ProductTrackingConstant.USER_NON_LOGIN}"
        else ""
        val valueActionPrefix = if (isAddWishlist) "add"
        else "remove"


        val enhanceEcommerceData = DataLayer.mapOf(
                KEY_EVENT, RECOMMENDATION_CLICK,
                KEY_CATEGORY, ProductTrackingConstant.Category.PDP_AFTER_ATC,
                KEY_ACTION, valueActionPrefix + ProductTrackingConstant.Action.ACTION_WISHLIST_ON_PRODUCT_RECOMMENDATION + valueActionPostfix,
                KEY_LABEL, product.header
        )
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(enhanceEcommerceData)
    }


    fun sendAllQueue() {
        trackingQueue.sendAll()
    }

    fun eventClickWishlistOnAffiliate(userId: String,
                                      productId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
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
        params[KEY_USER_ID] = userId
        params[KEY_PRODUCT_ID] = productId
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

    fun eventSendChat(productId: String) {
        val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PRODUCT_PAGE.toLowerCase(),
                ProductTrackingConstant.Action.CLICK,
                ProductTrackingConstant.Message.LABEL.toLowerCase()
        )
        mapEvent[KEY_PRODUCT_ID] = productId
        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }

    fun eventClickPDPInstallmentSeeMore(productId: String?) {
        if (productId.isNullOrEmpty()) return
        val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.CLICK_LIHAT_SEMUA_ON_SIMULASI_CICILAN,
                ""
        )
        mapEvent[KEY_PRODUCT_ID] = productId
        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }

    fun eventPDPAddToWishlist(productId: String?) {
        if (productId.isNullOrEmpty()) return
        val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                "add wishlist",
                productId
        )
        mapEvent[KEY_PRODUCT_ID] = productId
        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }

    fun eventPDPAddToWishlistNonLogin(productId: String?) {
        if (productId.isNullOrEmpty()) return
        val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                "add wishlist - non logged in",
                productId
        )
        mapEvent[KEY_PRODUCT_ID] = productId
        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }

    fun eventPDPRemoveToWishlist(productId: String?) {
        if (productId.isNullOrEmpty()) return
        val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                "remove wishlist",
                productId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }

    fun eventClickReviewOnSeeAllImage(productId: Int) {
        val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.ImageReview.ACTION_SEE_ALL,
                productId.toString()
        )
        mapEvent[KEY_PRODUCT_ID] = productId
        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }

    fun eventClickReviewOnBuyersImage(productId: Int, reviewId: String?) {
        val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.ImageReview.ACTION_SEE_ITEM,
                "product_id: $productId - review_id : $reviewId"
        )
        mapEvent[KEY_PRODUCT_ID] = productId
        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }

    fun eventClickReviewOnMostHelpfulReview(productId: Int?, reviewId: String?) {
        val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                "click - review gallery on most helpful review",
                "product_id: $productId - review_id : $reviewId"
        )
        mapEvent[KEY_PRODUCT_ID] = productId ?: 0
        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }

    private fun getEnhanceCategoryFormatted(detail: List<Category.Detail>?): String {
        val list = ArrayList<String>()
        if (detail != null) {
            for (i in 0 until detail.size) {
                list.add(detail[i].name)
            }
        }
        return TextUtils.join("/", list)
    }

    private fun getEnhanceShopType(goldOS: ShopInfo.GoldOS?): String {
        return when {
            goldOS?.isOfficial == 1 -> "official_store"
            goldOS?.isGold == 1 -> "gold_merchant"
            else -> "regular"
        }
    }

    private fun getEnhanceUrl(url: String?): String? {
        if (!TextUtils.isEmpty(url)) {
            val uri = Uri.parse(url)
            return uri.lastPathSegment
        }
        return ""
    }

    fun eventEnhanceEcommerceProductDetail(trackerListName: String?, productInfo: ProductInfo?,
                                           shopInfo: ShopInfo?, trackerAttribution: String?,
                                           isTradeIn: Boolean, isDiagnosed: Boolean,
                                           multiOrigin: Boolean, deeplinkUrl: String) {
        val dimension55 = if (isTradeIn && isDiagnosed)
            "true diagnostic"
        else if (isTradeIn && !isDiagnosed)
            "true non diagnostic"
        else
            "false"

        val dimension83 = productInfo?.freeOngkir?.let {
            if (it.isFreeOngkirActive)
                VALUE_BEBAS_ONGKIR
            else
                VALUE_NONE_OTHER
        }

        val subCategoryId = productInfo?.category?.detail?.firstOrNull()?.id ?: ""
        val subCategoryName = productInfo?.category?.detail?.firstOrNull()?.name ?: ""

        val productImageUrl = productInfo?.media?.filter {
            it.type == "image"
        }?.firstOrNull()?.urlOriginal ?: ""

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(DataLayer.mapOf(
                "event", "viewProduct",
                "eventCategory", "product page",
                "eventAction", "view product page",
                "eventLabel", getEnhanceShopType(shopInfo?.goldOS) + " - " + shopInfo?.shopCore?.name + " - " + productInfo?.basic?.name,
                KEY_PRODUCT_ID, productInfo?.basic?.id,
                "ecommerce", DataLayer.mapOf(
                "currencyCode", "IDR",
                "detail", DataLayer.mapOf(
                "products", DataLayer.listOf(
                DataLayer.mapOf(
                        "name", productInfo?.basic?.name,
                        "id", productInfo?.basic?.id,
                        "price", productInfo?.basic?.price?.toInt(),
                        "brand", productInfo?.basic?.name,
                        "category", getEnhanceCategoryFormatted(productInfo?.category?.detail),
                        "variant", "none / other",
                        "dimension38", trackerAttribution ?: "none / other",
                        "dimension55", dimension55,
                        "dimension54", getMultiOriginAttribution(multiOrigin),
                        "dimension83", dimension83,
                        KEY_DIMENSION_81, shopInfo?.goldOS?.shopTypeString

                ))).apply {
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
                "shopType", getEnhanceShopType(shopInfo?.goldOS),
                "pageType", "/productpage",
                "subcategory", subCategoryName,
                "subcategoryId", subCategoryId,
                "productUrl", productInfo?.getProductUrl(),
                "productDeeplinkUrl", deeplinkUrl,
                "productImageUrl", productImageUrl,
                "isOfficialStore", shopInfo?.goldOS?.isOfficial,
                "productPriceFormatted", getFormattedPrice(productInfo?.basic?.price?.toInt() ?: 0))
        )
    }

    ///////////////////////////////////////////////////////////////
    //BRANCH START
    //////////////////////////////////////////////////////////////

    fun eventBranchItemView(productInfo: ProductInfo?, userId: String?) {
        if (productInfo != null) {
            LinkerManager.getInstance().sendEvent(LinkerUtils.createGenericRequest(LinkerConstants.EVENT_ITEM_VIEW, createLinkerData(productInfo, userId)))
        }
    }

    fun eventBranchAddToWishlist(productInfo: ProductInfo?, userId: String?) {
        if (productInfo != null) {
            LinkerManager.getInstance().sendEvent(LinkerUtils.createGenericRequest(LinkerConstants.EVENT_ADD_TO_WHISHLIST, createLinkerData(productInfo, userId)))
        }
    }

    private fun createLinkerData(productInfo: ProductInfo, userId: String?): LinkerData {
        val linkerData = LinkerData()
        linkerData.id = productInfo.basic.id.toString()
        linkerData.price = productInfo.basic.price.toInt().toString()
        linkerData.description = productInfo.basic.description
        linkerData.shopId = productInfo.basic.shopID.toString()
        linkerData.catLvl1 = productInfo.category.name
        linkerData.userId = userId ?: ""
        linkerData.currency = currencyLable
        return linkerData
    }

    ///////////////////////////////////////////////////////////////
    //BRANCH END
    //////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////
    // APPSFYLER START
    ////////////////////////////////////////////////////////////////
    fun eventPDPWishlistAppsFyler(productInfo: ProductInfo) {
        eventAppsFyler(productInfo, "af_add_to_wishlist")
    }

    fun eventAppsFylerOpenProduct(productInfo: ProductInfo) {
        eventAppsFyler(productInfo, "af_content_view")
    }

    private fun eventAppsFyler(productInfo: ProductInfo, eventName: String) {
        TrackApp.getInstance().appsFlyer.run {
            val mutableMap = mutableMapOf(
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
                        put("level" + i + "_name", productInfo.category.detail[size - i].name)
                        put("level" + i + "_id", productInfo.category.detail[size - i].id)
                    }
                }
                if ("af_content_view" == eventName) {
                    val jsonArray = JSONArray()
                    val jsonObject = JSONObject()
                    jsonObject.put("id", productInfo.basic.id.toString())
                    jsonObject.put("quantity", 1)
                    jsonArray.put(jsonObject)
                    this["af_content"] = jsonArray.toString()
                }
            }

            sendEvent(eventName, mutableMap)
        }
    }

    ////////////////////////////////////////////////////////////////
    // APPSFYLER END
    ////////////////////////////////////////////////////////////////


    ////////////////////////////////////////////////////////////////
    // MOENGAGE START
    ////////////////////////////////////////////////////////////////
    fun sendMoEngagePDPReferralCodeShareEvent() {
        TrackApp.getInstance().moEngage.sendEvent("Share_Event",
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
        TrackApp.getInstance().moEngage.sendEvent(
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
                    put("product_price", productInfo.basic.price.toInt())
                    put("product_price_fmt", getFormattedPrice(productInfo.basic.price.toInt()))
                    put("is_official_store", isOfficialStore)
                    put("shop_id", productInfo.basic.shopID)
                    put("shop_name", shopName)
                    if (productInfo.pictures?.isNotEmpty() == true) {
                        put("product_image_url", productInfo.pictures?.get(0)?.urlOriginal ?: "")
                    }
                }
        )
    }

    fun sendGeneralEvent(event: String, category: String, action: String, label: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(event,
                category,
                action,
                label)
    }

    ////////////////////////////////////////////////////////////////
    // MOENGAGE END
    ////////////////////////////////////////////////////////////////

    private fun getFormattedPrice(price: Int): String {
        return CurrencyFormatUtil.getThousandSeparatorString(price.toDouble(), false, 0).formattedString
    }

    private fun getMultiOriginAttribution(isMultiOrigin: Boolean): String = when (isMultiOrigin) {
        true -> "tokopedia"
        else -> "regular"
    }

    private fun removeCurrencyPrice(priceFormatted: String): String {
        return try {
            priceFormatted.replace("[^\\d]".toRegex(), "")
        } catch (t: Throwable) {
            "0"
        }
    }

    fun eventClickApplyLeasing(productId: String, isVariant: Boolean) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.CLICK_APPLY_LEASING,
                "$productId - $isVariant"
        )
    }

    fun eventViewHelpPopUpWhenAtc() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                ProductTrackingConstant.PDP.EVENT_VIEW_PDP,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.VIEW_HELP_POP_UP_WHEN_ATC,
                ProductTrackingConstant.Label.EMPTY_LABEL
        )
    }

    fun eventClickReportOnHelpPopUpAtc() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.CLICK_REPORT_ON_HELP_POP_UP_ATC,
                ProductTrackingConstant.Label.EMPTY_LABEL
        )
    }

    fun eventClickCloseOnHelpPopUpAtc() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.CLICK_CLOSE_ON_HELP_POP_UP_ATC,
                ProductTrackingConstant.Label.EMPTY_LABEL
        )
    }

    fun eventClickSearchBar() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                ProductTrackingConstant.PDP.EVENT_CLICK_TOP_NAV,
                ProductTrackingConstant.Category.TOP_NAV_SEARCH_PDP,
                ProductTrackingConstant.Action.CLICK_SEARCH_BOX,
                ""
        )
    }

    fun eventClickTradeInRibbon(productId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.CLICK_RIBBON_TRADE_IN,
                productId
        )
    }

    fun eventClickSeeMoreRecomWidget(widgetName: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                String.format(ProductTrackingConstant.Action.CLICK_SEE_MORE_WIDGET, widgetName),
                ""
        )
    }

    fun eventClickPdpShare(productId: String) {
        val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.TOP_NAV_SHARE_PDP,
                ProductTrackingConstant.Action.CLICK_SHARE_PDP,
                ""
        )
        mapEvent[KEY_PRODUCT_ID] = productId
        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }

    fun eventClickProductDescriptionReadMore(productId: String) {
        val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.CLICK_READ_MORE,
                ""
        )
        mapEvent[KEY_PRODUCT_ID] = productId
        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }

    fun eventClickDescriptionTabOnProductDescription(productId: String) {
        val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.CLICK_TAB_DESCRIPTION_ON_PRODUCT_DESCRIPTION,
                ""
        )
        mapEvent[KEY_PRODUCT_ID] = productId
        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }

    fun eventClickSpecificationTabOnProductDescription(productId: String) {
        val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.CLICK_TAB_SPECIFICATION_ON_PRODUCT_DESCRIPTION,
                ""
        )
        mapEvent[KEY_PRODUCT_ID] = productId
        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
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

        const val PRODUCT_DETAIL_SCREEN_NAME = "/product"

        private const val ID = "id"
        private const val PROMO_NAME = "name"
        private const val PROMO_POSITION = "position"
        private const val PROMO_ID = "promo_id"
        private const val PROMO_CODE = "promo_id"

        private const val ACTION_FIELD = "actionField"
        private const val LIST = "list"
        private const val PRODUCTS = "products"
        private const val IMPRESSIONS = "impressions"
        private const val PRICE = "price"
        private const val BRAND = "brand"
        private const val DEFAULT_VALUE = "none / other"
        private const val VARIANT = "variant"
        private const val CATEGORY = "category"
        private const val LIST_DEFAULT = "/product - "
        private const val LIST_RECOMMENDATION = " - rekomendasi untuk anda - "
        private const val LIST_PRODUCT_AFTER_ATC = "/productafteratc  - "
        private const val CURRENCY_CODE = "currencyCode"
        private const val CURRENCY_DEFAULT_VALUE = "IDR"
        private const val DATA_DIMENSION_83 = "dimension83"
        private const val VALUE_BEBAS_ONGKIR = "bebas ongkir"
        private const val VALUE_NONE_OTHER = "none / other"
        private const val KEY_PRODUCT_ID = "productId"
        private const val KEY_DIMENSION_81 = "dimension81"
    }
}
