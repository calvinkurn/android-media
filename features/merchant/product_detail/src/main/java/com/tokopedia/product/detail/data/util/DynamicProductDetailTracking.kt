package com.tokopedia.product.detail.data.util

import android.os.Bundle
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.iris.util.KEY_SESSION_IRIS
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.linker.LinkerConstants
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.product.detail.common.ProductCartHelper
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.common.ProductTrackingConstant
import com.tokopedia.product.detail.common.ProductTrackingConstant.Action.CLICK_ANNOTATION_RECOM_CHIP
import com.tokopedia.product.detail.common.ProductTrackingConstant.TrackerId.TRACKER_ID_CLICK_THUMBNAIL
import com.tokopedia.product.detail.common.ProductTrackingConstant.TrackerId.TRACKER_ID_IMPRESS_THUMBNAIL
import com.tokopedia.product.detail.common.data.model.pdplayout.DynamicProductInfoP1
import com.tokopedia.product.detail.common.data.model.rates.P2RatesEstimateData
import com.tokopedia.product.detail.common.data.model.re.RestrictionAction
import com.tokopedia.product.detail.common.data.model.re.RestrictionData
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.DynamicPdpDataModel
import com.tokopedia.product.detail.data.model.datamodel.MediaDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductRecomLayoutBasicData
import com.tokopedia.product.detail.data.model.datamodel.ProductSingleVariantDataModel
import com.tokopedia.product.detail.data.util.TrackingUtil.removeCurrencyPrice
import com.tokopedia.product.detail.data.util.TrackingUtil.sendTrackingBundle
import com.tokopedia.product.util.processor.Product
import com.tokopedia.product.util.processor.ProductDetailViewsBundler
import com.tokopedia.recommendation_widget_common.extension.hasLabelGroupFulfillment
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.trackingoptimizer.TrackingQueue
import org.json.JSONArray
import org.json.JSONObject
import java.util.ArrayList
import java.util.Locale
import kotlin.collections.HashMap

object DynamicProductDetailTracking {

    fun sendScreen(irisSessionId: String, shopID: String, shopType: String, productId: String) {
        val customDimension: MutableMap<String, String> = java.util.HashMap()
        customDimension[ProductTrackingConstant.Tracking.KEY_SHOP_ID_SELLER] = shopID
        customDimension[ProductTrackingConstant.Tracking.KEY_PAGE_TYPE] = "/product"
        customDimension[ProductTrackingConstant.Tracking.KEY_SHOP_TYPE] = shopType
        customDimension[ProductTrackingConstant.Tracking.KEY_PRODUCT_ID_] = productId
        customDimension[KEY_SESSION_IRIS] = irisSessionId

        TrackApp.getInstance().gtm.sendScreenAuthenticated(ProductTrackingConstant.Tracking.PRODUCT_DETAIL_SCREEN_NAME, customDimension)
    }

    fun generateComponentTrackModel(data: DynamicPdpDataModel?, position: Int): ComponentTrackDataModel {
        return ComponentTrackDataModel(data?.type() ?: "", data?.name() ?: "", position)
    }

    fun generateVariantString(variant: ProductVariant?, selectedProductId: String): String {
        return variant?.getOptionListString(selectedProductId)?.map {
            it
        }?.joinToString(",") ?: ""
    }

    object Click {
        fun onInformationIconMultiLocClicked(
            productInfo: DynamicProductInfoP1?,
            componentTrackDataModel: ComponentTrackDataModel,
            userId: String
        ) {
            val shopId = productInfo?.basic?.shopID ?: ""

            val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PG,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.CLICK_INFO_MULTILOC,
                "shop_id:$shopId"
            )
            mapEvent[ProductTrackingConstant.Tracking.KEY_BUSINESS_UNIT] = ProductTrackingConstant.Tracking.BUSINESS_UNIT_PDP
            mapEvent[ProductTrackingConstant.Tracking.KEY_CURRENT_SITE] = ProductTrackingConstant.Tracking.CURRENT_SITE
            mapEvent[ProductTrackingConstant.Tracking.KEY_SHOP_TYPE] = productInfo?.shopTypeString.orEmpty()
            mapEvent[ProductTrackingConstant.Tracking.KEY_HIT_USER_ID] = userId
            mapEvent[ProductTrackingConstant.Tracking.KEY_SHOP_ID_SELLER] = shopId

            TrackingUtil.addComponentTracker(mapEvent, productInfo, componentTrackDataModel, ProductTrackingConstant.Action.CLICK_CATEGORY_IMAGE)
        }

        fun onImageCategoryCarouselClicked(productInfo: DynamicProductInfoP1?, componentTrackDataModel: ComponentTrackDataModel?, categoryName: String, categoryId: String) {
            val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.CLICK_CATEGORY_IMAGE,
                String.format(ProductTrackingConstant.Label.EVENT_LABEL_CLICK_IMAGE_CATEGORY_CAROUSEL, categoryId, categoryName)
            )
            mapEvent[ProductTrackingConstant.Tracking.KEY_BUSINESS_UNIT] = ProductTrackingConstant.Tracking.BUSINESS_UNIT_PDP
            mapEvent[ProductTrackingConstant.Tracking.KEY_CURRENT_SITE] = ProductTrackingConstant.Tracking.CURRENT_SITE

