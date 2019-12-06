package com.tokopedia.product.detail.data.util

import android.net.Uri
import android.text.TextUtils
import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.linker.LinkerConstants
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.product.detail.common.data.model.pdplayout.DynamicProductInfoP1
import com.tokopedia.product.detail.common.data.model.product.Category
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.track.TrackApp
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

class DynamicProductDetailTracking @Inject constructor() {

    private val screenName = ProductTrackingConstant.Tracking.PRODUCT_DETAIL_SCREEN_NAME

    fun eventBranchItemView(productInfo: DynamicProductInfoP1?, userId: String?, description: String) {
        if (productInfo != null) {
            LinkerManager.getInstance().sendEvent(LinkerUtils.createGenericRequest(LinkerConstants.EVENT_ITEM_VIEW, createLinkerData(productInfo, userId, description)))
        }
    }

    fun sendScreen(shopID: String, shopType: String, productId: String) {
        TrackApp.getInstance().gtm.sendScreenAuthenticated(screenName, shopID,
                shopType, "/product", productId)
    }

    fun sendMoEngageClickReview(productInfo: DynamicProductInfoP1, shopName: String) {
        sendMoEngage(productInfo, shopName, "Clicked_Ulasan_Pdp")
    }

    fun sendMoEngageOpenProduct(productInfo: DynamicProductInfoP1, shopName: String) {
        sendMoEngage(productInfo, shopName, "Product_Page_Opened")
    }

    fun sendMoEngageClickDiskusi(productInfo: DynamicProductInfoP1, shopName: String) {
        sendMoEngage(productInfo, shopName, "Clicked_Diskusi_Pdp")
    }

    fun eventPDPWishlistAppsFyler(productInfo: DynamicProductInfoP1) {
        eventAppsFyler(productInfo, "af_add_to_wishlist")
    }

    fun eventAppsFylerOpenProduct(productInfo: DynamicProductInfoP1) {
        eventAppsFyler(productInfo, "af_content_view")
    }

    fun eventBranchAddToWishlist(productInfo: DynamicProductInfoP1?, userId: String?, description: String) {
        if (productInfo != null) {
            LinkerManager.getInstance().sendEvent(LinkerUtils.createGenericRequest(LinkerConstants.EVENT_ADD_TO_WHISHLIST, createLinkerData(productInfo, userId, description)))
        }
    }

    fun eventDiscussionClickedIris(productInfo: DynamicProductInfoP1?, deeplinkUrl: String,
                                   shopName: String) {

        var categoryNameLvl2 = ""
        var categoryIdLvl2 = ""
        if (productInfo?.basic?.category?.detail?.size ?: 0 >= 2) {
            productInfo?.basic?.category?.detail?.get(1)?.let {
                categoryIdLvl2 = it.id
                categoryNameLvl2 = it.name
            }
        }

        val imageUrl = productInfo?.data?.media?.filter {
            it.type == "image"
        }?.firstOrNull()?.uRLOriginal ?: ""

        val mapOfData: Map<String, Any?> =
                mapOf(ProductTrackingConstant.Tracking.KEY_EVENT to "clickPDP",
                        ProductTrackingConstant.Tracking.KEY_CATEGORY to "product detail page",
                        ProductTrackingConstant.Tracking.KEY_ACTION to "Click",
                        ProductTrackingConstant.Tracking.KEY_LABEL to "Talk",
                        "subcategory" to categoryNameLvl2,
                        "subcategoryId" to categoryIdLvl2,
                        "category" to productInfo?.basic?.category?.name,
                        "categoryId" to productInfo?.basic?.category?.id,
                        "productName" to productInfo?.data?.name,
                        "productId" to productInfo?.basic?.getProductId(),
                        "productUrl" to productInfo?.basic?.url,
                        "productDepplinkUrl" to deeplinkUrl,
                        "productImageUrl" to imageUrl,
                        "productPrice" to productInfo?.data?.price?.value,
                        "isOfficialStore" to productInfo?.data?.isOS,
                        "shopId" to productInfo?.basic?.shopID,
                        "shopName" to shopName,
                        "productPriceFormatted" to getFormattedPrice(productInfo?.data?.price?.value
                                ?: 0))

        TrackApp.getInstance().gtm.sendGeneralEvent(mapOfData)
    }

