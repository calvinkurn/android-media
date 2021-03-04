package com.tokopedia.product.detail.data.util

import android.net.Uri
import android.text.TextUtils
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.atc_common.domain.analytics.AddToCartBaseAnalytics
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.product.detail.common.data.model.pdplayout.DynamicProductInfoP1
import com.tokopedia.product.detail.common.data.model.product.Category
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.track.TrackApp
import com.tokopedia.unifycomponents.ticker.Ticker
import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by Yehezkiel on 2020-02-11
 */
object TrackingUtil {

    fun createMvcListMap(viewModelList: List<MerchantVoucherViewModel>, shopId: Int, startIndex: Int): List<Any> {
        val list = mutableListOf<Any>()
        for (i in viewModelList.indices) {
            val viewModel = viewModelList[i]
            val position = startIndex.plus(i).plus(1)
            if (viewModel.isAvailable()) {
                list.add(
                        DataLayer.mapOf(
                                ProductTrackingConstant.Tracking.ID, shopId.toString(),
                                ProductTrackingConstant.Tracking.PROMO_NAME, listOf(ProductTrackingConstant.Label.PDP, position.toString(), viewModel.voucherName).joinToString(" - "),
                                ProductTrackingConstant.Tracking.PROMO_POSITION, position,
                                ProductTrackingConstant.Tracking.PROMO_ID, viewModel.voucherId,
                                ProductTrackingConstant.Tracking.PROMO_CODE, viewModel.voucherCode
                        )
                )
            }
        }
        return list
    }

    fun getTickerTypeInfoString(tickerType:Int) : String {
        return when(tickerType){
            Ticker.TYPE_INFORMATION -> "info"
            Ticker.TYPE_WARNING -> "warning"
            else -> "other"
        }
    }

    fun createMVCMap(vouchers: List<MerchantVoucherViewModel>, shopId: String, position: Int): List<Any> {
        return vouchers.withIndex().filter { it.value.isAvailable() }.map {
            DataLayer.mapOf(
                    ProductTrackingConstant.Tracking.ID, shopId,
                    ProductTrackingConstant.Tracking.PROMO_NAME, listOf(ProductTrackingConstant.Label.PDP, (position + it.index + 1).toString(), it.value.voucherName).joinToString(" - "),
                    ProductTrackingConstant.Tracking.PROMO_POSITION, (position + it.index + 1).toString(),
                    ProductTrackingConstant.Tracking.PROMO_ID, it.value.voucherId,
                    ProductTrackingConstant.Tracking.PROMO_CODE, it.value.voucherCode
            )
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
                }).toString()
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

    fun addComponentTracker(mapEvent: MutableMap<String, Any>,
                            productInfo: DynamicProductInfoP1?,
                            componentTrackDataModel: ComponentTrackDataModel?,
                            elementName: String) {
        mapEvent[ProductTrackingConstant.Tracking.KEY_PRODUCT_ID] = productInfo?.basic?.productID
                ?: ""
        mapEvent[ProductTrackingConstant.Tracking.KEY_LAYOUT] = "layout:${productInfo?.layoutName};catName:${productInfo?.basic?.category?.name};catId:${productInfo?.basic?.category?.id};"

        if (componentTrackDataModel != null)
            mapEvent[ProductTrackingConstant.Tracking.KEY_COMPONENT] = "comp:${componentTrackDataModel.componentType};temp:${componentTrackDataModel.componentName};elem:${elementName};cpos:${componentTrackDataModel.adapterPosition};"
        else
            mapEvent[ProductTrackingConstant.Tracking.KEY_COMPONENT] = ""

        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
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
        val list = ArrayList<String>()
        if (detail != null) {
            for (i in 0 until detail.size) {
                list.add(detail[i].name)
            }
        }
        return TextUtils.join("/", list)
    }

    fun getFormattedPrice(price: Int): String {
        return CurrencyFormatUtil.getThousandSeparatorString(price.toDouble(), false, 0).formattedString
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