package com.tokopedia.product.detail.data.util

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.product.detail.common.ProductTrackingConstant
import com.tokopedia.product.detail.common.ProductTrackingConstant.Category.ITEM_CATEGORY_BUILDER
import com.tokopedia.product.detail.common.ProductTrackingConstant.Category.KEY_UNDEFINED
import com.tokopedia.product.detail.common.data.model.pdplayout.DynamicProductInfoP1
import com.tokopedia.product.detail.common.data.model.product.Category
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.tracking.TrackingConstant
import com.tokopedia.track.TrackApp
import com.tokopedia.track.constant.TrackerConstant
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.utils.currency.CurrencyFormatUtil
import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by Yehezkiel on 2020-02-11
 */
object TrackingUtil {

    fun sendTrackingBundle(key: String, bundle: Bundle) {
        TrackApp
            .getInstance()
            .gtm
            .sendEnhanceEcommerceEvent(
                key,
                bundle
            )
    }

    @SuppressLint("VisibleForTests")
    fun createCommonImpressionTracker(
        productInfo: DynamicProductInfoP1?,
        componentTrackDataModel: ComponentTrackDataModel,
        userId: String,
        lcaWarehouseId: String,
        customAction: String = "",
        customCreativeName: String = "",
        customItemName: String = "",
        customLabel: String = "",
        customPromoCode: String = "",
        customItemId: String = "",
        customPromoId: String = ""
    ): HashMap<String, Any>? {
        val productId = productInfo?.basic?.productID ?: ""
        val shopId = productInfo?.basic?.shopID ?: ""
        val shopType = productInfo?.shopTypeString ?: ""
        val listOfCategoryId = productInfo?.basic?.category?.detail
        val categoryName = productInfo?.basic?.category?.name.orEmpty()
        val categoryString = "${listOfCategoryId?.getOrNull(0)?.id.orEmpty()} / ${listOfCategoryId?.getOrNull(1)?.id.orEmpty()} / ${listOfCategoryId?.getOrNull(2)?.id.orEmpty()} / $categoryName "

        val mapEvent = DataLayer.mapOf(
            ProductTrackingConstant.Tracking.KEY_EVENT, "promoView",
            ProductTrackingConstant.Tracking.KEY_CATEGORY, ProductTrackingConstant.Category.PDP,
            ProductTrackingConstant.Tracking.KEY_ACTION, customAction,
            ProductTrackingConstant.Tracking.KEY_LABEL, customLabel,
            ProductTrackingConstant.Tracking.KEY_BUSINESS_UNIT, ProductTrackingConstant.Tracking.BUSINESS_UNIT_PDP,
            ProductTrackingConstant.Tracking.KEY_CURRENT_SITE, ProductTrackingConstant.Tracking.CURRENT_SITE,
            ProductTrackingConstant.Tracking.KEY_USER_ID_VARIANT, userId,
            "categoryId", "productId : $productId",
            ProductTrackingConstant.Tracking.KEY_ECOMMERCE,
            DataLayer.mapOf(
                "promoView",
                DataLayer.mapOf(
                    "promotions",
                    DataLayer.listOf(
                        DataLayer.mapOf(
                            "id", customItemId, // item_id
                            "name", customItemName, // item_name
                            "creative", customCreativeName, // creative_name
                            "creative_url", "",
                            "position", componentTrackDataModel.adapterPosition, // creative_slot
                            "category", categoryString,
                            "promo_id", customPromoId,
                            "promo_code", customPromoCode
                        )
                    )
                )
            )
        )
        mapEvent[ProductTrackingConstant.Tracking.KEY_PRODUCT_ID] = productInfo?.basic?.productID
            ?: ""
        mapEvent[ProductTrackingConstant.Tracking.KEY_WAREHOUSE_ID] = lcaWarehouseId
        mapEvent[ProductTrackingConstant.Tracking.KEY_LAYOUT] = "layout:${productInfo?.layoutName};catName:${productInfo?.basic?.category?.name};catId:${productInfo?.basic?.category?.id};"
        mapEvent[ProductTrackingConstant.Tracking.KEY_COMPONENT] = "comp:${componentTrackDataModel.componentName};temp:${componentTrackDataModel.componentType};elem:${"impression - modular component"};cpos:${componentTrackDataModel.adapterPosition};"
        mapEvent[ProductTrackingConstant.Tracking.KEY_PRODUCT_SHOP_ID] = shopId
        mapEvent[ProductTrackingConstant.Tracking.KEY_SHOP_TYPE] = shopType

        return mapEvent as HashMap<String, Any>?
    }