    fun eventReviewClickedIris(productInfo: DynamicProductInfoP1?, deeplinkUrl: String,
                               shopName: String) {

        var categoryNameLvl2 = ""
        var categoryIdLvl2 = ""
        if (productInfo?.basic?.category?.detail?.size ?: 0 >= 2) {
            productInfo?.basic?.category?.detail?.get(1)?.let {
                categoryIdLvl2 = it.id
                categoryNameLvl2 = it.name
            }
        }

        val imageUrl = productInfo?.data?.media?.filter {
            it.type == "image"
        }?.firstOrNull()?.uRLOriginal ?: ""

        val mapOfData: Map<String, Any?> = mapOf(ProductTrackingConstant.Tracking.KEY_EVENT to "clickPDP",
                ProductTrackingConstant.Tracking.KEY_CATEGORY to "product detail page",
                ProductTrackingConstant.Tracking.KEY_ACTION to "click",
                ProductTrackingConstant.Tracking.KEY_LABEL to "review",
                "subcategory" to categoryNameLvl2,
                "subcategoryId" to categoryIdLvl2,
                "category" to productInfo?.basic?.category?.name,
                "categoryId" to productInfo?.basic?.category?.id,
                "productName" to productInfo?.data?.name,
                "productId" to productInfo?.basic?.getProductId(),
                "productUrl" to productInfo?.basic?.url,
                "productDepplinkUrl" to deeplinkUrl,
                "productImageUrl" to imageUrl,
                "productPrice" to productInfo?.data?.price?.value,
                "isOfficialStore" to productInfo?.data?.isOS,
                "shopId" to productInfo?.basic?.shopID,
                "shopName" to shopName,
                "productPriceFormatted" to getFormattedPrice(productInfo?.data?.price?.value ?: 0)
        )

        TrackApp.getInstance().gtm.sendGeneralEvent(mapOfData)
    }

    private fun createLinkerData(productInfo: DynamicProductInfoP1, userId: String?, description: String): LinkerData {
        val linkerData = LinkerData()
        linkerData.id = productInfo.basic.productID
        linkerData.price = productInfo.data.price.value.toString()
        linkerData.description = description
        linkerData.shopId = productInfo.basic.shopID
        linkerData.catLvl1 = productInfo.basic.category.name
        linkerData.userId = userId ?: ""
        linkerData.currency = ProductTrackingConstant.Tracking.CURRENCY_DEFAULT_VALUE
        return linkerData
    }

    private fun sendMoEngage(productInfo: DynamicProductInfoP1,
                             shopName: String,
                             eventName: String) {

        productInfo.run {
            basic.category.breadcrumbUrl

            TrackApp.getInstance().moEngage.sendEvent(
                    eventName,
                    mutableMapOf<String, Any>().apply {
                        if (basic.category.detail.isNotEmpty()) {
                            put("category", basic.category.detail[0].name)
                            put("category_id", basic.category.detail[0].id)
                        }
                        if (basic.category.detail.size > 1) {
                            put("subcategory", basic.category.detail[1].name)
                            put("subcategory_id", basic.category.detail[1].id)
                        }
                        put("product_name", getProductName)
                        put("product_id", basic.getProductId())
                        put("product_url", basic.url)
                        put("product_price", data.price.value)
                        put("product_price_fmt", getFormattedPrice(data.price.value))
                        put("is_official_store", data.isOS)
                        put("shop_id", productInfo.basic.shopID)
                        put("shop_name", shopName)
                        if (data.pictures.isNotEmpty()) {
                            put("product_image_url", data.pictures.get(0).urlOriginal)
                        }
                    }
            )
        }
    }

    private fun eventAppsFyler(productInfo: DynamicProductInfoP1, eventName: String) {
        TrackApp.getInstance().appsFlyer.run {
            productInfo.let {
                val mutableMap = mutableMapOf(
                        "af_description" to "productView",
                        "af_content_id" to it.basic.getProductId(),
                        "af_content_type" to "product",
                        "af_price" to it.data.price.value,
                        "af_currency" to "IDR",
                        "af_quantity" to 1.toString()
                ).apply {
                    if (it.basic.category.detail.isNotEmpty()) {
                        val size = it.basic.category.detail.size
                        for (i in 1..size) {
                            put("level" + i + "_name", it.basic.category.detail[size - i].name)
                            put("level" + i + "_id", it.basic.category.detail[size - i].id)
                        }
                    }
                    if ("af_content_view" == eventName) {
                        val jsonArray = JSONArray()
                        val jsonObject = JSONObject()
                        jsonObject.put("id", it.basic.getProductId())
                        jsonObject.put("quantity", 1)
                        jsonArray.put(jsonObject)
                        this["af_content"] = jsonArray.toString()
                    }
                }

                sendEvent(eventName, mutableMap as Map<String, Any>?)
            }
        }
    }