            TrackingUtil.addComponentTracker(mapEvent, productInfo, componentTrackDataModel, ProductTrackingConstant.Action.CLICK_CATEGORY_IMAGE)
        }

        fun onSeeAllCategoryCarouselClicked(productInfo: DynamicProductInfoP1?, componentTrackDataModel: ComponentTrackDataModel?) {
            val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.CLICK_SEE_ALL_CATEGORY_CAROUSEL,
                ""
            )
            mapEvent[ProductTrackingConstant.Tracking.KEY_BUSINESS_UNIT] = ProductTrackingConstant.Tracking.BUSINESS_UNIT_PDP
            mapEvent[ProductTrackingConstant.Tracking.KEY_CURRENT_SITE] = ProductTrackingConstant.Tracking.CURRENT_SITE

            TrackingUtil.addComponentTracker(mapEvent, productInfo, componentTrackDataModel, ProductTrackingConstant.Action.CLICK_SEE_ALL_CATEGORY_CAROUSEL)
        }

        fun onQuantityEditorClicked(productId: String, oldQuantity: Int, newQuantity: Int) {
            val label = "quantity button:${if (newQuantity > oldQuantity) "plus" else "minus"}"
            val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.CLICK_VARIANT_QUANTITY_EDITOR,
                label
            )

            mapEvent[ProductTrackingConstant.Tracking.KEY_BUSINESS_UNIT] = ProductTrackingConstant.Tracking.BUSINESS_UNIT_PDP
            mapEvent[ProductTrackingConstant.Tracking.KEY_CURRENT_SITE] = ProductTrackingConstant.Tracking.CURRENT_SITE
            mapEvent[ProductTrackingConstant.Tracking.KEY_PRODUCT_ID] = productId

            TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
        }

        fun eventClickShipmentErrorComponent(productInfo: DynamicProductInfoP1?, userId: String, title: String, componentTrackDataModel: ComponentTrackDataModel?) {
            val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.CLICK_SHIPMENT_ERROR_COMPONENT,
                String.format(ProductTrackingConstant.Label.EVENT_LABEL_CLICK_SHIPMENT_ERROR, title)
            )
            mapEvent[ProductTrackingConstant.Tracking.KEY_BUSINESS_UNIT] = ProductTrackingConstant.Tracking.BUSINESS_UNIT_PDP
            mapEvent[ProductTrackingConstant.Tracking.KEY_CURRENT_SITE] = ProductTrackingConstant.Tracking.CURRENT_SITE
            mapEvent[ProductTrackingConstant.Tracking.KEY_USER_ID_VARIANT] = userId
            mapEvent[ProductTrackingConstant.Tracking.KEY_SHOP_ID_SELLER] = productInfo?.basic?.shopID.orEmpty()

            TrackingUtil.addComponentTracker(mapEvent, productInfo, componentTrackDataModel, ProductTrackingConstant.Action.CLICK_SHIPMENT_ERROR_COMPONENT)
        }

        fun eventClickShipment(
            productInfo: DynamicProductInfoP1?,
            userId: String,
            componentTrackDataModel: ComponentTrackDataModel?,
            title: String,
            chipsLabel: List<String>,
            isCod: Boolean
        ) {
            val chipsLabelFormat = chipsLabel.joinToString(",")
            val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.CLICK_SEE_OTHER_COURIER,
                String.format(ProductTrackingConstant.Label.EVENT_LABEL_CLICK_SHIPMENT, title, chipsLabelFormat, isCod)
            )

            mapEvent[ProductTrackingConstant.Tracking.KEY_BUSINESS_UNIT] = ProductTrackingConstant.Tracking.BUSINESS_UNIT_PDP
            mapEvent[ProductTrackingConstant.Tracking.KEY_CURRENT_SITE] = ProductTrackingConstant.Tracking.CURRENT_SITE
            mapEvent[ProductTrackingConstant.Tracking.KEY_USER_ID_VARIANT] = userId
            mapEvent[ProductTrackingConstant.Tracking.KEY_SHOP_ID_SELLER] = productInfo?.basic?.shopID.orEmpty()

            TrackingUtil.addComponentTracker(mapEvent, productInfo, componentTrackDataModel, ProductTrackingConstant.Action.CLICK_SEE_OTHER_COURIER)
        }

        fun eventClickVideoVolume(
            productInfo: DynamicProductInfoP1?,
            userId: String,
            componentTrackDataModel: ComponentTrackDataModel?,
            isMute: Boolean
        ) {
            val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.CLICK_MUTE_VIDEO,
                ProductTrackingConstant.Label.VIDEO_STATE + isMute.toString()
            )

            mapEvent[ProductTrackingConstant.Tracking.KEY_USER_ID_VARIANT] = userId
            mapEvent[ProductTrackingConstant.Tracking.KEY_ISLOGGIN] = (userId.isNotEmpty()).toString()
            mapEvent[ProductTrackingConstant.Tracking.KEY_SHOP_TYPE] = productInfo?.shopTypeString.orEmpty()
            mapEvent[ProductTrackingConstant.Tracking.KEY_SHOP_ID_SELLER] = productInfo?.basic?.shopID.orEmpty()

            TrackingUtil.addComponentTracker(mapEvent, productInfo, componentTrackDataModel, ProductTrackingConstant.Action.CLICK_MUTE_VIDEO)
        }

        fun eventVideoStateChange(
            productInfo: DynamicProductInfoP1?,
            userId: String,
            componentTrackDataModel: ComponentTrackDataModel?,
            stopTime: Long,
            videoDuration: Long,
            isAutoPlay: Boolean
        ) {
            val eventLabel = ProductTrackingConstant.Label.VIDEO_DURATION + videoDuration.toString() + ";" +
                ProductTrackingConstant.Label.VIDEO_LAST_STOP_TIME + stopTime.toString() + ";" +
                ProductTrackingConstant.Label.VIDEO_AUTO_PLAY + isAutoPlay.toString()

            val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.CLICK_INTERACTION_VIDEO,
                eventLabel
            )

            mapEvent[ProductTrackingConstant.Tracking.KEY_USER_ID_VARIANT] = userId
            mapEvent[ProductTrackingConstant.Tracking.KEY_ISLOGGIN] = (userId.isNotEmpty()).toString()
            mapEvent[ProductTrackingConstant.Tracking.KEY_SHOP_TYPE] = productInfo?.shopTypeString.orEmpty()
            mapEvent[ProductTrackingConstant.Tracking.KEY_SHOP_ID_SELLER] = productInfo?.basic?.shopID.orEmpty()

            TrackingUtil.addComponentTracker(mapEvent, productInfo, componentTrackDataModel, ProductTrackingConstant.Action.CLICK_FULLSCREEN_VIDEO)
        }

        fun eventClickFullScreenVideo(
            productInfo: DynamicProductInfoP1?,
            userId: String,
            componentTrackDataModel: ComponentTrackDataModel?
        ) {
            val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.CLICK_FULLSCREEN_VIDEO,
                ProductTrackingConstant.Label.VIDEO_STATE + true.toString()
            )

            mapEvent[ProductTrackingConstant.Tracking.KEY_USER_ID_VARIANT] = userId
            mapEvent[ProductTrackingConstant.Tracking.KEY_ISLOGGIN] = (userId.isNotEmpty()).toString()
            mapEvent[ProductTrackingConstant.Tracking.KEY_SHOP_TYPE] = productInfo?.shopTypeString.orEmpty()
            mapEvent[ProductTrackingConstant.Tracking.KEY_SHOP_ID_SELLER] = productInfo?.basic?.shopID.orEmpty()

            TrackingUtil.addComponentTracker(mapEvent, productInfo, componentTrackDataModel, ProductTrackingConstant.Action.CLICK_FULLSCREEN_VIDEO)
        }

        fun eventClickReportFromComponent(productInfo: DynamicProductInfoP1?, userId: String, componentTrackDataModel: ComponentTrackDataModel?) {
            val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.CLICK_REPORT_FROM_COMPONENT,
                ""
            )
            mapEvent[ProductTrackingConstant.Tracking.KEY_USER_ID_VARIANT] = userId
            mapEvent[ProductTrackingConstant.Tracking.KEY_ISLOGGIN] = (userId.isNotEmpty()).toString()
            mapEvent[ProductTrackingConstant.Tracking.KEY_SHOP_TYPE] = productInfo?.shopTypeString.orEmpty()
            mapEvent[ProductTrackingConstant.Tracking.KEY_SHOP_ID_SELLER] = productInfo?.basic?.shopID.orEmpty()

            TrackingUtil.addComponentTracker(mapEvent, productInfo, componentTrackDataModel, ProductTrackingConstant.Action.CLICK_REPORT_FROM_COMPONENT)
        }

        fun eventClickFollowNpl(productInfo: DynamicProductInfoP1?, userId: String) {
            val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.CLICK_NPL_FOLLOWERS,
                productInfo?.basic?.shopID ?: ""
            )
            mapEvent[ProductTrackingConstant.Tracking.KEY_BUSINESS_UNIT] = ProductTrackingConstant.Tracking.BUSINESS_UNIT
            mapEvent[ProductTrackingConstant.Tracking.KEY_CURRENT_SITE] = ProductTrackingConstant.Tracking.CURRENT_SITE
            mapEvent[ProductTrackingConstant.Tracking.KEY_USER_ID_VARIANT] = userId

            TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
        }

        fun eventClickCustomInfo(title: String, userId: String, productInfo: DynamicProductInfoP1?, componentTrackDataModel: ComponentTrackDataModel?) {
            val componentTitle = "component title: $title"
            val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.CLICK_CUSTOM_INFO,
                componentTitle
            )
            mapEvent[ProductTrackingConstant.Tracking.KEY_USER_ID_VARIANT] = userId
            mapEvent[ProductTrackingConstant.Tracking.KEY_ISLOGGIN] = (userId.isNotEmpty()).toString()
            mapEvent[ProductTrackingConstant.Tracking.KEY_SHOP_TYPE] = productInfo?.shopTypeString.orEmpty()
            mapEvent[ProductTrackingConstant.Tracking.KEY_SHOP_ID_SELLER] = productInfo?.basic?.shopID.orEmpty()

            TrackingUtil.addComponentTracker(mapEvent, productInfo, componentTrackDataModel, ProductTrackingConstant.Action.CLICK_CUSTOM_INFO)
        }

        fun eventClickTicker(tickerTitle: String, tickerType: Int, productInfo: DynamicProductInfoP1?, componentTrackDataModel: ComponentTrackDataModel?, userId: String, tickerMessage: String) {
            val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.CLICK_TICKER,
                "${ProductTrackingConstant.Tracking.KEY_TICKER_TYPE}:${TrackingUtil.getTickerTypeInfoString(tickerType)};ticker title:$tickerTitle;ticker message: $tickerMessage;"
            )
            mapEvent[ProductTrackingConstant.Tracking.KEY_USER_ID_VARIANT] = userId
            mapEvent[ProductTrackingConstant.Tracking.KEY_SHOP_ID_SELLER] = productInfo?.basic?.shopID.orEmpty()
            mapEvent[ProductTrackingConstant.Tracking.KEY_SHOP_TYPE] = productInfo?.shopTypeString.orEmpty()
            mapEvent[ProductTrackingConstant.Tracking.KEY_ISLOGGIN] = (userId.isNotEmpty()).toString()

            TrackingUtil.addComponentTracker(mapEvent, productInfo, componentTrackDataModel, ProductTrackingConstant.Action.CLICK_TICKER)
        }

        fun eventClickWholesale(productInfo: DynamicProductInfoP1?, componentTrackDataModel: ComponentTrackDataModel?) {
            val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.CLICK_WHOLESALE,
                ""
            )

            TrackingUtil.addComponentTracker(mapEvent, productInfo, componentTrackDataModel, ProductTrackingConstant.Action.CLICK_WHOLESALE)
        }

        fun eventClickButtonNonLogin(actionButton: Int, productInfo: DynamicProductInfoP1?, userId: String, shopType: String, buttonActionText: String) {
            if (actionButton == ProductDetailCommonConstant.ATC_BUTTON) {
                eventAtcButtonNonLogin(productInfo, userId, shopType)
            } else {
                eventBuyButtonNonLogin(productInfo, userId, shopType, buttonActionText)
            }
        }

        private fun eventAtcButtonNonLogin(productInfo: DynamicProductInfoP1?, userId: String, shopType: String) {
            val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.CLICK_ATC_NON_LOGIN,
                ""
            )
            mapEvent[ProductTrackingConstant.Tracking.KEY_USER_ID] = userId
            mapEvent[ProductTrackingConstant.Tracking.KEY_SHOP_ID_SELLER] = productInfo?.basic?.shopID.orEmpty()
            mapEvent[ProductTrackingConstant.Tracking.KEY_SHOP_TYPE] = shopType
            mapEvent[ProductTrackingConstant.Tracking.KEY_ISLOGGIN] = (userId.isNotEmpty()).toString()

            TrackingUtil.addComponentTracker(mapEvent, productInfo, null, ProductTrackingConstant.Action.IMPRESSION_CHOOSE_VARIANT_NOTIFICATION)
        }

        private fun eventBuyButtonNonLogin(productInfo: DynamicProductInfoP1?, userId: String, shopType: String, buttonActionText: String) {
            val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                "click $buttonActionText on pdp - non login",
                ""
            )
            mapEvent[ProductTrackingConstant.Tracking.KEY_USER_ID] = userId
            mapEvent[ProductTrackingConstant.Tracking.KEY_SHOP_ID_SELLER] = productInfo?.basic?.shopID.orEmpty()
            mapEvent[ProductTrackingConstant.Tracking.KEY_SHOP_TYPE] = shopType
            mapEvent[ProductTrackingConstant.Tracking.KEY_ISLOGGIN] = (userId.isNotEmpty()).toString()

            TrackingUtil.addComponentTracker(mapEvent, productInfo, null, ProductTrackingConstant.Action.IMPRESSION_CHOOSE_VARIANT_NOTIFICATION)
        }

        fun eventEcommerceBuy(
            actionButton: Int,
            buttonText: String,
            userId: String,
            cartId: String,
            trackerAttribution: String,
            multiOrigin: Boolean,
            variantString: String,
            productInfo: DynamicProductInfoP1?,
            boType: Int,
            ratesEstimateData: P2RatesEstimateData?,
            buyerDistrictId: String,
            sellerDistrictId: String,
            lcaWarehouseId: String
        ) {
            val productId = productInfo?.basic?.productID ?: ""
            val shopId = productInfo?.basic?.shopID ?: ""
            val productName = productInfo?.data?.name ?: ""
            val productPrice = productInfo?.finalPrice.toString()
            val shopType = productInfo?.shopTypeString ?: ""
            val shopName = productInfo?.basic?.shopName ?: ""
            val boTypeString = ProductCartHelper.getBoTrackerString(boType)

            val quantity = productInfo?.basic?.minOrder ?: 0
            val generateButtonActionString = when (actionButton) {
                ProductDetailCommonConstant.OCS_BUTTON -> "$buttonText ocs"
                ProductDetailCommonConstant.OCC_BUTTON -> "$buttonText occ"
                else -> "$buttonText normal"
            }

            val categoryName = productInfo?.basic?.category?.detail?.map {
                it.name
            }?.joinToString("/", postfix = "/ ${productInfo.basic.category.id}") ?: ""

            val categoryId = productInfo?.basic?.category?.detail?.map {
                it.id
            }?.joinToString("/") ?: ""

            val dimension10 = productInfo?.data?.isCod ?: false
            val dimension12 = ratesEstimateData?.cheapestShippingPrice?.toLong()?.toString() ?: ""
            val dimension14 = ratesEstimateData?.title ?: ""
            val dimension16 = ratesEstimateData?.etaText ?: ""
            val dimension120 = "$buyerDistrictId - $sellerDistrictId"

            TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                    ProductTrackingConstant.Tracking.KEY_EVENT, "addToCart",
                    ProductTrackingConstant.Tracking.KEY_CATEGORY, ProductTrackingConstant.Category.PDP,
                    ProductTrackingConstant.Tracking.KEY_ACTION, if (actionButton == ProductDetailCommonConstant.ATC_BUTTON) "click - tambah ke keranjang on pdp" else "click - $buttonText on pdp",
                    ProductTrackingConstant.Tracking.KEY_LABEL, if (actionButton == ProductDetailCommonConstant.ATC_BUTTON) "" else "fitur : $generateButtonActionString",
                    ProductTrackingConstant.Tracking.KEY_PRODUCT_ID, productId,
                    ProductTrackingConstant.Tracking.KEY_LAYOUT, "layout:${productInfo?.layoutName};catName:${productInfo?.basic?.category?.name};catId:${productInfo?.basic?.category?.id};",
                    ProductTrackingConstant.Tracking.KEY_HIT_USER_ID, userId,
                    ProductTrackingConstant.Tracking.KEY_SHOP_ID_SELLER, shopId,
                    ProductTrackingConstant.Tracking.KEY_SHOP_TYPE, shopType,
                    ProductTrackingConstant.Tracking.KEY_WAREHOUSE_ID, lcaWarehouseId,
                    ProductTrackingConstant.Tracking.KEY_ISLOGGIN, (userId.isNotEmpty()).toString(),
                    ProductTrackingConstant.Tracking.KEY_BUSINESS_UNIT, ProductTrackingConstant.Tracking.BUSINESS_UNIT_PDP,
                    ProductTrackingConstant.Tracking.KEY_CURRENT_SITE, ProductTrackingConstant.Tracking.CURRENT_SITE,
                    ProductTrackingConstant.Tracking.KEY_COMPONENT, "",
                    ProductTrackingConstant.Tracking.KEY_ECOMMERCE,
                    DataLayer.mapOf(
                        ProductTrackingConstant.Tracking.CURRENCY_CODE,
                        ProductTrackingConstant.Tracking.CURRENCY_DEFAULT_VALUE,
                        ProductTrackingConstant.Tracking.KEY_ADD,
                        DataLayer.mapOf(
                            ProductTrackingConstant.Tracking.PRODUCTS,
                            DataLayer.listOf(
                                DataLayer.mapOf(
                                    ProductTrackingConstant.Tracking.NAME, productName,
                                    ProductTrackingConstant.Tracking.ID, productId,
                                    ProductTrackingConstant.Tracking.PRICE, productPrice,
                                    ProductTrackingConstant.Tracking.BRAND, ProductTrackingConstant.Tracking.VALUE_NONE_OTHER,
                                    ProductTrackingConstant.Tracking.CATEGORY, categoryName,
                                    ProductTrackingConstant.Tracking.VARIANT, variantString,
                                    ProductTrackingConstant.Tracking.QUANTITY, quantity,
                                    ProductTrackingConstant.Tracking.KEY_PRODUCT_CATEGORY_ID, categoryId,
                                    ProductTrackingConstant.Tracking.KEY_PRODUCT_SHOP_ID, shopId,
                                    ProductTrackingConstant.Tracking.KEY_PRODUCT_SHOP_NAME, shopName,
                                    ProductTrackingConstant.Tracking.KEY_PRODUCT_SHOP_TYPE, shopType,
                                    ProductTrackingConstant.Tracking.KEY_DIMENSION_10, dimension10.toString(),
                                    ProductTrackingConstant.Tracking.KEY_DIMENSION_12, dimension12,
                                    ProductTrackingConstant.Tracking.KEY_DIMENSION_14, dimension14,
                                    ProductTrackingConstant.Tracking.KEY_DIMENSION_16, dimension16,
                                    ProductTrackingConstant.Tracking.KEY_DIMENSION_38, trackerAttribution,
                                    ProductTrackingConstant.Tracking.KEY_DIMENSION_40, "null",
                                    ProductTrackingConstant.Tracking.KEY_DIMENSION_45, cartId,
                                    ProductTrackingConstant.Tracking.KEY_DIMENSION_54, TrackingUtil.getMultiOriginAttribution(multiOrigin),
                                    ProductTrackingConstant.Tracking.KEY_DIMENSION_79, shopId,
                                    ProductTrackingConstant.Tracking.KEY_DIMENSION_80, shopName,
                                    ProductTrackingConstant.Tracking.KEY_DIMENSION_81, shopType,
                                    ProductTrackingConstant.Tracking.KEY_DIMENSION_82, categoryId,
                                    ProductTrackingConstant.Tracking.KEY_DIMENSION_83, boTypeString,
                                    ProductTrackingConstant.Tracking.KEY_DIMENSION_120, dimension120
                                )
                            )
                        )
                    )
                )
            )
        }

        fun onSingleVariantClicked(productInfo: DynamicProductInfoP1?, variantUiData: ProductSingleVariantDataModel?, variantCommonData: ProductVariant?, variantPosition: Int) {
            val variantLevel = variantCommonData?.variants?.size?.toString()?.toList()?.joinToString(prefix = "level : ", postfix = ";")
            val variantTitle = variantCommonData?.variants?.mapNotNull { it.identifier }?.joinToString(",", prefix = "variant_title : ", postfix = ";")
                ?: ""
            val variantValue = "variant_value: " + generateVariantString(
                variantCommonData,
                productInfo?.basic?.productID
                    ?: ""
            ) + ";"
            val variantId = "variant : " + productInfo?.basic?.productID + ";"

            val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.CLICK_CHOOSE_PRODUCT_VARIANT,
                variantLevel + variantTitle + variantValue + variantId
            )

            mapEvent[ProductTrackingConstant.Tracking.KEY_BUSINESS_UNIT] = ProductTrackingConstant.Tracking.BUSINESS_UNIT_PDP
            mapEvent[ProductTrackingConstant.Tracking.KEY_CURRENT_SITE] = ProductTrackingConstant.Tracking.CURRENT_SITE

            TrackingUtil.addComponentTracker(mapEvent, productInfo, generateComponentTrackModel(variantUiData, variantPosition), ProductTrackingConstant.Action.CLICK_CHOOSE_PRODUCT_VARIANT)
        }

        fun eventFollowShop(
            productInfo: DynamicProductInfoP1?,
            componentTrackDataModel: ComponentTrackDataModel?,
            shopName: String
        ) {
            val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.CLICK_FOLLOW,
                shopName
            )

            TrackingUtil.addComponentTracker(mapEvent, productInfo, componentTrackDataModel, ProductTrackingConstant.Action.CLICK_FOLLOW)
        }

        fun eventUnfollowShop(
            productInfo: DynamicProductInfoP1?,
            componentTrackDataModel: ComponentTrackDataModel?,
            shopName: String
        ) {
            val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.CLICK_UNFOLLOW,
                shopName
            )

            TrackingUtil.addComponentTracker(mapEvent, productInfo, componentTrackDataModel, ProductTrackingConstant.Action.CLICK_UNFOLLOW)
        }

        fun trackTradein(usedPrice: Double, productInfo: DynamicProductInfoP1?, componentTrackDataModel: ComponentTrackDataModel?) {
            if (usedPrice > 0) {
                trackTradeinAfterDiagnotics(productInfo, componentTrackDataModel)
            } else {
                trackTradeinBeforeDiagnotics(productInfo, componentTrackDataModel)
            }
        }

        fun trackTradeinBeforeDiagnotics(productInfo: DynamicProductInfoP1?, componentTrackDataModel: ComponentTrackDataModel?) {
            val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.CLICK_TRADEIN,
                "before diagnostic"
            )

            TrackingUtil.addComponentTracker(mapEvent, productInfo, componentTrackDataModel, ProductTrackingConstant.Action.CLICK_TRADEIN)
        }

        fun trackTradeinAfterDiagnotics(productInfo: DynamicProductInfoP1?, componentTrackDataModel: ComponentTrackDataModel?) {
            val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Category.PDP,
                "after diagnostic"
            )
            TrackingUtil.addComponentTracker(mapEvent, productInfo, componentTrackDataModel, ProductTrackingConstant.Category.PDP)
        }

        fun eventClickSeeMoreRecomWidget(recomWidget: RecommendationWidget, widgetName: String, productInfo: DynamicProductInfoP1?, componentTrackDataModel: ComponentTrackDataModel) {
            val recomSeeAllAction = String.format(ProductTrackingConstant.Action.CLICK_SEE_MORE_WIDGET, widgetName)
            val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                recomSeeAllAction,
                String.format(ProductTrackingConstant.Action.EVENT_ACTION_CLICK_SEE_ALL_RECOM, recomWidget.title, recomWidget.pageName, recomWidget.layoutType)
            )
            mapEvent.put(ProductTrackingConstant.Tracking.KEY_BUSINESS_UNIT, ProductTrackingConstant.Tracking.CURRENT_SITE)
            mapEvent.put(ProductTrackingConstant.Tracking.KEY_CURRENT_SITE, ProductTrackingConstant.Tracking.CURRENT_SITE)
            TrackingUtil.addComponentTracker(mapEvent, productInfo, componentTrackDataModel, recomSeeAllAction)
        }

        fun eventClickRecomAddToCart(recomItem: RecommendationItem, userId: String, quantity: Int) {
            val mapData = DataLayer.mapOf(
                ProductTrackingConstant.Tracking.KEY_EVENT, ProductTrackingConstant.RecomTokonow.KEY_EVENT_ATC,
                ProductTrackingConstant.Tracking.KEY_CATEGORY, ProductTrackingConstant.RecomTokonow.KEY_EVENT_CATEGORY_ATC,
                ProductTrackingConstant.Tracking.KEY_ACTION, ProductTrackingConstant.RecomTokonow.KEY_EVENT_ACTION_ATC,
                ProductTrackingConstant.Tracking.KEY_LABEL, String.format(ProductTrackingConstant.RecomTokonow.KEY_EVENT_LABEL_ATC, recomItem.name, ""),
                ProductTrackingConstant.Tracking.KEY_BUSINESS_UNIT, ProductTrackingConstant.Tracking.BUSINESS_UNIT,
                ProductTrackingConstant.Tracking.KEY_CURRENT_SITE, ProductTrackingConstant.Tracking.CURRENT_SITE,
                ProductTrackingConstant.Tracking.KEY_USER_ID_VARIANT, userId,
                ProductTrackingConstant.RecomTokonow.KEY_EVENT_PAGE_SOURCE, String.format(ProductTrackingConstant.RecomTokonow.PARAM_EVENT_PAGE_SOURCE, "PDP", recomItem.recommendationType),
                ProductTrackingConstant.Tracking.KEY_ECOMMERCE,
                DataLayer.mapOf(
                    ProductTrackingConstant.Tracking.CURRENCY_CODE,
                    ProductTrackingConstant.Tracking.CURRENCY_DEFAULT_VALUE,
                    ProductTrackingConstant.Tracking.KEY_ADD,
                    DataLayer.mapOf(
                        ProductTrackingConstant.Tracking.PRODUCTS,
                        DataLayer.listOf(
                            DataLayer.mapOf(
                                ProductTrackingConstant.Tracking.BRAND, ProductTrackingConstant.Tracking.VALUE_NONE_OTHER,
                                ProductTrackingConstant.Tracking.CATEGORY, "",
                                ProductTrackingConstant.Tracking.KEY_CATEGORY_ID, "",
                                ProductTrackingConstant.Tracking.KEY_DIMENSION_40, String.format(ProductTrackingConstant.RecomTokonow.PARAM_ATC_DIMENS_40, recomItem.pageName, recomItem.recommendationType),
                                ProductTrackingConstant.Tracking.KEY_DIMENSION_45, recomItem.cartId,
                                ProductTrackingConstant.Tracking.ID, recomItem.productId,
                                ProductTrackingConstant.Tracking.NAME, recomItem.name,
                                ProductTrackingConstant.Tracking.PRICE, recomItem.priceInt,
                                ProductTrackingConstant.Tracking.QUANTITY, quantity,
                                ProductTrackingConstant.Tracking.KEY_SHOP_ID_SELLER, recomItem.shopId,
                                ProductTrackingConstant.Tracking.KEY_SHOP_NAME, recomItem.shopName,
                                ProductTrackingConstant.Tracking.KEY_SHOP_TYPE, recomItem.shopType,
                                ProductTrackingConstant.Tracking.VARIANT, ""
                            )
                        )
                    )
                )

            )
            TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(mapData)
        }

        fun eventClickSeeFilterAnnotation(chipValue: String) {
            val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_RECOMMENDATION,
                ProductTrackingConstant.Category.PDP,
                CLICK_ANNOTATION_RECOM_CHIP,
                chipValue
            )
            TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
        }

        fun eventCategoryClicked(categoryId: String, categoryName: String, productInfo: DynamicProductInfoP1?, componentTrackDataModel: ComponentTrackDataModel) {
            val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.CLICK_CATEGORY,
                "$categoryId - $categoryName"
            )

            TrackingUtil.addComponentTracker(mapEvent, productInfo, componentTrackDataModel, ProductTrackingConstant.Action.CLICK_CATEGORY)
        }

        fun eventEtalaseClicked(etalaseId: String, etalaseName: String, productInfo: DynamicProductInfoP1?, componentTrackDataModel: ComponentTrackDataModel) {
            val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.CLICK_ETALASE,
                "$etalaseId - $etalaseName"
            )

            TrackingUtil.addComponentTracker(mapEvent, productInfo, componentTrackDataModel, ProductTrackingConstant.Action.CLICK_ETALASE)
        }

        fun eventProductImageOnSwipe(productInfo: DynamicProductInfoP1?, componentTrackDataModel: ComponentTrackDataModel, trackingQueue: TrackingQueue?, type: String, imageUrl: String, position: Int) {
            val productId = productInfo?.basic?.productID ?: ""
            val containerType = productInfo?.data?.containerType.orEmpty()
            val mapEvent = DataLayer.mapOf(
                ProductTrackingConstant.Tracking.KEY_EVENT, ProductTrackingConstant.MerchantVoucher.PROMO_VIEW,
                ProductTrackingConstant.Tracking.KEY_CATEGORY, ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Tracking.KEY_ACTION, ProductTrackingConstant.Action.SWIPE_PRODUCT_PICTURE,
                ProductTrackingConstant.Tracking.KEY_CATEGORY_ID, productId,
                ProductTrackingConstant.Tracking.KEY_LABEL, "",
                "categoryId", "productId : $productId",
                ProductTrackingConstant.Tracking.KEY_ECOMMERCE,
                DataLayer.mapOf(
                    "promoView",
                    DataLayer.mapOf(
                        "promotions",
                        DataLayer.listOf(
                            DataLayer.mapOf(
                                "id", imageUrl,
                                "name", "product detail page - $productId - pdp",
                                "creative", "media type:$type;container_type:$containerType;",
                                "creative_url", imageUrl,
                                "position", position
                            )
                        )
                    )
                )
            )
            mapEvent[ProductTrackingConstant.Tracking.KEY_BUSINESS_UNIT] = ProductTrackingConstant.Tracking.SWIPE_IMAGE_BUSINESS_UNIT
            mapEvent[ProductTrackingConstant.Tracking.KEY_PRODUCT_ID] = productId
            mapEvent[ProductTrackingConstant.Tracking.KEY_LAYOUT] = "layout:${productInfo?.layoutName};catName:${productInfo?.basic?.category?.name};catId:${productInfo?.basic?.category?.id};"
            mapEvent[ProductTrackingConstant.Tracking.KEY_COMPONENT] = "comp:${componentTrackDataModel.componentName};temp:${componentTrackDataModel.componentType};elem:${ProductTrackingConstant.Action.SWIPE_PRODUCT_PICTURE};cpos:${componentTrackDataModel.adapterPosition};"
            trackingQueue?.putEETracking(mapEvent as HashMap<String, Any>?)
        }

        fun eventProductImageClicked(productInfo: DynamicProductInfoP1?, componentTrackDataModel: ComponentTrackDataModel) {
            val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.CLICK_PRODUCT_PICTURE,
                ""
            )
            TrackingUtil.addComponentTracker(mapEvent, productInfo, componentTrackDataModel, ProductTrackingConstant.Action.CLICK_PRODUCT_PICTURE)
        }

        fun eventClickPDPInstallmentSeeMore(productInfo: DynamicProductInfoP1?, componentTrackDataModel: ComponentTrackDataModel) {
            val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.CLICK_LIHAT_SEMUA_ON_SIMULASI_CICILAN,
                ""
            )
            TrackingUtil.addComponentTracker(mapEvent, productInfo, componentTrackDataModel, ProductTrackingConstant.Action.CLICK_LIHAT_SEMUA_ON_SIMULASI_CICILAN)
        }

        fun eventClickProductDescriptionReadMore(productInfo: DynamicProductInfoP1?, componentTrackDataModel: ComponentTrackDataModel) {
            val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.CLICK_READ_MORE,
                ""
            )
            TrackingUtil.addComponentTracker(mapEvent, productInfo, componentTrackDataModel, ProductTrackingConstant.Action.CLICK_READ_MORE)
        }

        fun eventClickProductSpecificationReadMore(productInfo: DynamicProductInfoP1?, componentTrackDataModel: ComponentTrackDataModel) {
            val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PG,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.CLICK_SPECIFICATION_READ_MORE,
                ""
            ).apply {
                put(ProductTrackingConstant.Tracking.KEY_BUSINESS_UNIT, ProductTrackingConstant.Tracking.BUSINESS_UNIT_PDP)
                put(ProductTrackingConstant.Tracking.KEY_CURRENT_SITE, ProductTrackingConstant.Tracking.CURRENT_SITE)
                put(ProductTrackingConstant.Tracking.KEY_TRACKER_ID, ProductTrackingConstant.TrackerId.TRACKER_ID_CLICK_SPECIFICATION)
            }
            TrackingUtil.addComponentTracker(mapEvent, productInfo, componentTrackDataModel, ProductTrackingConstant.Action.CLICK_READ_MORE)
        }

        fun eventClickPdpShare(productId: String, userId: String, campaignId: String, bundleId: String) {
            val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_COMMUNICATION,
                ProductTrackingConstant.Category.TOP_NAV_SHARE_PDP,
                ProductTrackingConstant.Action.CLICK_SHARE_PDP,
                "$productId - $campaignId - $bundleId"
            )
            mapEvent[ProductTrackingConstant.Tracking.KEY_BUSINESS_UNIT] = ProductTrackingConstant.Tracking.VALUE_BUSINESS_UNIT_SHARING
            mapEvent[ProductTrackingConstant.Tracking.KEY_CURRENT_SITE] = ProductTrackingConstant.Tracking.CURRENT_SITE
            mapEvent[ProductTrackingConstant.Tracking.KEY_PRODUCT_ID] = productId
            mapEvent[ProductTrackingConstant.Tracking.KEY_USER_ID_VARIANT] = userId.ifBlank { 0 }
            TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
        }

        fun eventClickTradeInRibbon(productInfo: DynamicProductInfoP1?, componentTrackDataModel: ComponentTrackDataModel) {
            val productId = productInfo?.basic?.productID ?: ""

            val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.CLICK_RIBBON_TRADE_IN,
                productId
            )
            TrackingUtil.addComponentTracker(mapEvent, productInfo, componentTrackDataModel, ProductTrackingConstant.Action.CLICK_RIBBON_TRADE_IN)
        }

        fun eventImageShopClicked(productInfo: DynamicProductInfoP1?, shopId: String, componentTrackDataModel: ComponentTrackDataModel) {
            if (productInfo?.basic?.productID.isNullOrEmpty()) return
            val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.CLICK_SHOP_PAGE,
                shopId
            )
            TrackingUtil.addComponentTracker(mapEvent, productInfo, componentTrackDataModel, ProductTrackingConstant.Action.ADD_WISHLIST)
        }

        fun eventClickReviewOnBuyersImage(
            productInfo: DynamicProductInfoP1?,
            componentTrackDataModel: ComponentTrackDataModel,
            reviewId: String?
        ) {
            val productId = productInfo?.basic?.productID ?: ""
            val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.ImageReview.ACTION_SEE_ITEM,
                "product_id: $productId - review_id : $reviewId"
            )
            TrackingUtil.addComponentTracker(mapEvent, productInfo, componentTrackDataModel, ProductTrackingConstant.ImageReview.ACTION_SEE_ITEM)
        }

        fun onSeeAllLastItemImageReview(productInfo: DynamicProductInfoP1?, componentTrackDataModel: ComponentTrackDataModel) {
            val productId = productInfo?.basic?.productID ?: ""

            val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.ImageReview.ACTION_SEE_ALL,
                productId
            )
            TrackingUtil.addComponentTracker(mapEvent, productInfo, componentTrackDataModel, ProductTrackingConstant.ImageReview.ACTION_SEE_ALL)
        }

        fun onSeeAllReviewTextView(productInfo: DynamicProductInfoP1?, userId: String, componentTrackDataModel: ComponentTrackDataModel) {
            val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.CLICK_SEE_ALL_ULASAN,
                ""
            )
            mapEvent[ProductTrackingConstant.Tracking.KEY_USER_ID_VARIANT] = userId
            mapEvent[ProductTrackingConstant.Tracking.KEY_SHOP_ID_SELLER] = productInfo?.basic?.shopID
            mapEvent[ProductTrackingConstant.Tracking.KEY_ISLOGGIN] = (userId.isNotEmpty()).toString()
            mapEvent[ProductTrackingConstant.Tracking.KEY_SHOP_TYPE] = productInfo?.shopTypeString.orEmpty()

            TrackingUtil.addComponentTracker(mapEvent, productInfo, componentTrackDataModel, ProductTrackingConstant.Action.CLICK_SEE_ALL_ULASAN)
        }

        fun onClickReviewerName(
            dynamicProductInfoP1: DynamicProductInfoP1,
            reviewID: String,
            userId: String,
            reviewerUserID: String,
            statistics: String,
            label: String,
            componentTrackData: ComponentTrackDataModel
        ) {
            val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PG,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.MostHelpfulReview.ACTON_CLICK_REVIEWER_NAME,
                String.format(
                    ProductTrackingConstant.MostHelpfulReview.LABEL_CLICK_REVIEWER_NAME,
                    reviewID,
                    reviewerUserID,
                    statistics,
                    label
                )
            )
            mapEvent[ProductTrackingConstant.Tracking.KEY_TRACKER_ID] = ProductTrackingConstant.MostHelpfulReview.TRACKER_ID_CLICK_REVIEWER_NAME
            mapEvent[ProductTrackingConstant.Tracking.KEY_USER_ID_VARIANT] = userId
            mapEvent[ProductTrackingConstant.Tracking.KEY_BUSINESS_UNIT] = ProductTrackingConstant.Tracking.BUSINESS_UNIT_PDP
            mapEvent[ProductTrackingConstant.Tracking.KEY_CURRENT_SITE] = ProductTrackingConstant.Tracking.CURRENT_SITE

            TrackingUtil.addComponentTracker(mapEvent, dynamicProductInfoP1, componentTrackData, ProductTrackingConstant.MostHelpfulReview.ACTON_CLICK_REVIEWER_NAME)
        }

        fun eventRecommendationClick(
            product: RecommendationItem,
            chipValue: String,
            isComparison: Boolean,
            position: Int,
            isSessionActive: Boolean,
            pageName: String,
            pageTitle: String,
            productInfo: DynamicProductInfoP1?,
            componentTrackDataModel: ComponentTrackDataModel
        ) {
            val listValue = ProductTrackingConstant.Tracking.LIST_DEFAULT + pageName +
                (if (!isSessionActive) " - ${ProductTrackingConstant.Tracking.USER_NON_LOGIN}" else "") +
                ProductTrackingConstant.Tracking.LIST_RECOMMENDATION + product.recommendationType + (if (product.isTopAds) " - product topads" else "") +
                (if (isComparison) ProductTrackingConstant.TopAds.RECOMMENDATION_COMPARISON else ProductTrackingConstant.TopAds.RECOMMENDATION_CAROUSELL) + " - " + (
                productInfo?.parentProductId
                    ?: ""
                )
            val topAdsAction = ProductTrackingConstant.Action.TOPADS_CLICK + (if (!isSessionActive) " - ${ProductTrackingConstant.Tracking.USER_NON_LOGIN}" else "")
            val bebasOngkirValue = if (product.isFreeOngkirActive && product.labelGroupList.hasLabelGroupFulfillment()) {
                ProductTrackingConstant.Tracking.VALUE_BEBAS_ONGKIR_EXTRA
            } else if (product.isFreeOngkirActive && !product.labelGroupList.hasLabelGroupFulfillment()) {
                ProductTrackingConstant.Tracking.VALUE_BEBAS_ONGKIR
            } else {
                ProductTrackingConstant.Tracking.VALUE_NONE_OTHER
            }
            TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                    ProductTrackingConstant.Tracking.KEY_EVENT, ProductTrackingConstant.Action.PRODUCT_CLICK,
                    ProductTrackingConstant.Tracking.KEY_CATEGORY, ProductTrackingConstant.Category.PDP,
                    ProductTrackingConstant.Tracking.KEY_ACTION, topAdsAction,
                    ProductTrackingConstant.Tracking.KEY_LABEL, "$pageTitle-$chipValue",
                    ProductTrackingConstant.Tracking.KEY_PRODUCT_ID,
                    productInfo?.basic?.productID
                        ?: "",
                    ProductTrackingConstant.Tracking.KEY_LAYOUT, "layout:${productInfo?.layoutName};catName:${productInfo?.basic?.category?.name};catId:${productInfo?.basic?.category?.id};",
                    ProductTrackingConstant.Tracking.KEY_COMPONENT, "comp:${componentTrackDataModel.componentName};temp:${componentTrackDataModel.componentType};elem:$topAdsAction;cpos:${componentTrackDataModel.adapterPosition};",
                    ProductTrackingConstant.Tracking.KEY_ECOMMERCE,
                    DataLayer.mapOf(
                        ProductTrackingConstant.Tracking.CURRENCY_CODE,
                        ProductTrackingConstant.Tracking.CURRENCY_DEFAULT_VALUE,
                        ProductTrackingConstant.Action.CLICK,
                        DataLayer.mapOf(
                            ProductTrackingConstant.Tracking.ACTION_FIELD,
                            DataLayer.mapOf(ProductTrackingConstant.Tracking.LIST, listValue),
                            ProductTrackingConstant.Tracking.PRODUCTS,
                            DataLayer.listOf(
                                DataLayer.mapOf(
                                    ProductTrackingConstant.Tracking.PROMO_NAME, product.name,
                                    ProductTrackingConstant.Tracking.ID, product.productId.toString(), ProductTrackingConstant.Tracking.PRICE, TrackingUtil.removeCurrencyPrice(product.price),
                                    ProductTrackingConstant.Tracking.BRAND, ProductTrackingConstant.Tracking.DEFAULT_VALUE,
                                    ProductTrackingConstant.Tracking.VARIANT, ProductTrackingConstant.Tracking.DEFAULT_VALUE,
                                    ProductTrackingConstant.Tracking.CATEGORY, product.categoryBreadcrumbs.toLowerCase(),
                                    ProductTrackingConstant.Tracking.PROMO_POSITION, position + 1,
                                    ProductTrackingConstant.Tracking.KEY_DIMENSION_83, bebasOngkirValue,
                                    ProductTrackingConstant.Tracking.KEY_PRODUCT_ID, product.productId.toString()
                                )
                            )
                        )
                    )
                )
            )
        }

        fun eventEditProductClick(
            isSessionActive: Boolean,
            productInfo: DynamicProductInfoP1?,
            componentTrackDataModel: ComponentTrackDataModel
        ) {
            val topAdsAction = ProductTrackingConstant.Action.TOPADS_CLICK + (if (!isSessionActive) " - ${ProductTrackingConstant.Tracking.USER_NON_LOGIN}" else "")

            val editProductData = DataLayer.mapOf(
                ProductTrackingConstant.Tracking.KEY_EVENT, ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Tracking.KEY_CATEGORY, ProductTrackingConstant.Category.PRODUCT_DETAIL_PAGE_SELLER,
                ProductTrackingConstant.Tracking.KEY_ACTION, ProductTrackingConstant.Action.CLICK_EDIT_PRODUCT,
                ProductTrackingConstant.Tracking.KEY_LABEL, "",
                ProductTrackingConstant.Tracking.KEY_PRODUCT_ID, productInfo?.parentProductId,
                ProductTrackingConstant.Tracking.KEY_LAYOUT, "layout:${productInfo?.layoutName};catName:${productInfo?.basic?.category?.name};catId:${productInfo?.basic?.category?.id};",
                ProductTrackingConstant.Tracking.KEY_COMPONENT, "comp:${componentTrackDataModel.componentName};temp:${componentTrackDataModel.componentType};elem:$topAdsAction;cpos:${componentTrackDataModel.adapterPosition};"
            )
            TrackApp.getInstance().gtm.sendGeneralEvent(editProductData)
        }

        fun eventPDPAddToWishlist(productInfo: DynamicProductInfoP1?, componentTrackDataModel: ComponentTrackDataModel) {
            if (productInfo?.basic?.productID.isNullOrEmpty()) return
            val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                "add wishlist",
                productInfo?.basic?.productID
            )
            TrackingUtil.addComponentTracker(mapEvent, productInfo, componentTrackDataModel, ProductTrackingConstant.Action.ADD_WISHLIST)
        }

        fun eventPDPRemoveToWishlist(productInfo: DynamicProductInfoP1?, componentTrackDataModel: ComponentTrackDataModel) {
            if (productInfo?.basic?.productID.isNullOrEmpty()) return
            val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.REMOVE_WISHLIST,
                productInfo?.basic?.productID
            )

            TrackingUtil.addComponentTracker(mapEvent, productInfo, componentTrackDataModel, ProductTrackingConstant.Action.REMOVE_WISHLIST)
        }

        fun eventPDPAddToWishlistNonLogin(productInfo: DynamicProductInfoP1?, componentTrackDataModel: ComponentTrackDataModel) {
            if (productInfo?.basic?.productID.isNullOrEmpty()) return
            val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.ADD_WISHLIST_NON_LOGIN,
                productInfo?.basic?.productID

            )
            TrackingUtil.addComponentTracker(mapEvent, productInfo, componentTrackDataModel, ProductTrackingConstant.Action.ADD_WISHLIST_NON_LOGIN)
        }

        fun eventButtonChatClicked(productInfo: DynamicProductInfoP1?) {
            if (productInfo?.basic?.productID?.isEmpty() == true) return
            val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.CLICK_BUTTON_CHAT,
                productInfo?.basic?.productID ?: ""
            )

            TrackingUtil.addComponentTracker(mapEvent, productInfo, null, ProductTrackingConstant.Action.CLICK_BUTTON_CHAT)
        }

        fun eventClickVariant(eventLabel: String, productInfo: DynamicProductInfoP1?, componentTrackDataModel: ComponentTrackDataModel) {
            val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.CLICK_VARIANT,
                eventLabel
            )
            TrackingUtil.addComponentTracker(mapEvent, productInfo, componentTrackDataModel, ProductTrackingConstant.Action.CLICK_VARIANT)
        }

        fun eventCartToolbarClicked(variant: String?, productInfo: DynamicProductInfoP1?) {
            val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.CLICK_CART_BUTTON_VARIANT,
                variant ?: ""
            )
            TrackingUtil.addComponentTracker(mapEvent, productInfo, null, ProductTrackingConstant.Action.CLICK_CART_BUTTON_VARIANT)
        }

        fun eventNotifyMe(productInfo: DynamicProductInfoP1, componentTrackDataModel: ComponentTrackDataModel?, notifyMe: Boolean, userId: String) {
            val action = if (notifyMe) {
                ProductDetailCommonConstant.VALUE_TEASER_TRACKING_UNREGISTER
            } else {
                ProductDetailCommonConstant.VALUE_TEASER_TRACKING_REGISTER
            }
            val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                "${ProductTrackingConstant.Action.CLICK_NOTIFY_ME} - $action",
                ProductTrackingConstant.Label.EMPTY_LABEL
            )

            mapEvent[ProductTrackingConstant.Tracking.KEY_USER_ID_VARIANT] = userId
            mapEvent[ProductTrackingConstant.Tracking.KEY_ISLOGGIN] = (userId.isNotEmpty()).toString()

            TrackingUtil.addComponentTracker(mapEvent, productInfo, componentTrackDataModel, ProductTrackingConstant.Action.CLICK_NOTIFY_ME)
        }

        fun eventActivationOvo(productId: String, userId: String) {
            val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.CLICK_BUY_ACTIVATION_OVO,
                ProductTrackingConstant.Label.EMPTY_LABEL
            )
            TrackingUtil.addComponentOvoTracker(mapEvent, productId, userId)
        }

        fun eventAddToCartRecommendationClick(product: RecommendationItem, position: Int, isSessionActive: Boolean, pageName: String, pageTitle: String, mainProductId: String) {
            val valueLoginOrNotLogin = if (!isSessionActive) {
                " ${ProductTrackingConstant.Tracking.USER_NON_LOGIN} - "
            } else {
                ""
            }
            val listValue = ProductTrackingConstant.Tracking.LIST_PRODUCT_AFTER_ATC + pageName + ProductTrackingConstant.Tracking.LIST_RECOMMENDATION + valueLoginOrNotLogin +
                product.recommendationType + (if (product.isTopAds) " - product topads - $mainProductId" else " - $mainProductId")
            val actionValuePostfix = if (!isSessionActive) {
                " - ${ProductTrackingConstant.Tracking.USER_NON_LOGIN}"
            } else {
                ""
            }
            val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.Action.PRODUCT_CLICK,
                ProductTrackingConstant.Category.PDP_AFTER_ATC,
                ProductTrackingConstant.Action.TOPADS_ATC_CLICK + actionValuePostfix,
                pageTitle
            )
            val bebasOngkirValue = if (product.isFreeOngkirActive && product.labelGroupList.hasLabelGroupFulfillment()) {
                ProductTrackingConstant.Tracking.VALUE_BEBAS_ONGKIR_EXTRA
            } else if (product.isFreeOngkirActive && !product.labelGroupList.hasLabelGroupFulfillment()) {
                ProductTrackingConstant.Tracking.VALUE_BEBAS_ONGKIR
            } else {
                ProductTrackingConstant.Tracking.VALUE_NONE_OTHER
            }
            mapEvent[ProductTrackingConstant.Tracking.KEY_ECOMMERCE] = DataLayer.mapOf(
                ProductTrackingConstant.Action.CLICK,
                DataLayer.mapOf(
                    ProductTrackingConstant.Tracking.ACTION_FIELD,
                    DataLayer.mapOf(ProductTrackingConstant.Tracking.LIST, listValue),
                    ProductTrackingConstant.Tracking.PRODUCTS,
                    DataLayer.listOf(
                        DataLayer.mapOf(
                            ProductTrackingConstant.Tracking.PROMO_NAME, product.name,
                            ProductTrackingConstant.Tracking.ID, product.productId.toString(),
                            ProductTrackingConstant.Tracking.PRICE, removeCurrencyPrice(product.price),
                            ProductTrackingConstant.Tracking.BRAND, ProductTrackingConstant.Tracking.DEFAULT_VALUE,
                            ProductTrackingConstant.Tracking.CATEGORY, product.categoryBreadcrumbs.toLowerCase(),
                            ProductTrackingConstant.Tracking.VARIANT, ProductTrackingConstant.Tracking.DEFAULT_VALUE,
                            ProductTrackingConstant.Tracking.PROMO_POSITION, position,
                            ProductTrackingConstant.Tracking.KEY_DIMENSION_83, bebasOngkirValue
                        )
                    )
                )
            )

            TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(mapEvent)
        }

        fun eventDiscussionSeeAll(productInfo: DynamicProductInfoP1, componentTrackDataModel: ComponentTrackDataModel?, userId: String, numberOfThreadsShown: String) {
            val mapEvent = TrackingUtil.addDiscussionParams(
                TrackAppUtils.gtmData(
                    ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                    ProductTrackingConstant.Category.PDP,
                    ProductTrackingConstant.Action.CLICK_DISCUSSION_SEE_ALL,
                    String.format(ProductTrackingConstant.Label.DISCUSSION_SEE_ALL, numberOfThreadsShown)
                ),
                userId
            )

            TrackingUtil.addComponentTracker(mapEvent, productInfo, componentTrackDataModel, ProductTrackingConstant.Action.CLICK_DISCUSSION_SEE_ALL)
        }

        fun eventDiscussionDetails(productInfo: DynamicProductInfoP1, componentTrackDataModel: ComponentTrackDataModel?, userId: String, talkId: String, numberOfThreadsShown: String) {
            val mapEvent = TrackingUtil.addDiscussionParams(
                TrackAppUtils.gtmData(
                    ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                    ProductTrackingConstant.Category.PDP,
                    ProductTrackingConstant.Action.CLICK_THREAD_DETAIL_DISCUSSION,
                    String.format(ProductTrackingConstant.Label.DISCUSSION_DETAIL, talkId, numberOfThreadsShown)
                ),
                userId
            )

            TrackingUtil.addComponentTracker(mapEvent, productInfo, componentTrackDataModel, ProductTrackingConstant.Action.CLICK_THREAD_DETAIL_DISCUSSION)
        }

        fun eventEmptyDiscussionSendQuestion(productInfo: DynamicProductInfoP1?, componentTrackDataModel: ComponentTrackDataModel?, userId: String, isVariantSelected: Boolean, totalAvailableVariants: String) {
            val mapEvent = TrackingUtil.addDiscussionParams(
                TrackAppUtils.gtmData(
                    ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                    ProductTrackingConstant.Category.PDP,
                    ProductTrackingConstant.Action.CLICK_SEND_QUESTION,
                    String.format(ProductTrackingConstant.Label.DISCUSSION_EMPTY_QUESTION, isVariantSelected, totalAvailableVariants)
                ),
                userId
            )

            TrackingUtil.addComponentTracker(mapEvent, productInfo, componentTrackDataModel, ProductTrackingConstant.Action.CLICK_SEND_QUESTION)
        }

        fun eventTopAdsImageViewClicked(trackingQueue: TrackingQueue, userId: String, bannerId: String, position: Int, bannerName: String) {
            val mapEvent = hashMapOf<String, Any>(
                ProductTrackingConstant.Tracking.KEY_EVENT to ProductTrackingConstant.Tracking.PROMO_CLICK,
                ProductTrackingConstant.Tracking.KEY_CATEGORY to ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Tracking.KEY_ACTION to ProductTrackingConstant.Action.CLICK_TDN_BANNER_ADS_WIDGET,
                ProductTrackingConstant.Tracking.KEY_LABEL to "",
                ProductTrackingConstant.Tracking.KEY_BUSINESS_UNIT to ProductTrackingConstant.Tracking.BUSINESS_UNIT,
                ProductTrackingConstant.Tracking.KEY_CURRENT_SITE to ProductTrackingConstant.Tracking.CURRENT_SITE,
                ProductTrackingConstant.Tracking.KEY_DISCUSSION_USER_ID to userId,
                ProductTrackingConstant.Tracking.KEY_SCREEN_NAME to ProductTrackingConstant.Tracking.PRODUCT_DETAIL_SCREEN_NAME,
                ProductTrackingConstant.Tracking.KEY_ECOMMERCE to hashMapOf(
                    ProductTrackingConstant.Tracking.PROMO_CLICK to hashMapOf(
                        ProductTrackingConstant.Tracking.KEY_PROMOTIONS to listOf(
                            hashMapOf(
                                ProductTrackingConstant.Tracking.ID to bannerId,
                                ProductTrackingConstant.Tracking.NAME to ProductTrackingConstant.TopAds.PDP_BANNER_TOPADS,
                                ProductTrackingConstant.Tracking.CREATIVE to
                                    if (bannerName.isEmpty()) ProductTrackingConstant.TopAds.DFAULT_CREATIVE_NAME_BANNER_TOP_ADS else bannerName,
                                ProductTrackingConstant.Tracking.POSITION to position
                            )
                        )
                    )
                )
            )

            trackingQueue.putEETracking(mapEvent)
        }

        fun eventTopAdsButtonClicked(userId: String, buttonName: String, productInfo: DynamicProductInfoP1?) {
            val data = mapOf(
                ProductTrackingConstant.Tracking.KEY_EVENT to ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Tracking.KEY_CATEGORY to ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Tracking.KEY_ACTION to "${ProductTrackingConstant.Action.CLICK} - $buttonName",
                ProductTrackingConstant.Tracking.KEY_LABEL to "",
                ProductTrackingConstant.Tracking.KEY_PRODUCT_ID to (
                    productInfo?.basic?.productID
                        ?: "0"
                    ),
                ProductTrackingConstant.Tracking.KEY_LAYOUT to "layout:${productInfo?.layoutName};catName:${productInfo?.basic?.category?.name};catId:${productInfo?.basic?.category?.id};",
                ProductTrackingConstant.Tracking.KEY_USER_ID_VARIANT to userId,
                ProductTrackingConstant.Tracking.KEY_SHOP_ID_SELLER to (
                    productInfo?.basic?.shopID
                        ?: "0"
                    ),
                ProductTrackingConstant.Tracking.KEY_SHOP_TYPE to productInfo?.shopTypeString.orEmpty(),
                ProductTrackingConstant.Tracking.KEY_ISLOGGIN to (userId.isNotEmpty()).toString()
            )

            TrackApp.getInstance().gtm.sendGeneralEvent(data)
        }

        fun eventClickOosButton(buttonName: String, isVariant: Boolean, productInfo: DynamicProductInfoP1?, userId: String) {
            val data = mapOf(
                ProductTrackingConstant.Tracking.KEY_EVENT to ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Tracking.KEY_CATEGORY to ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Tracking.KEY_ACTION to String.format(ProductTrackingConstant.Action.CLICK_BUTTON_OOS, buttonName),
                ProductTrackingConstant.Tracking.KEY_LABEL to String.format(ProductTrackingConstant.Label.BUTTON_OOS, isVariant.toString()),
                ProductTrackingConstant.Tracking.KEY_PRODUCT_ID to (
                    productInfo?.basic?.productID
                        ?: "0"
                    ),
                ProductTrackingConstant.Tracking.KEY_LAYOUT to "layout:${productInfo?.layoutName};catName:${productInfo?.basic?.category?.name};catId:${productInfo?.basic?.category?.id};",
                ProductTrackingConstant.Tracking.KEY_USER_ID_VARIANT to userId,
                ProductTrackingConstant.Tracking.KEY_SHOP_ID_SELLER to (
                    productInfo?.basic?.shopID
                        ?: "0"
                    ),
                ProductTrackingConstant.Tracking.KEY_SHOP_TYPE to productInfo?.shopTypeString.orEmpty(),
                ProductTrackingConstant.Tracking.KEY_ISLOGGIN to (userId.isNotEmpty()).toString()
            )
            TrackApp.getInstance().gtm.sendGeneralEvent(data)
        }

        fun eventClickPDPInsuranceProtection(productInfo: DynamicProductInfoP1?, insuranceBrandUrl: String, componentTrackDataModel: ComponentTrackDataModel) {
            val categoryIdLevel3 = productInfo?.basic?.category?.detail?.getOrNull(2)?.id ?: ""

            val mapOfData = mutableMapOf(
                ProductTrackingConstant.Tracking.KEY_EVENT to ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Tracking.KEY_CATEGORY to ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Tracking.KEY_ACTION to ProductTrackingConstant.Action.CLICK_PP_INSURANCE_BOTTOMSHEET,
                ProductTrackingConstant.Tracking.KEY_LABEL to "$categoryIdLevel3 - $insuranceBrandUrl",
                ProductTrackingConstant.Tracking.CATEGORY to ProductTrackingConstant.Tracking.KEY_INSURANCE,
                ProductTrackingConstant.Tracking.BUSINESS_UNIT to ProductTrackingConstant.Tracking.KEY_FINTECH,
                ProductTrackingConstant.Tracking.KEY_PRODUCT_ID_ to (
                    productInfo?.basic?.productID
                        ?: ""
                    ),
                ProductTrackingConstant.Tracking.KEY_CURRENT_SITE to ProductTrackingConstant.Tracking.CURRENT_SITE_FINTECH
            ) as MutableMap<String, Any>

            TrackingUtil.addComponentTracker(mapOfData, productInfo, componentTrackDataModel, ProductTrackingConstant.Action.CLICK_PP_INSURANCE_BOTTOMSHEET)
        }

        fun eventAtcClickLihat(productId: String?) {
            if (productId.isNullOrEmpty()) {
                return
            }
            val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.CLICK_CHECK_CART,
                productId
            )
            mapEvent[ProductTrackingConstant.Tracking.KEY_PRODUCT_ID] = productId
            TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
        }

        fun eventClickAtcToVariantBottomSheet(productId: String?) {
            if (productId.isNullOrEmpty()) {
                return
            }
            val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.CLICK_VARIANT_ATC_BUTTON,
                ""
            )
            mapEvent[ProductTrackingConstant.Tracking.KEY_PRODUCT_ID] = productId
            mapEvent[ProductTrackingConstant.Tracking.KEY_BUSINESS_UNIT] = ProductTrackingConstant.Tracking.BUSINESS_UNIT_PDP
            mapEvent[ProductTrackingConstant.Tracking.KEY_CURRENT_SITE] = ProductTrackingConstant.Tracking.CURRENT_SITE
            TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
        }

        fun eventAddToCartRecommendationATCClick(product: RecommendationItem, position: Int, isSessionActive: Boolean, pageName: String, pageTitle: String, mainProductId: String) {
            val valueLoginOrNotLogin = if (!isSessionActive) {
                " ${ProductTrackingConstant.Tracking.USER_NON_LOGIN} - "
            } else {
                ""
            }
            val listValue = ProductTrackingConstant.Tracking.LIST_PRODUCT_AFTER_ATC + pageName + ProductTrackingConstant.Tracking.LIST_RECOMMENDATION + valueLoginOrNotLogin +
                product.recommendationType + (if (product.isTopAds) " - product topads - $mainProductId" else " - $mainProductId")
            val actionValuePostfix = if (!isSessionActive) {
                " - ${ProductTrackingConstant.Tracking.USER_NON_LOGIN}"
            } else {
                ""
            }

            val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.Action.PRODUCT_CLICK,
                ProductTrackingConstant.Category.PDP_AFTER_ATC,
                ProductTrackingConstant.Action.TOPADS_ATC_CLICK + actionValuePostfix,
                pageTitle
            )

            with(ProductTrackingConstant.Tracking) {
                val products = DataLayer.listOf(
                    DataLayer.mapOf(
                        PROMO_NAME, product.name,
                        ID, product.productId.toString(),
                        PRICE, removeCurrencyPrice(product.price),
                        BRAND, DEFAULT_VALUE,
                        CATEGORY, product.categoryBreadcrumbs.toLowerCase(),
                        VARIANT, DEFAULT_VALUE,
                        PROMO_POSITION, position,
                        KEY_DIMENSION_83, if (product.isFreeOngkirActive) VALUE_BEBAS_ONGKIR else VALUE_NONE_OTHER
                    )
                )

                val actionAdd = DataLayer.mapOf(
                    ACTION_FIELD,
                    DataLayer.mapOf(LIST, listValue),
                    PRODUCTS,
                    products
                )

                mapEvent[KEY_ECOMMERCE] = DataLayer.mapOf(ProductTrackingConstant.Action.ADD, actionAdd)
            }
            TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(mapEvent)
        }

        fun eventAddToCartRecommendationWishlist(product: RecommendationItem, isSessionActive: Boolean, isAddWishlist: Boolean) {
            val valueActionPostfix = if (!isSessionActive) {
                " - ${ProductTrackingConstant.Tracking.USER_NON_LOGIN}"
            } else {
                ""
            }
            val valueActionPrefix = if (isAddWishlist) {
                "add"
            } else {
                "remove"
            }

            val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.Action.RECOMMENDATION_CLICK,
                ProductTrackingConstant.Category.PDP_AFTER_ATC,
                valueActionPrefix + ProductTrackingConstant.Action.ACTION_WISHLIST_ON_PRODUCT_RECOMMENDATION + valueActionPostfix,
                product.header
            )
            TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(mapEvent)
        }

        fun eventClickBuyerPhotosClicked(productInfo: DynamicProductInfoP1?, userId: String, componentTrackDataModel: ComponentTrackDataModel?) {
            val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.MiniSocialProof.CLICK_BUYER_PHOTOS,
                ""
            )

            mapEvent[ProductTrackingConstant.Tracking.KEY_PRODUCT_ID] = productInfo?.basic?.productID
            mapEvent[ProductTrackingConstant.Tracking.KEY_USER_ID_VARIANT] = userId
            mapEvent[ProductTrackingConstant.Tracking.KEY_ISLOGGIN] = (userId.isNotEmpty()).toString()
            mapEvent[ProductTrackingConstant.Tracking.KEY_SHOP_TYPE] = productInfo?.shopTypeString
                ?: ""
            mapEvent[ProductTrackingConstant.Tracking.KEY_SHOP_ID_SELLER] = productInfo?.basic?.shopID
                ?: ""

            TrackingUtil.addComponentTracker(mapEvent, productInfo, componentTrackDataModel, ProductTrackingConstant.MiniSocialProof.CLICK_BUYER_PHOTOS)
        }

        fun eventClickBestSeller(componentTrackDataModel: ComponentTrackDataModel, productInfo: DynamicProductInfoP1?, componentName: String, userId: String) {
            val productId = productInfo?.basic?.productID
            val categoryName = productInfo?.basic?.category?.name
            val elementName = if (componentName.isNotEmpty()) componentName else componentTrackDataModel.componentName
            val itemBundle = Bundle().apply {
                putString(ProductTrackingConstant.Tracking.KEY_EVENT, ProductTrackingConstant.Tracking.SELECT_CONTENT)
                putString(ProductTrackingConstant.Tracking.KEY_ACTION, ProductTrackingConstant.Action.CLICK_MODULAR_COMPONENT)
                putString(ProductTrackingConstant.Tracking.KEY_CATEGORY, ProductTrackingConstant.Tracking.BUSINESS_UNIT_PDP)
                putString(ProductTrackingConstant.Tracking.KEY_LABEL, String.format(ProductTrackingConstant.Label.EVENT_LABEL_CLICK_BEST_SELLER, productInfo?.bestSellerContent?.get(productInfo.basic.productID)?.linkText, productInfo?.basic?.category?.id, categoryName))
                putString(ProductTrackingConstant.Tracking.KEY_BUSINESS_UNIT, ProductTrackingConstant.Tracking.CURRENT_SITE)
                putString(ProductTrackingConstant.Tracking.KEY_COMPONENT, String.format(ProductTrackingConstant.Label.EVENT_COMPONENT_CLICK_BEST_SELLER, componentTrackDataModel.componentType, componentTrackDataModel.componentName, ProductTrackingConstant.Action.CLICK_MODULAR_COMPONENT, componentTrackDataModel.adapterPosition))
                putString(ProductTrackingConstant.Tracking.KEY_CURRENT_SITE, ProductTrackingConstant.Tracking.BUSINESS_UNIT_PDP)

                // promotion
                val bundlePromotion = Bundle().apply {
                    putString(ProductTrackingConstant.Tracking.CREATIVE, String.format(ProductTrackingConstant.Label.EVENT_CREATIVE_CLICK_BEST_SELLER, productInfo?.layoutName, componentTrackDataModel.componentType, elementName))
                    putString(ProductTrackingConstant.Tracking.ID, "")
                    putString(ProductTrackingConstant.Tracking.NAME, "${ProductTrackingConstant.Category.PDP} - $productId")
                    putInt(ProductTrackingConstant.Tracking.POSITION, componentTrackDataModel.adapterPosition)
                }
                val list = mutableListOf<Bundle>()
                list.add(bundlePromotion)
                val bundlePromotions = Bundle().apply {
                    putParcelableArrayList(ProductTrackingConstant.Tracking.KEY_PROMOTIONS, list as ArrayList<Bundle>)
                }
                // promoClick
                val bundlePromoClick = Bundle().apply {
                    putBundle(ProductTrackingConstant.Tracking.PROMO_CLICK, bundlePromotions)
                }
                putBundle(ProductTrackingConstant.Tracking.KEY_ECOMMERCE, bundlePromoClick)

                putString(ProductTrackingConstant.Tracking.KEY_LAYOUT, String.format(ProductTrackingConstant.Label.EVENT_LAYOUT_CLICK_BEST_SELLER, productInfo?.layoutName, productInfo?.basic?.category?.name, productInfo?.basic?.category?.id))
                putString(ProductTrackingConstant.Tracking.KEY_PRODUCT_ID, productId)
                putString(ProductTrackingConstant.Tracking.KEY_USER_ID_VARIANT, userId)
            }

            TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(ProductTrackingConstant.Tracking.PROMO_CLICK, itemBundle)
        }

        fun onRestrictionGamificationClicked(
            productInfo: DynamicProductInfoP1?,
            reData: RestrictionData,
            userId: String
        ) {
            val productId = productInfo?.basic?.productID
            val shopId = productInfo?.basic?.shopID ?: ""
            val shopType = productInfo?.shopTypeString ?: ""
            val re = reData.action.firstOrNull() ?: RestrictionAction()
            val eventLabel = "button:${re.buttonText}"
            val layout = "layout:${productInfo?.layoutName};catName:${productInfo?.basic?.category?.name};catId:${productInfo?.basic?.category?.id};"

            val itemBundle = Bundle().apply {
                putString(ProductTrackingConstant.Tracking.KEY_EVENT, ProductTrackingConstant.Tracking.SELECT_CONTENT)
                putString(ProductTrackingConstant.Tracking.KEY_ACTION, ProductTrackingConstant.Action.ACTION_CLICK_RESTRICTION_COMPONENT)
                putString(ProductTrackingConstant.Tracking.KEY_CATEGORY, ProductTrackingConstant.Tracking.BUSINESS_UNIT_PDP)
                putString(ProductTrackingConstant.Tracking.KEY_LABEL, eventLabel)
                putString(ProductTrackingConstant.Tracking.KEY_BUSINESS_UNIT, ProductTrackingConstant.Tracking.CURRENT_SITE)
                putString(ProductTrackingConstant.Tracking.KEY_COMPONENT, "")
                putString(ProductTrackingConstant.Tracking.KEY_CURRENT_SITE, ProductTrackingConstant.Tracking.BUSINESS_UNIT_PDP)
                putString(ProductTrackingConstant.Tracking.KEY_LAYOUT, layout)

                // promotion
                val bundlePromotion = Bundle().apply {
                    putString(ProductTrackingConstant.Tracking.CREATIVE, re.description)
                    putString(ProductTrackingConstant.Tracking.ID, re.attributeName)
                    putString(ProductTrackingConstant.Tracking.NAME, re.title)
                    putInt(ProductTrackingConstant.Tracking.POSITION, 1)
                }
                val list = mutableListOf<Bundle>()
                list.add(bundlePromotion)
                val bundlePromotions = Bundle().apply {
                    putParcelableArrayList(ProductTrackingConstant.Tracking.KEY_PROMOTIONS, list as ArrayList<Bundle>)
                }

                putBundle(ProductTrackingConstant.Tracking.KEY_ECOMMERCE, bundlePromotions)
                putString(ProductTrackingConstant.Tracking.KEY_PRODUCT_ID, productId)
                putString(ProductTrackingConstant.Tracking.KEY_USER_ID_VARIANT, userId)
                putString(ProductTrackingConstant.Tracking.KEY_PRODUCT_SHOP_ID, shopId)
                putString(ProductTrackingConstant.Tracking.KEY_PRODUCT_SHOP_TYPE, shopType)
            }

            TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(ProductTrackingConstant.Tracking.PROMO_CLICK, itemBundle)
        }
    }

    object Iris {

        fun eventDiscussionClickedIris(
            productInfo: DynamicProductInfoP1?,
            deeplinkUrl: String,
            shopName: String,
            componentTrackDataModel: ComponentTrackDataModel
        ) {
            val categoryIdLevel1 = productInfo?.basic?.category?.detail?.firstOrNull()?.id ?: ""
            val categoryNameLevel1 = productInfo?.basic?.category?.detail?.firstOrNull()?.name ?: ""

            val categoryNameLvl2 = productInfo?.basic?.category?.detail?.getOrNull(1)?.name ?: ""
            val categoryIdLvl2 = productInfo?.basic?.category?.detail?.getOrNull(1)?.id ?: ""

            val categoryIdLevel3 = productInfo?.basic?.category?.detail?.getOrNull(2)?.id ?: ""
            val categoryNameLevel3 = productInfo?.basic?.category?.detail?.getOrNull(2)?.name ?: ""

            val imageUrl = productInfo?.data?.media?.filter {
                it.type == "image"
            }?.firstOrNull()?.uRLOriginal ?: ""

            val isOs = productInfo?.data?.isOS == true

            val mapOfData = mutableMapOf(
                ProductTrackingConstant.Tracking.KEY_EVENT to ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Tracking.KEY_CATEGORY to ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Tracking.KEY_ACTION to ProductTrackingConstant.Action.CLICK_DISKUSI_PRODUCT_TAB,
                ProductTrackingConstant.Tracking.KEY_LABEL to "",
                ProductTrackingConstant.Tracking.KEY_GROUP_NAME to categoryNameLevel3,
                ProductTrackingConstant.Tracking.KEY_GROUP_ID to categoryIdLevel3,
                "subcategory" to categoryNameLvl2,
                "subcategoryId" to categoryIdLvl2,
                "category" to categoryNameLevel1,
                "categoryId" to categoryIdLevel1,
                "productName" to (productInfo?.getProductName ?: ""),
                "productId" to (productInfo?.basic?.productID ?: ""),
                "productUrl" to (productInfo?.basic?.url ?: ""),
                "productDeeplinkUrl" to deeplinkUrl,
                "productImageUrl" to imageUrl,
                "productPrice" to (productInfo?.data?.price?.value ?: ""),
                "isOfficialStore" to isOs.toString(),
                "shopId" to (productInfo?.basic?.shopID ?: ""),
                "shopName" to shopName,
                "productPriceFormatted" to TrackingUtil.getFormattedPrice(
                    productInfo?.data?.price?.value
                        ?: 0.0
                )
            ) as MutableMap<String, Any>

            TrackingUtil.addComponentTracker(mapOfData, productInfo, componentTrackDataModel, ProductTrackingConstant.Action.CLICK_DISKUSI_PRODUCT_TAB)
        }

        fun eventReviewClickedIris(
            productInfo: DynamicProductInfoP1?,
            deeplinkUrl: String,
            shopName: String
        ) {
            val categoryIdLevel1 = productInfo?.basic?.category?.detail?.firstOrNull()?.id ?: ""
            val categoryNameLevel1 = productInfo?.basic?.category?.detail?.firstOrNull()?.name ?: ""

            val categoryNameLvl2 = productInfo?.basic?.category?.detail?.getOrNull(1)?.name ?: ""
            val categoryIdLvl2 = productInfo?.basic?.category?.detail?.getOrNull(1)?.id ?: ""

            val categoryIdLevel3 = productInfo?.basic?.category?.detail?.getOrNull(2)?.id ?: ""
            val categoryNameLevel3 = productInfo?.basic?.category?.detail?.getOrNull(2)?.name ?: ""

            val imageUrl = productInfo?.data?.media?.filter {
                it.type == "image"
            }?.firstOrNull()?.uRLOriginal ?: ""

            val isOs = productInfo?.data?.isOS == true

            val mapOfData: Map<String, Any?> = mapOf(
                ProductTrackingConstant.Tracking.KEY_EVENT to "clickPDP",
                ProductTrackingConstant.Tracking.KEY_CATEGORY to "product detail page",
                ProductTrackingConstant.Tracking.KEY_ACTION to "click - review produk tab",
                ProductTrackingConstant.Tracking.KEY_LABEL to "review",
                ProductTrackingConstant.Tracking.KEY_GROUP_NAME to categoryNameLevel3,
                ProductTrackingConstant.Tracking.KEY_GROUP_ID to categoryIdLevel3,
                "subcategory" to categoryNameLvl2,
                "subcategoryId" to categoryIdLvl2,
                "category" to categoryNameLevel1,
                "categoryId" to categoryIdLevel1,
                "productName" to (productInfo?.getProductName ?: ""),
                "productId" to (productInfo?.basic?.productID ?: ""),
                "productUrl" to productInfo?.basic?.url,
                "productDeeplinkUrl" to deeplinkUrl,
                "productImageUrl" to imageUrl,
                "productPrice" to productInfo?.data?.price?.value,
                "isOfficialStore" to isOs.toString(),
                "shopId" to productInfo?.basic?.shopID,
                "shopName" to shopName,
                "productPriceFormatted" to TrackingUtil.getFormattedPrice(
                    productInfo?.data?.price?.value
                        ?: 0.0
                )
            )

            TrackApp.getInstance().gtm.sendGeneralEvent(mapOfData)
        }
    }

    object Branch {
        fun eventBranchItemView(productInfo: DynamicProductInfoP1?, userId: String?) {
            if (productInfo != null) {
                LinkerManager.getInstance().sendEvent(LinkerUtils.createGenericRequest(LinkerConstants.EVENT_ITEM_VIEW, TrackingUtil.createLinkerDataForViewItem(productInfo, userId)))
            }
        }

        fun eventBranchAddToWishlist(productInfo: DynamicProductInfoP1?, userId: String?, description: String) {
            if (productInfo != null) {
                LinkerManager.getInstance().sendEvent(LinkerUtils.createGenericRequest(LinkerConstants.EVENT_ADD_TO_WHISHLIST, TrackingUtil.createLinkerData(productInfo, userId, description)))
            }
        }
    }

    object Moengage {

        fun sendMoEngageClickReview(productInfo: DynamicProductInfoP1) {
            sendMoEngage(productInfo, "Clicked_Ulasan_Pdp")
        }

        fun sendMoEngageOpenProduct(productInfo: DynamicProductInfoP1) {
            sendMoEngage(productInfo, "Product_Page_Opened")
        }

        fun sendMoEngageClickDiskusi(productInfo: DynamicProductInfoP1) {
            sendMoEngage(productInfo, "Clicked_Diskusi_Pdp")
        }

        fun eventPDPWishlistAppsFyler(productInfo: DynamicProductInfoP1) {
            eventAppsFyler(productInfo, "af_add_to_wishlist")
        }

        fun eventAppsFylerOpenProduct(productInfo: DynamicProductInfoP1) {
            eventAppsFyler(productInfo, "af_content_view")
        }

        fun sendMoEngagePDPReferralCodeShareEvent() {
            TrackApp.getInstance().moEngage.sendEvent(
                "Share_Event",
                mutableMapOf<String, Any>(
                    "channel" to "lainnya",
                    "source" to "pdp_share"
                )
            )
        }

        private fun sendMoEngage(
            productInfo: DynamicProductInfoP1,
            eventName: String
        ) {
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
                        put("product_id", basic.productID)
                        put("product_url", basic.url)
                        put("product_price", data.price.value)
                        put("product_price_fmt", TrackingUtil.getFormattedPrice(data.price.value))
                        put("is_official_store", data.isOS)
                        put("shop_id", productInfo.basic.shopID)
                        put("shop_name", productInfo.basic.shopName)
                        put("product_image_url", data.getFirstProductImage().toString())
                    }
                )
            }
        }

        private fun eventAppsFyler(productInfo: DynamicProductInfoP1, eventName: String) {
            TrackApp.getInstance().appsFlyer.run {
                productInfo.let {
                    val mutableMap = mutableMapOf(
                        "af_description" to "productView",
                        "af_content_id" to it.basic.productID,
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
                            jsonObject.put("id", it.basic.productID)
                            jsonObject.put("quantity", 1)
                            jsonArray.put(jsonObject)
                            this["af_content"] = jsonArray.toString()
                        }
                    }

                    sendEvent(eventName, mutableMap as Map<String, Any>?)
                }
            }
        }
    }

    object Impression {
        fun eventViewErrorWhenAddToCart(errorMessage: String, productId: String, userId: String) {
            val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_VIEW_PDP,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.ACTION_VIEW_ERROR_WHEN_ADD_TO_CART,
                "not success - $errorMessage"
            )
            mapEvent[ProductTrackingConstant.Tracking.KEY_USER_ID_VARIANT] = userId
            mapEvent[ProductTrackingConstant.Tracking.KEY_ISLOGGIN] = (userId.isNotEmpty()).toString()
            mapEvent[ProductTrackingConstant.Tracking.PROMO_ID] = productId
            TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
        }

        fun eventMediaThumbnailClick(
            trackingQueue: TrackingQueue?,
            position: Int,
            userId: String,
            media: MediaDataModel,
            productInfo: DynamicProductInfoP1?,
            lcaWarehouseId: String,
            componentTrackDataModel: ComponentTrackDataModel?
        ) {
            val productId = productInfo?.basic?.productID ?: ""
            val shopId = productInfo?.basic?.shopID ?: ""
            val shopType = productInfo?.shopTypeString ?: ""

            val mediaType = "media type:${media.type}"
            val itemName = "product_id:$productId;variantOptionID:${media.variantOptionId}"
            val itemId = media.urlOriginal

            val mapEvent = DataLayer.mapOf(
                ProductTrackingConstant.Tracking.KEY_EVENT, "promoView",
                ProductTrackingConstant.Tracking.KEY_CATEGORY, ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Tracking.KEY_ACTION, "click - product media thumbnail carousel",
                ProductTrackingConstant.Tracking.KEY_LABEL, "",
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
                                "id",
                                itemId,
                                "name",
                                itemName,
                                "creative",
                                mediaType,
                                "position",
                                position
                            )
                        )
                    )
                )
            )
            mapEvent[ProductTrackingConstant.Tracking.KEY_PRODUCT_ID] = productId
            mapEvent[ProductTrackingConstant.Tracking.KEY_TRACKER_ID] = TRACKER_ID_CLICK_THUMBNAIL
            mapEvent[ProductTrackingConstant.Tracking.KEY_SHOP_ID_SELLER] = shopId
            mapEvent[ProductTrackingConstant.Tracking.KEY_SHOP_TYPE] = shopType
            mapEvent[ProductTrackingConstant.Tracking.KEY_WAREHOUSE_ID] = lcaWarehouseId
            mapEvent[ProductTrackingConstant.Tracking.KEY_LAYOUT] = "layout:${productInfo?.layoutName};catName:${productInfo?.basic?.category?.name};catId:${productInfo?.basic?.category?.id};"
            mapEvent[ProductTrackingConstant.Tracking.KEY_COMPONENT] = "comp:${componentTrackDataModel?.componentName ?: ""};temp:${componentTrackDataModel?.componentType ?: ""};elem:${"impression - modular component"};cpos:${componentTrackDataModel?.adapterPosition ?: ""};"

            trackingQueue?.putEETracking(mapEvent as HashMap<String, Any>?)
        }

        fun eventMediaThumbnailImpression(
            trackingQueue: TrackingQueue?,
            position: Int,
            userId: String,
            media: MediaDataModel,
            productInfo: DynamicProductInfoP1?,
            lcaWarehouseId: String,
            componentTrackDataModel: ComponentTrackDataModel?
        ) {
            val productId = productInfo?.basic?.productID ?: ""
            val shopId = productInfo?.basic?.shopID ?: ""
            val shopType = productInfo?.shopTypeString ?: ""

            val eventLabel = "count_media:${productInfo?.data?.media?.size ?: 0}"
            val mediaType = "media type:${media.type}"
            val itemName = "product_id:$productId;variantOptionID:${media.variantOptionId}"
            val itemId = media.urlOriginal

            val mapEvent = DataLayer.mapOf(
                ProductTrackingConstant.Tracking.KEY_EVENT, "promoView",
                ProductTrackingConstant.Tracking.KEY_CATEGORY, ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Tracking.KEY_ACTION, "impression - product media thumbnail carousel",
                ProductTrackingConstant.Tracking.KEY_LABEL, eventLabel,
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
                                "id",
                                itemId,
                                "name",
                                itemName,
                                "creative",
                                mediaType,
                                "position",
                                position
                            )
                        )
                    )
                )
            )
            mapEvent[ProductTrackingConstant.Tracking.KEY_PRODUCT_ID] = productId
            mapEvent[ProductTrackingConstant.Tracking.KEY_TRACKER_ID] = TRACKER_ID_IMPRESS_THUMBNAIL
            mapEvent[ProductTrackingConstant.Tracking.KEY_SHOP_ID_SELLER] = shopId
            mapEvent[ProductTrackingConstant.Tracking.KEY_SHOP_TYPE] = shopType
            mapEvent[ProductTrackingConstant.Tracking.KEY_WAREHOUSE_ID] = lcaWarehouseId
            mapEvent[ProductTrackingConstant.Tracking.KEY_LAYOUT] = "layout:${productInfo?.layoutName};catName:${productInfo?.basic?.category?.name};catId:${productInfo?.basic?.category?.id};"
            mapEvent[ProductTrackingConstant.Tracking.KEY_COMPONENT] = "comp:${componentTrackDataModel?.componentName ?: ""};temp:${componentTrackDataModel?.componentType ?: ""};elem:${"impression - modular component"};cpos:${componentTrackDataModel?.adapterPosition ?: ""};"

            trackingQueue?.putEETracking(mapEvent as HashMap<String, Any>?)
        }

        fun eventImpressionComponent(
            trackingQueue: TrackingQueue?,
            componentTrackDataModel: ComponentTrackDataModel,
            productInfo: DynamicProductInfoP1?,
            componentName: String,
            purchaseProtectionUrl: String,
            userId: String,
            lcaWarehouseId: String
        ) {
            val productId = productInfo?.basic?.productID ?: ""
            val elementName = componentName.ifEmpty { componentTrackDataModel.componentName }

            val mapEvent = TrackingUtil.createCommonImpressionTracker(
                productInfo = productInfo,
                componentTrackDataModel = componentTrackDataModel,
                userId = userId,
                lcaWarehouseId = lcaWarehouseId,
                customAction = "impression - modular component",
                customCreativeName = "layout:${productInfo?.layoutName};comp:$elementName;temp:${componentTrackDataModel.componentType};",
                customItemName = "product detail page - $productId",
                customLabel = "",
                customPromoCode = purchaseProtectionUrl
            )

            trackingQueue?.putEETracking(mapEvent)
        }

        fun eventImpressionShopMultilocation(
            trackingQueue: TrackingQueue?,
            componentTrackDataModel: ComponentTrackDataModel,
            productInfo: DynamicProductInfoP1?,
            shopCountLocation: String,
            userId: String,
            lcaWarehouseId: String
        ) {
            val productId = productInfo?.basic?.productID ?: ""
            val shopId = productInfo?.basic?.shopID ?: ""

            val mapEvent = TrackingUtil.createCommonImpressionTracker(
                productInfo = productInfo,
                componentTrackDataModel = componentTrackDataModel,
                userId = userId,
                lcaWarehouseId = lcaWarehouseId,
                customAction = "impression - shop info component",
                customCreativeName = "count_location:$shopCountLocation",
                customItemName = "product detail page - $productId",
                customLabel = "shop_id:$shopId",
                customPromoCode = "",
                customItemId = "shop info component - $shopId - $productId"
            )

            trackingQueue?.putEETracking(mapEvent)
        }

        private val generateProduct = { irisSessionId: String, trackerListName: String?,
            productInfo: DynamicProductInfoP1?,
            trackerAttribution: String?,
            isTradeIn: Boolean, isDiagnosed: Boolean,
            multiOrigin: Boolean, deeplinkUrl: String,
            isStockAvailable: String, boType: Int,
            affiliateUniqueId: String, uuid: String,
            ratesEstimateData: P2RatesEstimateData?,
            buyerDistrictId: String, sellerDistrictId: String ->

            val dimension10 = productInfo?.data?.isCod ?: false
            val dimension12 = ratesEstimateData?.cheapestShippingPrice?.toLong()?.toString() ?: ""
            val dimension14 = ratesEstimateData?.title ?: ""
            val dimension16 = ratesEstimateData?.etaText ?: ""
            val dimension53 = productInfo?.data?.campaign?.discountedPrice?.toLong()?.toString().orEmpty()
            val dimension55 = TrackingUtil.getTradeInString(isTradeIn, isDiagnosed)
            val dimension83 = ProductCartHelper.getBoTrackerString(boType)
            val dimension54 = TrackingUtil.getMultiOriginAttribution(multiOrigin)
            val dimension38 = trackerAttribution ?: ProductTrackingConstant.Tracking.DEFAULT_VALUE
            val dimension98 = if (isStockAvailable == "0") "not available" else "available"
            val dimension113 = if (affiliateUniqueId.isNotBlank()) "$affiliateUniqueId - $uuid" else ""
            val dimension120 = "$buyerDistrictId - $sellerDistrictId"

            val categoryFormatted = TrackingUtil.getEnhanceCategoryFormatted(productInfo?.basic?.category?.detail)

            arrayListOf(
                Product(
                    name = productInfo?.getProductName ?: "",
                    id = productInfo?.basic?.productID ?: "",
                    price = productInfo?.data?.price?.value ?: 0.0,
                    brand = productInfo?.getProductName,
                    variant = ProductTrackingConstant.Tracking.DEFAULT_VALUE,
                    category = categoryFormatted,
                    currency = null,
                    dimension10 = dimension10.toString(),
                    dimension12 = dimension12,
                    dimension14 = dimension14,
                    dimension16 = dimension16,
                    dimension38 = dimension38,
                    dimension53 = dimension53,
                    dimension55 = dimension55,
                    dimension54 = dimension54,
                    dimension83 = dimension83,
                    dimension81 = productInfo?.shopTypeString ?: "",
                    dimension98 = dimension98,
                    dimension90 = if (affiliateUniqueId.isNotBlank()) "affiliate" else null,
                    dimension113 = dimension113,
                    dimension120 = dimension120,
                    index = 1
                )
            )
        }

        private val generateProductViewBundle = { irisSessionId: String, trackerListName: String?,
            productInfo: DynamicProductInfoP1?,
            shopInfo: ShopInfo?, trackerAttribution: String?,
            isTradeIn: Boolean, isDiagnosed: Boolean,
            multiOrigin: Boolean, deeplinkUrl: String,
            isStockAvailable: String, boType: Int,
            affiliateUniqueId: String, uuid: String,
            ratesEstimateData: P2RatesEstimateData?,
            buyerDistrictId: String,
            sellerDistrictId: String,
            lcaWarehouseId: String,
            campaignId: String,
            variantId: String ->

            val categoryIdLevel1 = productInfo?.basic?.category?.detail?.firstOrNull()?.id ?: ""
            val categoryNameLevel1 = productInfo?.basic?.category?.detail?.firstOrNull()?.name ?: ""

            val subCategoryIdLevel2 = productInfo?.basic?.category?.detail?.getOrNull(1)?.id ?: ""
            val subCategoryNameLevel2 = productInfo?.basic?.category?.detail?.getOrNull(1)?.name.orEmpty()

            val categoryIdLevel3 = productInfo?.basic?.category?.detail?.getOrNull(2)?.id ?: ""
            val categoryNameLevel3 = productInfo?.basic?.category?.detail?.getOrNull(2)?.name ?: ""

            val isPmInt = if (productInfo?.data?.isPowerMerchant == true) 1 else 0
            val isOsInt = if (productInfo?.data?.isOS == true) 1 else 0

            val productImageUrl = TrackingUtil.getProductFirstImageUrl(productInfo)
            val label = TrackingUtil.getProductViewLabel(productInfo)

            val products = generateProduct(
                irisSessionId, trackerListName, productInfo,
                trackerAttribution, isTradeIn, isDiagnosed, multiOrigin, deeplinkUrl,
                isStockAvailable, boType, affiliateUniqueId, uuid, ratesEstimateData, buyerDistrictId,
                sellerDistrictId
            )

            ProductDetailViewsBundler
                .getBundle(
                    if (trackerListName?.isNotEmpty() == true) {
                        trackerListName
                    } else {
                        ""
                    },
                    products,
                    TrackingUtil.getEnhanceUrl(productInfo?.basic?.url),
                    shopInfo?.shopCore?.name,
                    productInfo?.basic?.shopID,
                    shopInfo?.shopCore?.domain,
                    shopInfo?.location,
                    isPmInt.toString(),
                    categoryIdLevel1,
                    productInfo?.shopTypeString ?: "",
                    "/productpage",
                    subCategoryNameLevel2,
                    subCategoryIdLevel2,
                    productInfo?.basic?.url,
                    deeplinkUrl,
                    productImageUrl,
                    isOsInt.toString(),
                    TrackingUtil.getFormattedPrice(productInfo?.data?.price?.value ?: 0.0),
                    productInfo?.basic?.productID ?: "",
                    "layout:${productInfo?.layoutName};catName:${productInfo?.basic?.category?.name};catId:${productInfo?.basic?.category?.id}",
                    "",
                    (productInfo?.finalPrice.orZero().toString()),
                    productInfo?.getProductName,
                    categoryNameLevel3,
                    categoryIdLevel3,
                    categoryNameLevel1,
                    irisSessionId,
                    ProductTrackingConstant.Tracking.CURRENT_SITE,
                    ProductDetailViewsBundler.KEY,
                    "product page",
                    "view product page",
                    label,
                    ProductTrackingConstant.Tracking.BUSINESS_UNIT_PDP,
                    null,
                    productInfo?.isProductVariant().toString(),
                    productInfo?.data?.campaign?.campaignID,
                    "product status:${productInfo?.basic?.status?.toLowerCase()};" + "shop status:${shopInfo?.statusInfo?.shopStatus};",
                    productInfo?.getFinalStock(),
                    trackerAttribution ?: ProductTrackingConstant.Tracking.DEFAULT_VALUE,
                    lcaWarehouseId,
                    shopInfo?.shopCore?.ownerId,
                    campaignId,
                    variantId
                )
        }

        fun eventProductView(
            productInfo: DynamicProductInfoP1?,
            shopInfo: ShopInfo?,
            irisSessionId: String,
            trackerListName: String?,
            trackerAttribution: String?,
            isTradeIn: Boolean,
            isDiagnosed: Boolean,
            multiOrigin: Boolean,
            deeplinkUrl: String,
            isStockAvailable: String,
            boType: Int,
            affiliateUniqueId: String,
            uuid: String,
            ratesEstimateData: P2RatesEstimateData?,
            buyerDistrictId: String,
            sellerDistrictId: String,
            lcaWarehouseId: String,
            campaignId: String,
            variantId: String
        ) {
            productInfo?.let {
                if (shopInfo?.isShopInfoNotEmpty() == true) {
                    val sentBundle = generateProductViewBundle(
                        irisSessionId, trackerListName, it, shopInfo,
                        trackerAttribution, isTradeIn, isDiagnosed, multiOrigin, deeplinkUrl,
                        isStockAvailable, boType, affiliateUniqueId, uuid, ratesEstimateData,
                        buyerDistrictId, sellerDistrictId, lcaWarehouseId, campaignId, variantId
                    )
                    sendTrackingBundle(
                        ProductDetailViewsBundler.KEY,
                        sentBundle
                    )
                }
            }
        }

        fun eventRecommendationImpression(
            trackingQueue: TrackingQueue?,
            position: Int,
            product: RecommendationItem,
            chipValue: String,
            isComparison: Boolean,
            isSessionActive: Boolean,
            pageName: String,
            pageTitle: String,
            productInfo: DynamicProductInfoP1?,
            componentTrackDataModel: ComponentTrackDataModel
        ) {
            val listValue = ProductTrackingConstant.Tracking.LIST_DEFAULT + pageName +
                (if (!isSessionActive) " - ${ProductTrackingConstant.Tracking.USER_NON_LOGIN}" else "") +
                ProductTrackingConstant.Tracking.LIST_RECOMMENDATION + product.recommendationType + (if (product.isTopAds) " - product topads" else "") +
                (if (isComparison) ProductTrackingConstant.TopAds.RECOMMENDATION_COMPARISON else ProductTrackingConstant.TopAds.RECOMMENDATION_CAROUSELL) + " - " + (
                productInfo?.parentProductId
                    ?: ""
                )
            val topAdsAction = ProductTrackingConstant.Action.TOPADS_IMPRESSION + (if (!isSessionActive) " - ${ProductTrackingConstant.Tracking.USER_NON_LOGIN}" else "")
            val bebasOngkirValue = if (product.isFreeOngkirActive && product.labelGroupList.hasLabelGroupFulfillment()) {
                ProductTrackingConstant.Tracking.VALUE_BEBAS_ONGKIR_EXTRA
            } else if (product.isFreeOngkirActive && !product.labelGroupList.hasLabelGroupFulfillment()) {
                ProductTrackingConstant.Tracking.VALUE_BEBAS_ONGKIR
            } else {
                ProductTrackingConstant.Tracking.VALUE_NONE_OTHER
            }
            val enhanceEcommerceData = DataLayer.mapOf(
                ProductTrackingConstant.Tracking.KEY_EVENT, ProductTrackingConstant.Action.PRODUCT_VIEW,
                ProductTrackingConstant.Tracking.KEY_CATEGORY, ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Tracking.KEY_ACTION, topAdsAction,
                ProductTrackingConstant.Tracking.KEY_LABEL, "$pageTitle-$chipValue",
                ProductTrackingConstant.Tracking.KEY_PRODUCT_ID,
                productInfo?.basic?.productID.orEmpty(),
                ProductTrackingConstant.Tracking.KEY_LAYOUT, "layout:${productInfo?.layoutName};catName:${productInfo?.basic?.category?.name};catId:${productInfo?.basic?.category?.id};",
                ProductTrackingConstant.Tracking.KEY_COMPONENT, "comp:${componentTrackDataModel.componentName};temp:${componentTrackDataModel.componentType};elem:$topAdsAction;cpos:${componentTrackDataModel.adapterPosition};",
                ProductTrackingConstant.Tracking.KEY_ECOMMERCE,
                DataLayer.mapOf(
                    ProductTrackingConstant.Tracking.CURRENCY_CODE,
                    ProductTrackingConstant.Tracking.CURRENCY_DEFAULT_VALUE,
                    ProductTrackingConstant.Tracking.IMPRESSIONS,
                    DataLayer.listOf(
                        DataLayer.mapOf(
                            ProductTrackingConstant.Tracking.PROMO_NAME, product.name,
                            ProductTrackingConstant.Tracking.ID, product.productId.toString(),
                            ProductTrackingConstant.Tracking.PRICE, TrackingUtil.removeCurrencyPrice(product.price),
                            ProductTrackingConstant.Tracking.BRAND, ProductTrackingConstant.Tracking.DEFAULT_VALUE,
                            ProductTrackingConstant.Tracking.VARIANT, ProductTrackingConstant.Tracking.DEFAULT_VALUE,
                            ProductTrackingConstant.Tracking.CATEGORY, product.categoryBreadcrumbs.toLowerCase(),
                            ProductTrackingConstant.Tracking.PROMO_POSITION, position + 1,
                            ProductTrackingConstant.Tracking.LIST, listValue,
                            ProductTrackingConstant.Tracking.KEY_DIMENSION_83, bebasOngkirValue,
                            ProductTrackingConstant.Tracking.KEY_PRODUCT_ID, product.productId.toString()
                        )
                    )
                )
            )
            trackingQueue?.putEETracking(enhanceEcommerceData as HashMap<String, Any>?)
        }

        fun eventPurchaseProtectionAvailable(userId: String, productInfo: DynamicProductInfoP1?, insuranceBrand: String) {
            val categoryIdLevel3 = productInfo?.basic?.category?.detail?.getOrNull(2)?.id ?: ""

            val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.Action.PRODUCT_VIEW,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.ACTION_PP_INSURANCE,
                "${productInfo?.basic?.productID ?: ""} - $categoryIdLevel3 - $insuranceBrand"
            )
            mapEvent[ProductTrackingConstant.Tracking.KEY_USER_ID] = userId
            mapEvent[ProductTrackingConstant.Tracking.PRODUCT_PRICE] = productInfo?.data?.price?.value ?: ""

            TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
        }

        fun eventTopAdsImageViewImpression(trackingQueue: TrackingQueue, userId: String, bannerId: String, position: Int, bannerName: String) {
            val mapEvent = hashMapOf<String, Any>(
                ProductTrackingConstant.Tracking.KEY_EVENT to ProductTrackingConstant.Tracking.PROMO_VIEW,
                ProductTrackingConstant.Tracking.KEY_CATEGORY to ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Tracking.KEY_ACTION to ProductTrackingConstant.Action.VIEW_TDN_BANNER_ADS_WIDGET,
                ProductTrackingConstant.Tracking.KEY_LABEL to "",
                ProductTrackingConstant.Tracking.KEY_BUSINESS_UNIT to ProductTrackingConstant.Tracking.BUSINESS_UNIT,
                ProductTrackingConstant.Tracking.KEY_CURRENT_SITE to ProductTrackingConstant.Tracking.CURRENT_SITE,
                ProductTrackingConstant.Tracking.KEY_DISCUSSION_USER_ID to userId,
                ProductTrackingConstant.Tracking.KEY_SCREEN_NAME to ProductTrackingConstant.Tracking.PRODUCT_DETAIL_SCREEN_NAME,
                ProductTrackingConstant.Tracking.KEY_ECOMMERCE to hashMapOf(
                    ProductTrackingConstant.Tracking.PROMO_VIEW to hashMapOf(
                        ProductTrackingConstant.Tracking.KEY_PROMOTIONS to listOf(
                            hashMapOf(
                                ProductTrackingConstant.Tracking.ID to bannerId,
                                ProductTrackingConstant.Tracking.NAME to ProductTrackingConstant.TopAds.PDP_BANNER_TOPADS,
                                ProductTrackingConstant.Tracking.CREATIVE to
                                    if (bannerName.isEmpty()) ProductTrackingConstant.TopAds.DFAULT_CREATIVE_NAME_BANNER_TOP_ADS else bannerName,
                                ProductTrackingConstant.Tracking.POSITION to position
                            )
                        )
                    )
                )
            )

            trackingQueue.putEETracking(mapEvent)
        }

        fun eventTickerImpression(tickerType: Int, tickerTitle: String, tickerMessage: String) {
            val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_VIEW_PDP_IRIS,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.VIEW_TICKER_OOS,
                String.format(ProductTrackingConstant.Label.TICKER_OOS, TrackingUtil.getTickerTypeInfoString(tickerType), tickerTitle, tickerMessage)
            )
            TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
        }
    }

    object TradeIn {
        fun eventAddToCartFinalPrice(phoneType: String, phonePrice: String, deviceId: String, userId: String, productInfo: DynamicProductInfoP1?) {
            val productId = productInfo?.basic?.productID ?: ""
            val shopId = productInfo?.basic?.shopID ?: ""
            val productName = productInfo?.data?.name ?: ""
            val productPrice = productInfo?.finalPrice.toString()
            val shopType = productInfo?.shopTypeString ?: ""
            val shopName = productInfo?.basic?.shopName ?: ""

            val categoryName = productInfo?.basic?.category?.detail?.map {
                it.name
            }?.joinToString("/", postfix = "/ ${productInfo.basic.category.id}") ?: ""

            TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                    ProductTrackingConstant.Tracking.KEY_EVENT, "addToCart",
                    ProductTrackingConstant.Tracking.KEY_CATEGORY, "harga final trade in",
                    ProductTrackingConstant.Tracking.KEY_ACTION, "click beli sekarang",
                    ProductTrackingConstant.Tracking.KEY_LABEL, "phone type : $phoneType - phone price : $phonePrice - diagnostic id : $deviceId",
                    ProductTrackingConstant.Tracking.KEY_PRODUCT_ID, productId,
                    ProductTrackingConstant.Tracking.KEY_SCREEN_NAME, "trade in - final price page",
                    ProductTrackingConstant.Tracking.KEY_CURRENT_SITE, ProductTrackingConstant.Tracking.CURRENT_SITE,
                    ProductTrackingConstant.Tracking.KEY_USER_ID, userId,
                    ProductTrackingConstant.Tracking.KEY_BUSINESS_UNIT, "trade-in",
                    ProductTrackingConstant.Tracking.KEY_ISLOGGIN, (userId.isNotEmpty()).toString(),
                    ProductTrackingConstant.Tracking.KEY_ECOMMERCE,
                    DataLayer.mapOf(
                        ProductTrackingConstant.Tracking.CURRENCY_CODE,
                        ProductTrackingConstant.Tracking.CURRENCY_DEFAULT_VALUE,
                        ProductTrackingConstant.Tracking.KEY_ADD,
                        DataLayer.mapOf(
                            ProductTrackingConstant.Tracking.PRODUCTS,
                            DataLayer.listOf(
                                DataLayer.mapOf(
                                    ProductTrackingConstant.Tracking.NAME, productName,
                                    ProductTrackingConstant.Tracking.ID, productId,
                                    ProductTrackingConstant.Tracking.PRICE, productPrice,
                                    ProductTrackingConstant.Tracking.BRAND, ProductTrackingConstant.Tracking.VALUE_NONE_OTHER,
                                    ProductTrackingConstant.Tracking.CATEGORY, categoryName,
                                    ProductTrackingConstant.Tracking.VARIANT, ProductTrackingConstant.Tracking.VALUE_NONE_OTHER,
                                    ProductTrackingConstant.Tracking.QUANTITY, "1",
                                    ProductTrackingConstant.Tracking.KEY_DIMENSION_79, shopId,
                                    ProductTrackingConstant.Tracking.KEY_DIMENSION_81, shopType,
                                    ProductTrackingConstant.Tracking.KEY_DIMENSION_80, shopName
                                )
                            )
                        )
                    )
                )
            )
        }
    }

    object Recommendation {

        fun eventAddToCartRecommendationImpression(position: Int, product: RecommendationItem, isSessionActive: Boolean, pageName: String, pageTitle: String, mainProductId: String, trackingQueue: TrackingQueue) {
            val valueLoginOrNotLogin = if (!isSessionActive) {
                " ${ProductTrackingConstant.Tracking.USER_NON_LOGIN} - "
            } else {
                ""
            }
            val listValue = ProductTrackingConstant.Tracking.LIST_PRODUCT_AFTER_ATC + pageName + ProductTrackingConstant.Tracking.LIST_RECOMMENDATION + valueLoginOrNotLogin +
                product.recommendationType + (if (product.isTopAds) " - product topads - $mainProductId" else " - $mainProductId")
            val valueActionPostfix = if (!isSessionActive) {
                " - ${ProductTrackingConstant.Tracking.USER_NON_LOGIN}"
            } else {
                ""
            }

            val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.Action.PRODUCT_VIEW,
                ProductTrackingConstant.Category.PDP_AFTER_ATC,
                ProductTrackingConstant.Action.TOPADS_IMPRESSION + valueActionPostfix,
                pageTitle
            )
            val bebasOngkirValue = if (product.isFreeOngkirActive && product.labelGroupList.hasLabelGroupFulfillment()) {
                ProductTrackingConstant.Tracking.VALUE_BEBAS_ONGKIR_EXTRA
            } else if (product.isFreeOngkirActive && !product.labelGroupList.hasLabelGroupFulfillment()) {
                ProductTrackingConstant.Tracking.VALUE_BEBAS_ONGKIR
            } else {
                ProductTrackingConstant.Tracking.VALUE_NONE_OTHER
            }
            with(ProductTrackingConstant.Tracking) {
                val impressions = DataLayer.listOf(
                    DataLayer.mapOf(
                        PROMO_NAME, product.name,
                        ID, product.productId.toString(),
                        PRICE, removeCurrencyPrice(product.price),
                        BRAND, DEFAULT_VALUE,
                        CATEGORY, product.categoryBreadcrumbs.toLowerCase(),
                        VARIANT, DEFAULT_VALUE,
                        LIST, listValue,
                        PROMO_POSITION, position,
                        KEY_DIMENSION_83, bebasOngkirValue
                    )
                )

                mapEvent[KEY_ECOMMERCE] = DataLayer.mapOf(
                    CURRENCY_CODE,
                    CURRENCY_DEFAULT_VALUE,
                    IMPRESSIONS,
                    impressions
                )
            }

            trackingQueue.putEETracking(mapEvent as HashMap<String, Any>?)
        }
    }

    object ProductDetailSheet {
        fun onCustomInfoPalugadaBottomSheetClicked() {
            val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PG,
                ProductTrackingConstant.Category.PDP_DETAIL_BOTTOMSHEET,
                ProductTrackingConstant.Action.CLICK_CUSTOM_INFO_HAMPERS_BOTTOM_SHEET,
                ""
            )
            mapEvent[ProductTrackingConstant.Tracking.KEY_BUSINESS_UNIT] = ProductTrackingConstant.Tracking.BUSINESS_UNIT_PDP
            mapEvent[ProductTrackingConstant.Tracking.KEY_CURRENT_SITE] = ProductTrackingConstant.Tracking.CURRENT_SITE

            TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
        }

        fun onVariantGuideLineBottomSheetClicked(productInfo: DynamicProductInfoP1?, userId: String) {
            val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.CLICK_VARIANT_GUIDELINE_BOTTOM_SHEET,
                "show"
            )
            mapEvent[ProductTrackingConstant.Tracking.KEY_PRODUCT_ID] = productInfo?.basic?.productID.orEmpty()
            mapEvent[ProductTrackingConstant.Tracking.KEY_SHOP_TYPE] = productInfo?.shopTypeString.orEmpty()
            mapEvent[ProductTrackingConstant.Tracking.KEY_SHOP_ID_SELLER] = productInfo?.basic?.shopID.orEmpty()
            mapEvent[ProductTrackingConstant.Tracking.KEY_USER_ID_VARIANT] = userId
            mapEvent[ProductTrackingConstant.Tracking.KEY_ISLOGGIN] = (userId.isNotEmpty()).toString()

            TrackingUtil.addComponentTracker(mapEvent, productInfo, null, ProductTrackingConstant.Action.CLICK_VARIANT_GUIDELINE_BOTTOM_SHEET)
        }

        fun onShopNotesClicked(productInfo: DynamicProductInfoP1?, userId: String, shopNotesTitle: String) {
            val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.CLICK_SHOP_NOTES_BOTTOM_SHEET,
                "informasi:$shopNotesTitle"
            )

            mapEvent[ProductTrackingConstant.Tracking.KEY_PRODUCT_ID] = productInfo?.basic?.productID.orEmpty()
            mapEvent[ProductTrackingConstant.Tracking.KEY_SHOP_TYPE] = productInfo?.shopTypeString.orEmpty()
            mapEvent[ProductTrackingConstant.Tracking.KEY_SHOP_ID_SELLER] = productInfo?.basic?.shopID.orEmpty()
            mapEvent[ProductTrackingConstant.Tracking.KEY_USER_ID_VARIANT] = userId
            mapEvent[ProductTrackingConstant.Tracking.KEY_ISLOGGIN] = (userId.isNotEmpty()).toString()

            TrackingUtil.addComponentTracker(mapEvent, productInfo, null, ProductTrackingConstant.Action.CLICK_SHOP_NOTES_BOTTOM_SHEET)
        }

        fun onSpecificationClick(productInfo: DynamicProductInfoP1?, userId: String) {
            val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.CLICK_SPECIFICATION_BOTTOM_SHEET,
                ""
            )

            mapEvent[ProductTrackingConstant.Tracking.KEY_PRODUCT_ID] = productInfo?.basic?.productID.orEmpty()
            mapEvent[ProductTrackingConstant.Tracking.KEY_SHOP_TYPE] = productInfo?.shopTypeString.orEmpty()
            mapEvent[ProductTrackingConstant.Tracking.KEY_SHOP_ID_SELLER] = productInfo?.basic?.shopID.orEmpty()
            mapEvent[ProductTrackingConstant.Tracking.KEY_USER_ID_VARIANT] = userId
            mapEvent[ProductTrackingConstant.Tracking.KEY_ISLOGGIN] = (userId.isNotEmpty()).toString()

            TrackingUtil.addComponentTracker(mapEvent, productInfo, null, ProductTrackingConstant.Action.CLICK_SPECIFICATION_BOTTOM_SHEET)
        }

        fun onCategoryBottomSheetClicked(productInfo: DynamicProductInfoP1?, userId: String) {
            val categoryId = productInfo?.basic?.category?.detail?.lastOrNull()?.id ?: ""
            val categoryName = productInfo?.basic?.category?.detail?.lastOrNull()?.name ?: ""

            val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.CLICK_CATEGORY_BOTTOM_SHEET,
                "category name:$categoryName; category id:$categoryId"
            )

            mapEvent[ProductTrackingConstant.Tracking.KEY_PRODUCT_ID] = productInfo?.basic?.productID.orEmpty()
            mapEvent[ProductTrackingConstant.Tracking.KEY_SHOP_TYPE] = productInfo?.shopTypeString.orEmpty()
            mapEvent[ProductTrackingConstant.Tracking.KEY_SHOP_ID_SELLER] = productInfo?.basic?.shopID.orEmpty()
            mapEvent[ProductTrackingConstant.Tracking.KEY_USER_ID_VARIANT] = userId
            mapEvent[ProductTrackingConstant.Tracking.KEY_ISLOGGIN] = (userId.isNotEmpty()).toString()

            TrackingUtil.addComponentTracker(mapEvent, productInfo, null, ProductTrackingConstant.Action.CLICK_CATEGORY_BOTTOM_SHEET)
        }

        fun onCatalogBottomSheetClicked(productInfo: DynamicProductInfoP1?, userId: String, catalogName: String) {
            val categoryName = productInfo?.basic?.catalogID ?: ""

            val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.CLICK_CATALOG_BOTTOM_SHEET,
                "catalog_id:$categoryName; catalog_name:$catalogName"
            )

            mapEvent[ProductTrackingConstant.Tracking.KEY_PRODUCT_ID] = productInfo?.basic?.productID.orEmpty()

            TrackingUtil.addComponentTracker(mapEvent, productInfo, null, ProductTrackingConstant.Action.CLICK_CATALOG_BOTTOM_SHEET)
        }

        fun onEtalaseBottomSheetClicked(productInfo: DynamicProductInfoP1?, userId: String) {
            val etalaseId = productInfo?.basic?.menu?.id ?: ""
            val etalaseName = productInfo?.basic?.menu?.name ?: ""

            val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.CLICK_ETALASE_BOTTOM_SHEET,
                "etalase name:$etalaseName; etalase id:$etalaseId"
            )

            mapEvent[ProductTrackingConstant.Tracking.KEY_PRODUCT_ID] = productInfo?.basic?.productID.orEmpty()
            mapEvent[ProductTrackingConstant.Tracking.KEY_SHOP_TYPE] = productInfo?.shopTypeString.orEmpty()
            mapEvent[ProductTrackingConstant.Tracking.KEY_SHOP_ID_SELLER] = productInfo?.basic?.shopID.orEmpty()
            mapEvent[ProductTrackingConstant.Tracking.KEY_USER_ID_VARIANT] = userId
            mapEvent[ProductTrackingConstant.Tracking.KEY_ISLOGGIN] = (userId.isNotEmpty()).toString()

            TrackingUtil.addComponentTracker(mapEvent, productInfo, null, ProductTrackingConstant.Action.CLICK_ETALASE_BOTTOM_SHEET)
        }

        fun onWriteDiscussionSheetClicked(productInfo: DynamicProductInfoP1?, userId: String) {
            val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.CLICK_WRITE_DISCUSSION_BOTTOM_SHEET,
                ""
            )

            mapEvent[ProductTrackingConstant.Tracking.KEY_PRODUCT_ID] = productInfo?.basic?.productID.orEmpty()
            mapEvent[ProductTrackingConstant.Tracking.KEY_SHOP_TYPE] = productInfo?.shopTypeString.orEmpty()
            mapEvent[ProductTrackingConstant.Tracking.KEY_SHOP_ID_SELLER] = productInfo?.basic?.shopID.orEmpty()
            mapEvent[ProductTrackingConstant.Tracking.KEY_USER_ID_VARIANT] = userId
            mapEvent[ProductTrackingConstant.Tracking.KEY_ISLOGGIN] = (userId.isNotEmpty()).toString()

            TrackingUtil.addComponentTracker(mapEvent, productInfo, null, ProductTrackingConstant.Action.CLICK_WRITE_DISCUSSION_BOTTOM_SHEET)
        }

        fun onCheckDiscussionSheetClicked(productInfo: DynamicProductInfoP1?, userId: String) {
            val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.CLICK_CHECK_DISCUSSION_BOTTOM_SHEET,
                ""
            )

            mapEvent[ProductTrackingConstant.Tracking.KEY_PRODUCT_ID] = productInfo?.basic?.productID.orEmpty()
            mapEvent[ProductTrackingConstant.Tracking.KEY_SHOP_TYPE] = productInfo?.shopTypeString.orEmpty()
            mapEvent[ProductTrackingConstant.Tracking.KEY_SHOP_ID_SELLER] = productInfo?.basic?.shopID.orEmpty()
            mapEvent[ProductTrackingConstant.Tracking.KEY_USER_ID_VARIANT] = userId
            mapEvent[ProductTrackingConstant.Tracking.KEY_ISLOGGIN] = (userId.isNotEmpty()).toString()

            TrackingUtil.addComponentTracker(mapEvent, productInfo, null, ProductTrackingConstant.Action.CLICK_CHECK_DISCUSSION_BOTTOM_SHEET)
        }
    }

    object ImpulsiveBanner {
        fun impressImpulsiveBanner(widget: RecommendationWidget, userId: String, productId: String, templateNameType: String, basicData: ProductRecomLayoutBasicData) {
            val mapEvent = DataLayer.mapOf(
                ProductTrackingConstant.Tracking.KEY_EVENT, ProductTrackingConstant.Tracking.PROMO_VIEW,
                ProductTrackingConstant.Tracking.KEY_ACTION, ProductTrackingConstant.ImpulsiveBanner.IMPRESSION_BANNER,
                ProductTrackingConstant.Tracking.KEY_CATEGORY, ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Tracking.KEY_LABEL, "",
                ProductTrackingConstant.Tracking.KEY_BUSINESS_UNIT, ProductTrackingConstant.Tracking.BUSINESS_UNIT_PDP,
                ProductTrackingConstant.Tracking.KEY_COMPONENT, ProductTrackingConstant.ImpulsiveBanner.EVENT_COMPONENT_IMPRESSION_BANNER.format(widget.pageName, templateNameType, ProductTrackingConstant.ImpulsiveBanner.IMPRESSION_BANNER, 1),
                ProductTrackingConstant.Tracking.KEY_CURRENT_SITE, ProductTrackingConstant.Tracking.CURRENT_SITE,
                ProductTrackingConstant.Tracking.KEY_LAYOUT, ProductTrackingConstant.ImpulsiveBanner.EVENT_LAYOUT_IMPRESSION_BANNER.format(basicData.generalLayoutName, basicData.categoryName, basicData.categoryId),
                ProductTrackingConstant.Tracking.KEY_PRODUCT_ID, productId,
                ProductTrackingConstant.Tracking.KEY_USER_ID, userId,
                ProductTrackingConstant.Tracking.KEY_ECOMMERCE,
                DataLayer.mapOf(
                    ProductTrackingConstant.Tracking.PROMO_VIEW,
                    DataLayer.mapOf(
                        ProductTrackingConstant.Tracking.KEY_PROMOTIONS,
                        DataLayer.listOf(
                            DataLayer.mapOf(
                                ProductTrackingConstant.Tracking.CREATIVE,
                                ProductTrackingConstant.ImpulsiveBanner.CREATIVE_NAME,
                                ProductTrackingConstant.Tracking.ID,
                                widget.recommendationBanner?.thematicID ?: "",
                                ProductTrackingConstant.Tracking.NAME,
                                ProductTrackingConstant.ImpulsiveBanner.CREATIVE_BUILDER.format(widget.layoutType, widget.title),
                                ProductTrackingConstant.Tracking.POSITION,
                                1
                            )
                        )
                    )
                )
            )
            TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(mapEvent)
        }

        fun clickImpulsiveBanner(widget: RecommendationWidget, userId: String, productId: String, templateNameType: String, basicData: ProductRecomLayoutBasicData) {
            val mapEvent = DataLayer.mapOf(
                ProductTrackingConstant.Tracking.KEY_EVENT, ProductTrackingConstant.Tracking.PROMO_CLICK,
                ProductTrackingConstant.Tracking.KEY_ACTION, ProductTrackingConstant.ImpulsiveBanner.CLICK_BANNER,
                ProductTrackingConstant.Tracking.KEY_CATEGORY, ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Tracking.KEY_LABEL, "",
                ProductTrackingConstant.Tracking.KEY_BUSINESS_UNIT, ProductTrackingConstant.Tracking.BUSINESS_UNIT_PDP,
                ProductTrackingConstant.Tracking.KEY_COMPONENT, ProductTrackingConstant.ImpulsiveBanner.EVENT_COMPONENT_IMPRESSION_BANNER.format(widget.pageName, templateNameType, ProductTrackingConstant.ImpulsiveBanner.CLICK_BANNER, 1),
                ProductTrackingConstant.Tracking.KEY_CURRENT_SITE, ProductTrackingConstant.Tracking.CURRENT_SITE,
                ProductTrackingConstant.Tracking.KEY_LAYOUT, ProductTrackingConstant.ImpulsiveBanner.EVENT_LAYOUT_IMPRESSION_BANNER.format(basicData.generalLayoutName, basicData.categoryName, basicData.categoryId),
                ProductTrackingConstant.Tracking.KEY_PRODUCT_ID, productId,
                ProductTrackingConstant.Tracking.KEY_USER_ID, userId,
                ProductTrackingConstant.Tracking.KEY_ECOMMERCE,
                DataLayer.mapOf(
                    ProductTrackingConstant.Tracking.PROMO_CLICK,
                    DataLayer.mapOf(
                        ProductTrackingConstant.Tracking.KEY_PROMOTIONS,
                        DataLayer.listOf(
                            DataLayer.mapOf(
                                ProductTrackingConstant.Tracking.CREATIVE,
                                ProductTrackingConstant.ImpulsiveBanner.CREATIVE_NAME,
                                ProductTrackingConstant.Tracking.ID,
                                widget.recommendationBanner?.thematicID ?: "",
                                ProductTrackingConstant.Tracking.NAME,
                                ProductTrackingConstant.ImpulsiveBanner.CREATIVE_BUILDER.format(widget.layoutType, widget.title),
                                ProductTrackingConstant.Tracking.POSITION,
                                1
                            )
                        )
                    )
                )
            )
            TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(mapEvent)
        }
    }

    object BottomSheetErrorShipment {
        fun impressShipmentErrorBottomSheet(productInfo: DynamicProductInfoP1?, userId: String, bottomSheetTitle: String) {
            val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.VIEW_SHIPMENT_ERROR_BOTTOM_SHEET,
                String.format(ProductTrackingConstant.Label.VIEW_LABEL_CLICK_SHIPMENT_ERROR_BOTTOM_SHEET, bottomSheetTitle)
            )
            mapEvent[ProductTrackingConstant.Tracking.KEY_BUSINESS_UNIT] = ProductTrackingConstant.Tracking.BUSINESS_UNIT_PDP
            mapEvent[ProductTrackingConstant.Tracking.KEY_CURRENT_SITE] = ProductTrackingConstant.Tracking.CURRENT_SITE
            mapEvent[ProductTrackingConstant.Tracking.KEY_USER_ID_VARIANT] = userId
            mapEvent[ProductTrackingConstant.Tracking.KEY_SHOP_ID_SELLER] = productInfo?.basic?.shopID.orEmpty()

            TrackingUtil.addComponentTracker(mapEvent, productInfo, null, ProductTrackingConstant.Action.VIEW_SHIPMENT_ERROR_BOTTOM_SHEET)
        }

        fun eventClickButtonShipmentErrorBottomSheet(productInfo: DynamicProductInfoP1?, userId: String, bottomSheetTitle: String, errorCode: Int) {
            val eventAction = if (errorCode == ProductDetailCommonConstant.SHIPPING_ERROR_WEIGHT) {
                ProductTrackingConstant.Action.CLICK_BUTTON_SHIPMENT_ERROR_BOTTOM_SHEET_CHAT
            } else {
                ProductTrackingConstant.Action.CLICK_BUTTON_SHIPMENT_ERROR_BOTTOM_SHEET_CHOOSE_ADDRESS
            }

            val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                eventAction,
                String.format(ProductTrackingConstant.Label.VIEW_LABEL_CLICK_SHIPMENT_ERROR_BOTTOM_SHEET, bottomSheetTitle)
            )
            mapEvent[ProductTrackingConstant.Tracking.KEY_BUSINESS_UNIT] = ProductTrackingConstant.Tracking.BUSINESS_UNIT_PDP
            mapEvent[ProductTrackingConstant.Tracking.KEY_CURRENT_SITE] = ProductTrackingConstant.Tracking.CURRENT_SITE
            mapEvent[ProductTrackingConstant.Tracking.KEY_USER_ID_VARIANT] = userId
            mapEvent[ProductTrackingConstant.Tracking.KEY_SHOP_ID_SELLER] = productInfo?.basic?.shopID.orEmpty()

            TrackingUtil.addComponentTracker(mapEvent, productInfo, null, eventAction)
        }
    }

    object ProductBundling {

        fun eventImpressionProductBundling(
            userId: String,
            bundleId: String,
            bundleType: String,
            productInfo: DynamicProductInfoP1?,
            componentTrackDataModel: ComponentTrackDataModel,
            trackingQueue: TrackingQueue
        ) {
            val action = ProductTrackingConstant.Action.IMPRESSION_PRODUCT_BUNDLING
            val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.Tracking.PROMO_VIEW,
                ProductTrackingConstant.Category.PDP,
                action,
                String.format(
                    ProductTrackingConstant.Label.VIEW_LABEL_PRODUCT_BUNDLING,
                    bundleId,
                    bundleType.toLowerCase(Locale.ROOT)
                )
            )

            val productId = productInfo?.basic?.productID ?: ""
            val layout = productInfo?.layoutName
            val temp = componentTrackDataModel.componentType
            val comp = componentTrackDataModel.componentName
            val cpos = componentTrackDataModel.adapterPosition

            mapEvent[ProductTrackingConstant.Tracking.KEY_PRODUCT_ID] = productId
            mapEvent[ProductTrackingConstant.Tracking.KEY_LAYOUT] = "layout:$layout;catName:${productInfo?.basic?.category?.name};catId:${productInfo?.basic?.category?.id};"
            mapEvent[ProductTrackingConstant.Tracking.KEY_COMPONENT] = "comp:$comp;temp:$temp;elem:$action;cpos:$cpos;"
            mapEvent[ProductTrackingConstant.Tracking.KEY_BUSINESS_UNIT] = ProductTrackingConstant.Tracking.BUSINESS_UNIT_PDP
            mapEvent[ProductTrackingConstant.Tracking.KEY_CURRENT_SITE] = ProductTrackingConstant.Tracking.CURRENT_SITE
            mapEvent[ProductTrackingConstant.Tracking.KEY_USER_ID] = userId

            val ecommerce = DataLayer.mapOf(
                ProductTrackingConstant.Tracking.PROMO_VIEW,
                DataLayer.mapOf(
                    ProductTrackingConstant.Tracking.KEY_PROMOTIONS,
                    DataLayer.listOf(
                        DataLayer.mapOf(
                            ProductTrackingConstant.Tracking.CREATIVE,
                            "layout:$layout;comp:$comp;temp:$temp;",
                            ProductTrackingConstant.Tracking.ID,
                            "",
                            ProductTrackingConstant.Tracking.NAME,
                            "product detail page - $productId",
                            ProductTrackingConstant.Tracking.POSITION,
                            "$cpos"
                        )
                    )
                )
            )
            mapEvent[ProductTrackingConstant.Tracking.KEY_ECOMMERCE] = ecommerce

            trackingQueue.putEETracking(HashMap(mapEvent))
        }

        fun eventClickMultiBundleProduct(
            bundleId: String,
            bundleProductId: String,
            productInfo: DynamicProductInfoP1?,
            componentTrackDataModel: ComponentTrackDataModel
        ) {
            val action = ProductTrackingConstant.Action.CLICK_PRODUCT_BUNDLING
            val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                action,
                String.format(
                    ProductTrackingConstant.Label.EVENT_LABEL_CLICK_PRODUCT_BUNDLING_MULTIPLE,
                    bundleProductId,
                    bundleId
                )
            )
            mapEvent[ProductTrackingConstant.Tracking.KEY_BUSINESS_UNIT] = ProductTrackingConstant.Tracking.BUSINESS_UNIT_PDP
            mapEvent[ProductTrackingConstant.Tracking.KEY_CURRENT_SITE] = ProductTrackingConstant.Tracking.CURRENT_SITE
            TrackingUtil.addComponentTracker(mapEvent, productInfo, componentTrackDataModel, action)
        }

        fun eventClickCheckBundlePage(
            bundleId: String,
            bundleType: String,
            productInfo: DynamicProductInfoP1?,
            componentTrackDataModel: ComponentTrackDataModel
        ) {
            val action = ProductTrackingConstant.Action.CLICK_CHECK_PRODUCT_BUNDLING
            val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                action,
                String.format(
                    ProductTrackingConstant.Label.EVENT_LABEL_CLICK_CHECK_PRODUCT_BUNDLING,
                    bundleId,
                    bundleType.toLowerCase(Locale.ROOT)
                )
            )
            mapEvent[ProductTrackingConstant.Tracking.KEY_BUSINESS_UNIT] = ProductTrackingConstant.Tracking.BUSINESS_UNIT_PDP
            mapEvent[ProductTrackingConstant.Tracking.KEY_CURRENT_SITE] = ProductTrackingConstant.Tracking.CURRENT_SITE
            TrackingUtil.addComponentTracker(mapEvent, productInfo, componentTrackDataModel, action)
        }
    }

    object ViewToView {
        fun eventImpressViewToView(
            position: Int,
            product: RecommendationItem,
            pageName: String,
            pageTitle: String,
            productInfo: DynamicProductInfoP1?,
            userId: String,
            trackingQueue: TrackingQueue?
        ) {
            val itemList = arrayListOf(product.asDataLayer(position, pageTitle))
            val dataLayer = DataLayer.mapOf(
                ProductTrackingConstant.Tracking.KEY_EVENT, ProductTrackingConstant.Action.PROMO_VIEW,
                ProductTrackingConstant.Tracking.KEY_ACTION, ProductTrackingConstant.ViewToView.ACTION_IMPRESSION,
                ProductTrackingConstant.Tracking.KEY_CATEGORY, ProductTrackingConstant.ViewToView.CATEGORY_PDP,
                ProductTrackingConstant.Tracking.KEY_LABEL, pageTitle,
                ProductTrackingConstant.Tracking.KEY_BUSINESS_UNIT, ProductTrackingConstant.ViewToView.BUSINESS_UNIT_HOME,
                ProductTrackingConstant.Tracking.KEY_CURRENT_SITE, ProductTrackingConstant.Tracking.CURRENT_SITE,
                ProductTrackingConstant.Tracking.KEY_TRACKER_ID, ProductTrackingConstant.ViewToView.TRACKER_ID_IMPRESS,
                ProductTrackingConstant.Tracking.KEY_ECOMMERCE,
                DataLayer.mapOf(
                    ProductTrackingConstant.Tracking.CURRENCY_CODE,
                    ProductTrackingConstant.Tracking.CURRENCY_DEFAULT_VALUE,
                    ProductTrackingConstant.Action.PROMO_VIEW,
                    DataLayer.mapOf(
                        ProductTrackingConstant.Action.PROMOTIONS,
                        itemList
                    )
                ),
                ProductTrackingConstant.Tracking.KEY_PRODUCT_ID, productInfo?.basic?.productID ?: "",
                ProductTrackingConstant.Tracking.KEY_USER_ID_VARIANT, userId
            ) as HashMap<String, Any>

            trackingQueue?.putEETracking(dataLayer)
        }

        private fun RecommendationItem.asPromotionBundle(
            position: Int,
            headerName: String
        ) = Bundle().apply {
            val currentPosition = position + 1
            putString(ProductTrackingConstant.ViewToView.KEY_CREATIVE_NAME, ProductTrackingConstant.ViewToView.NULL_LABEL)
            putInt(ProductTrackingConstant.ViewToView.KEY_CREATIVE_SLOT, currentPosition)
            putString(ProductTrackingConstant.ViewToView.KEY_ITEM_ID, asItemId())
            putString(ProductTrackingConstant.ViewToView.KEY_ITEM_NAME, asItemName(currentPosition, headerName))
        }

        private fun RecommendationItem.asDataLayer(
            position: Int,
            headerName: String
        ): Map<String, Any> {
            val currentPosition = position + 1
            return hashMapOf(
                ProductTrackingConstant.ViewToView.KEY_CREATIVE_NAME to ProductTrackingConstant.ViewToView.NULL_LABEL,
                ProductTrackingConstant.ViewToView.KEY_CREATIVE_SLOT to currentPosition,
                ProductTrackingConstant.ViewToView.KEY_ITEM_ID to asItemId(),
                ProductTrackingConstant.ViewToView.KEY_ITEM_NAME to asItemName(currentPosition, headerName)
            )
        }

        private fun RecommendationItem.asItemId(): String {
            val channelId = ProductTrackingConstant.ViewToView.NULL_LABEL
            val bannerId = ProductTrackingConstant.ViewToView.NULL_LABEL
            val targetingType = ProductTrackingConstant.ViewToView.NULL_LABEL
            val targetingValue = ProductTrackingConstant.ViewToView.NULL_LABEL
            return "${channelId}_${bannerId}_${targetingType}_${targetingValue}_${departmentId}_$recommendationType"
        }

        private fun RecommendationItem.asItemName(
            position: Int,
            headerName: String
        ): String {
            return ProductTrackingConstant.ViewToView.ITEM_NAME_FORMAT.format(position, headerName)
        }

        fun eventClickViewToView(
            position: Int,
            product: RecommendationItem,
            pageName: String,
            pageTitle: String,
            productInfo: DynamicProductInfoP1?,
            userId: String
        ) {
            val itemBundle = Bundle().apply {
                putString(ProductTrackingConstant.Tracking.KEY_EVENT, ProductTrackingConstant.Tracking.SELECT_CONTENT)
                putString(ProductTrackingConstant.Tracking.KEY_ACTION, ProductTrackingConstant.ViewToView.ACTION_CLICK)
                putString(ProductTrackingConstant.Tracking.KEY_CATEGORY, ProductTrackingConstant.ViewToView.CATEGORY_PDP)
                putString(ProductTrackingConstant.Tracking.KEY_LABEL, pageTitle)
                putString(ProductTrackingConstant.Tracking.KEY_BUSINESS_UNIT, ProductTrackingConstant.ViewToView.BUSINESS_UNIT_HOME)
                putString(ProductTrackingConstant.Tracking.KEY_CURRENT_SITE, ProductTrackingConstant.Tracking.CURRENT_SITE)
                putString(ProductTrackingConstant.Tracking.KEY_TRACKER_ID, ProductTrackingConstant.ViewToView.TRACKER_ID_CLICK)

                // promotion
                val bundlePromotion = product.asPromotionBundle(position, pageTitle)
                val list = arrayListOf(bundlePromotion)
                putParcelableArrayList(ProductTrackingConstant.Tracking.KEY_PROMOTIONS, list)

                putString(ProductTrackingConstant.Tracking.KEY_PRODUCT_ID, productInfo?.basic?.productID ?: "")
                putString(ProductTrackingConstant.Tracking.KEY_USER_ID_VARIANT, userId)
            }
            TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(ProductTrackingConstant.Tracking.PROMO_CLICK, itemBundle)
        }
    }
}