    fun getTradeInString(isTradein: Boolean, isDiagnosed: Boolean): String {
        return if (isTradein && isDiagnosed) {
            ProductTrackingConstant.Tracking.TRADEIN_TRUE_DIAGNOSTIC
        } else if (isTradein && !isDiagnosed) {
            ProductTrackingConstant.Tracking.TRADEIN_TRUE_NON_DIAGNOSTIC
        } else {
            ProductTrackingConstant.Tracking.VALUE_FALSE
        }
    }

    fun getProductFirstImageUrl(productInfo: DynamicProductInfoP1?): String {
        return productInfo?.data?.media?.filter {
            it.type == "image"
        }?.firstOrNull()?.uRLOriginal ?: ""
    }

    fun getProductViewLabel(productInfo: DynamicProductInfoP1?): String {
        return "${productInfo?.shopTypeString ?: ""} - ${productInfo?.basic?.shopName ?: ""} - ${productInfo?.data?.name ?: ""}"
    }

    fun getTickerTypeInfoString(tickerType: Int): String {
        return when (tickerType) {
            Ticker.TYPE_INFORMATION -> "info"
            Ticker.TYPE_WARNING -> "warning"
            else -> "other"
        }
    }

    fun createLinkerData(productInfo: DynamicProductInfoP1, userId: String?, description: String): LinkerData {
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

    fun createLinkerDataForViewItem(productInfo: DynamicProductInfoP1, userId: String?): LinkerData {
        val linkerData = LinkerData()
        linkerData.shopId = productInfo.basic.shopID
        linkerData.price = productInfo.finalPrice.toString()
        linkerData.productName = productInfo.getProductName
        linkerData.sku = productInfo.basic.productID
        linkerData.currency = ProductTrackingConstant.Tracking.CURRENCY_DEFAULT_VALUE
        productInfo.basic.category.detail.getOrNull(0)?.let {
            linkerData.level1Name = it.name
            linkerData.level1Id = it.id
        }
        linkerData.userId = userId ?: ""
        linkerData.content = JSONArray().put(
            JSONObject().apply {
                put(ProductTrackingConstant.Tracking.ID, productInfo.basic.productID)
                put(ProductTrackingConstant.Tracking.QUANTITY, productInfo.data.stock.value.toString())
            }
        ).toString()
        productInfo.basic.category.detail.getOrNull(1)?.let {
            linkerData.level2Name = it.name
            linkerData.level2Id = it.id
        }
        linkerData.contentId = productInfo.basic.productID
        linkerData.contentType = ProductTrackingConstant.Tracking.CONTENT_TYPE
        productInfo.basic.category.detail.getOrNull(2)?.let {
            linkerData.level3Name = it.name
            linkerData.level3Id = it.id
            linkerData.productCategory = it.name
        }
        linkerData.quantity = ProductTrackingConstant.Tracking.BRANCH_QUANTITY
        return linkerData
    }

    fun addComponentTracker(
        mapEvent: MutableMap<String, Any>,
        productInfo: DynamicProductInfoP1?,
        componentTrackDataModel: ComponentTrackDataModel?,
        elementName: String
    ) {
        mapEvent[ProductTrackingConstant.Tracking.KEY_PRODUCT_ID] = productInfo?.basic?.productID
            ?: ""
        mapEvent[ProductTrackingConstant.Tracking.KEY_LAYOUT] = "layout:${productInfo?.layoutName};catName:${productInfo?.basic?.category?.name};catId:${productInfo?.basic?.category?.id};"

        if (componentTrackDataModel != null) {
            mapEvent[ProductTrackingConstant.Tracking.KEY_COMPONENT] = "comp:${componentTrackDataModel.componentName};temp:${componentTrackDataModel.componentType};elem:$elementName;cpos:${componentTrackDataModel.adapterPosition};"
        } else {
            mapEvent[ProductTrackingConstant.Tracking.KEY_COMPONENT] = ""
        }

        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }

    fun addClickEvent(
        productInfo: DynamicProductInfoP1?,
        trackDataModel: ComponentTrackDataModel?,
        action: String,
        trackerId: String = "",
        eventLabel: String = "",
        modifier: ((MutableMap<String, Any>) -> Unit) = {}
    ) {
        val events = mutableMapOf<String, Any>(
            TrackerConstant.EVENT to ProductTrackingConstant.PDP.EVENT_CLICK_PG,
            TrackerConstant.EVENT_ACTION to action,
            TrackerConstant.EVENT_CATEGORY to ProductTrackingConstant.Category.PDP,
            TrackerConstant.EVENT_LABEL to eventLabel,
            TrackingConstant.Hit.TRACKER_ID to trackerId,
            TrackerConstant.BUSINESS_UNIT to ProductTrackingConstant.Category.PDP,
            TrackerConstant.CURRENT_SITE to ProductTrackingConstant.Tracking.CURRENT_SITE
        ).apply { modifier.invoke(this) }

        addComponentTracker(
            mapEvent = events,
            productInfo = productInfo,
            componentTrackDataModel = trackDataModel,
            elementName = action
        )
    }

    fun addComponentOvoTracker(mapEvent: MutableMap<String, Any>, productId: String, userId: String) {
        mapEvent[ProductTrackingConstant.Tracking.KEY_PRODUCT_ID] = productId
        mapEvent[ProductTrackingConstant.Tracking.KEY_USER_ID_VARIANT] = userId
        mapEvent[ProductTrackingConstant.Tracking.KEY_ISLOGGIN] = (userId != "0").toString()
        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }

    fun removeCurrencyPrice(priceFormatted: String): String {
        return try {
            priceFormatted.replace("[^\\d]".toRegex(), "")
        } catch (t: Throwable) {
            "0"
        }
    }

    fun getEnhanceUrl(url: String?): String? {
        if (!TextUtils.isEmpty(url)) {
            val uri = Uri.parse(url)
            return uri.lastPathSegment
        }
        return ""
    }

    fun getMultiOriginAttribution(isMultiOrigin: Boolean): String = when (isMultiOrigin) {
        true -> "tokopedia"
        else -> "regular"
    }

    fun getEnhanceCategoryFormatted(detail: List<Category.Detail>?): String {
        val categoryNameLvl1 = detail?.firstOrNull()?.name ?: KEY_UNDEFINED
        val categoryNameLvl2 = detail?.getOrNull(1)?.name ?: KEY_UNDEFINED
        val categoryNameLvl3 = detail?.getOrNull(2)?.name ?: KEY_UNDEFINED
        return String.format(ITEM_CATEGORY_BUILDER, categoryNameLvl1, categoryNameLvl2, categoryNameLvl3, KEY_UNDEFINED)
    }

    fun getFormattedPrice(price: Double): String {
        return CurrencyFormatUtil.getThousandSeparatorString(price, false, 0).formattedString
    }

    fun addDiscussionParams(mapEvent: MutableMap<String, Any>, userId: String): MutableMap<String, Any> {
        with(ProductTrackingConstant.Tracking) {
            mapEvent.putAll(
                mapOf(
                    KEY_BUSINESS_UNIT to BUSINESS_UNIT,
                    KEY_CURRENT_SITE to CURRENT_SITE,
                    KEY_DISCUSSION_USER_ID to userId,
                    KEY_SCREEN_NAME to PRODUCT_DETAIL_SCREEN_NAME
                )
            )
        }
        return mapEvent
    }
}