    fun eventEnhanceEcommerceProductDetail(trackerListName: String?, productInfo: DynamicProductInfoP1?,
                                           shopInfo: ShopInfo?, trackerAttribution: String?,
                                           isTradeIn: Boolean, isDiagnosed: Boolean,
                                           multiOrigin: Boolean, deeplinkUrl: String) {
        val dimension55 = if (isTradeIn && isDiagnosed)
            "true diagnostic"
        else if (isTradeIn && !isDiagnosed)
            "true non diagnostic"
        else
            "false"

        val dimension83 = productInfo?.data?.isFreeOngkir?.let {
            if (it.isActive)
                ProductTrackingConstant.Tracking.VALUE_BEBAS_ONGKIR
            else
                ProductTrackingConstant.Tracking.VALUE_NONE_OTHER
        }

        val subCategoryId = productInfo?.basic?.category?.detail?.firstOrNull()?.id ?: ""
        val subCategoryName = productInfo?.basic?.category?.detail?.firstOrNull()?.name ?: ""

        val productImageUrl = productInfo?.data?.media?.filter {
            it.type == "image"
        }?.firstOrNull()?.uRLOriginal ?: ""

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(DataLayer.mapOf(
                ProductTrackingConstant.Tracking.KEY_EVENT, "viewProduct",
                ProductTrackingConstant.Tracking.KEY_CATEGORY, "product page",
                ProductTrackingConstant.Tracking.KEY_ACTION, "view product page",
                ProductTrackingConstant.Tracking.KEY_LABEL, getEnhanceShopType(shopInfo?.goldOS) + " - " + shopInfo?.shopCore?.name + " - " + productInfo?.data?.name,
                ProductTrackingConstant.Tracking.KEY_PRODUCT_ID, productInfo?.basic?.getProductId(),
                ProductTrackingConstant.Tracking.KEY_ECOMMERCE, DataLayer.mapOf(
                ProductTrackingConstant.Tracking.CURRENCY_CODE, ProductTrackingConstant.Tracking.CURRENCY_DEFAULT_VALUE,
                ProductTrackingConstant.Tracking.KEY_DETAIl, DataLayer.mapOf(
                ProductTrackingConstant.Tracking.PRODUCTS, DataLayer.listOf(
                DataLayer.mapOf(
                        ProductTrackingConstant.Tracking.NAME, productInfo?.getProductName,
                        ProductTrackingConstant.Tracking.ID, productInfo?.basic?.getProductId(),
                        ProductTrackingConstant.Tracking.PRICE, productInfo?.data?.price?.value,
                        ProductTrackingConstant.Tracking.BRAND, productInfo?.getProductName,
                        ProductTrackingConstant.Tracking.CATEGORY, getEnhanceCategoryFormatted(productInfo?.basic?.category?.detail),
                        ProductTrackingConstant.Tracking.VARIANT, ProductTrackingConstant.Tracking.DEFAULT_VALUE,
                        ProductTrackingConstant.Tracking.KEY_DIMENSION_38, trackerAttribution
                        ?: ProductTrackingConstant.Tracking.DEFAULT_VALUE,
                        ProductTrackingConstant.Tracking.KEY_DIMENSION_55, dimension55,
                        ProductTrackingConstant.Tracking.KEY_DIMENSION_54, getMultiOriginAttribution(multiOrigin),
                        ProductTrackingConstant.Tracking.KEY_DIMENSION_83, dimension83,
                        ProductTrackingConstant.Tracking.KEY_DIMENSION_81, shopInfo?.goldOS?.shopTypeString

                ))).apply {
            if (trackerListName?.isNotEmpty() == true) {
                put(ProductTrackingConstant.Tracking.ACTION_FIELD, DataLayer.mapOf(ProductTrackingConstant.Tracking.LIST, trackerListName))
            }
        }),
                "key", getEnhanceUrl(productInfo?.basic?.url),
                "shopName", shopInfo?.shopCore?.name,
                "shopId", productInfo?.basic?.shopID,
                "shopDomain", shopInfo?.shopCore?.domain,
                "shopLocation", shopInfo?.location,
                "shopIsGold", shopInfo?.goldOS?.isGoldBadge.toString(),
                "categoryId", productInfo?.basic?.category?.id,
                "shopType", getEnhanceShopType(shopInfo?.goldOS),
                "pageType", "/productpage",
                "subcategory", subCategoryName,
                "subcategoryId", subCategoryId,
                "productUrl", productInfo?.basic?.url,
                "productDeeplinkUrl", deeplinkUrl,
                "productImageUrl", productImageUrl,
                "isOfficialStore", shopInfo?.goldOS?.isOfficial,
                "productPriceFormatted", getFormattedPrice(productInfo?.data?.price?.value ?: 0))
        )
    }

    private fun getEnhanceUrl(url: String?): String? {
        if (!TextUtils.isEmpty(url)) {
            val uri = Uri.parse(url)
            return uri.lastPathSegment
        }
        return ""
    }

    private fun getMultiOriginAttribution(isMultiOrigin: Boolean): String = when (isMultiOrigin) {
        true -> "tokopedia"
        else -> "regular"
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

    private fun getFormattedPrice(price: Int): String {
        return CurrencyFormatUtil.getThousandSeparatorString(price.toDouble(), false, 0).formattedString
    }
}