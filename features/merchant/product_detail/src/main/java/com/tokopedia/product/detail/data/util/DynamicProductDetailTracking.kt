package com.tokopedia.product.detail.data.util

import android.os.Bundle
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.iris.util.KEY_SESSION_IRIS
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.linker.LinkerConstants
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.common.data.model.pdplayout.DynamicProductInfoP1
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.variant.VariantDataModel
import com.tokopedia.product.detail.data.util.ProductTrackingConstant.Action.CLICK_ANNOTATION_RECOM_CHIP
import com.tokopedia.product.detail.data.util.TrackingUtil.removeCurrencyPrice
import com.tokopedia.product.util.processor.Product
import com.tokopedia.product.util.processor.ProductDetailViewsBundler
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.variant_common.model.ProductVariantCommon
import org.json.JSONArray
import org.json.JSONObject


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

    private fun generateComponentTrackModel(variantData: VariantDataModel?, variantPosition: Int): ComponentTrackDataModel? {
        return ComponentTrackDataModel(variantData?.type ?: "", variantData?.name
                ?: "", variantPosition)
    }

    fun generateVariantString(variant: ProductVariantCommon?, selectedProductId: String): String {
        return variant?.getOptionListString(selectedProductId)?.map {
            it
        }?.joinToString(",") ?: ""
    }

    object Click {

        fun eventClickCustomInfo(title: String, userId: String, productInfo: DynamicProductInfoP1?, componentTrackDataModel: ComponentTrackDataModel?) {
            val componentTitle = "component title: $title"
            val mapEvent = TrackAppUtils.gtmData(
                    ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                    ProductTrackingConstant.Category.PDP,
                    ProductTrackingConstant.Action.CLICK_CUSTOM_INFO,
                    componentTitle)
            mapEvent[ProductTrackingConstant.Tracking.KEY_USER_ID_VARIANT] = userId
            mapEvent[ProductTrackingConstant.Tracking.KEY_ISLOGGIN] = (userId.isNotEmpty()).toString()
            mapEvent[ProductTrackingConstant.Tracking.KEY_SHOP_TYPE] = productInfo?.shopTypeString ?: ""
            mapEvent[ProductTrackingConstant.Tracking.KEY_SHOP_ID_SELLER] = productInfo?.basic?.shopID ?: ""

            TrackingUtil.addComponentTracker(mapEvent, productInfo, componentTrackDataModel, ProductTrackingConstant.Action.CLICK_CUSTOM_INFO)
        }

        fun eventClickTicker(tickerTitle: String, tickerType: Int, productInfo: DynamicProductInfoP1?, componentTrackDataModel: ComponentTrackDataModel?, userId: String) {
            val mapEvent = TrackAppUtils.gtmData(
                    ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                    ProductTrackingConstant.Category.PDP,
                    ProductTrackingConstant.Action.CLICK_TICKER,
                    "${ProductTrackingConstant.Tracking.KEY_TICKER_TYPE} : ${TrackingUtil.getTickerTypeInfoString(tickerType)}; $tickerTitle")
            mapEvent[ProductTrackingConstant.Tracking.KEY_USER_ID_VARIANT] = userId
            mapEvent[ProductTrackingConstant.Tracking.KEY_SHOP_ID_SELLER] = productInfo?.basic?.shopID
                    ?: ""
            mapEvent[ProductTrackingConstant.Tracking.KEY_SHOP_TYPE] = productInfo?.shopTypeString
                    ?: ""
            mapEvent[ProductTrackingConstant.Tracking.KEY_ISLOGGIN] = (userId.isNotEmpty()).toString()

            TrackingUtil.addComponentTracker(mapEvent, productInfo, componentTrackDataModel, ProductTrackingConstant.Action.CLICK_TICKER)
        }

        fun eventClickShopMiniShopInfo(productInfo: DynamicProductInfoP1?, componentTrackDataModel: ComponentTrackDataModel, userId: String) {
            val mapEvent = TrackAppUtils.gtmData(
                    ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                    ProductTrackingConstant.Category.PDP,
                    ProductTrackingConstant.Action.CLICK_SHOP_INFO_MINI, "${ProductTrackingConstant.Tracking.KEY_SHOP_ID_SELLER} : ${productInfo?.basic?.shopID}")
            mapEvent[ProductTrackingConstant.Tracking.KEY_USER_ID_VARIANT] = userId
            mapEvent[ProductTrackingConstant.Tracking.KEY_SHOP_ID_SELLER] = productInfo?.basic?.shopID ?: ""
            mapEvent[ProductTrackingConstant.Tracking.KEY_SHOP_TYPE] = productInfo?.shopTypeString ?: ""
            mapEvent[ProductTrackingConstant.Tracking.KEY_ISLOGGIN] = (userId.isNotEmpty()).toString()

            TrackingUtil.addComponentTracker(mapEvent, productInfo, componentTrackDataModel, ProductTrackingConstant.Action.CLICK_SHOP_INFO_MINI)
        }

        fun eventClickReviewImageMedia(productInfo: DynamicProductInfoP1?, componentTrackDataModel: ComponentTrackDataModel, userId: String) {
            val mapEvent = TrackAppUtils.gtmData(
                    ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                    ProductTrackingConstant.Category.PDP,
                    ProductTrackingConstant.Action.CLICK_REVIEW_IMAGE_MEDIA, "")
            mapEvent[ProductTrackingConstant.Tracking.KEY_USER_ID_VARIANT] = userId
            mapEvent[ProductTrackingConstant.Tracking.KEY_SHOP_ID_SELLER] = productInfo?.basic?.shopID ?: ""
            mapEvent[ProductTrackingConstant.Tracking.KEY_SHOP_TYPE] = productInfo?.shopTypeString ?: ""
            mapEvent[ProductTrackingConstant.Tracking.KEY_ISLOGGIN] = (userId.isNotEmpty()).toString()

            TrackingUtil.addComponentTracker(mapEvent, productInfo, componentTrackDataModel, ProductTrackingConstant.Action.CLICK_REVIEW_IMAGE_MEDIA)
        }

        fun eventClickWholesale(productInfo: DynamicProductInfoP1?, componentTrackDataModel: ComponentTrackDataModel?) {
            val mapEvent = TrackAppUtils.gtmData(
                    ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                    ProductTrackingConstant.Category.PDP,
                    ProductTrackingConstant.Action.CLICK_WHOLESALE, "")

            TrackingUtil.addComponentTracker(mapEvent, productInfo, componentTrackDataModel, ProductTrackingConstant.Action.CLICK_WHOLESALE)
        }

        fun eventClickBuyAskForImei(title: String, userId: String, productInfo: DynamicProductInfoP1?) {
            val actionTitle = "${ProductTrackingConstant.Action.CLICK_REQUEST_PERMISSION_IMEI} $title"
            val mapEvent = TrackAppUtils.gtmData(
                    ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                    ProductTrackingConstant.Category.PDP,
                    actionTitle, "")
            mapEvent[ProductTrackingConstant.Tracking.KEY_USER_ID_VARIANT] = userId
            mapEvent[ProductTrackingConstant.Tracking.KEY_ISLOGGIN] = (userId.isNotEmpty()).toString()

            TrackingUtil.addComponentTracker(mapEvent, productInfo, null, actionTitle)
        }

        fun eventClickGiveAccessPhoneStatePermission(title: String, userId: String, productInfo: DynamicProductInfoP1?) {
            val actionTitle = "${ProductTrackingConstant.Action.CLICK_ACCEPT_PERMISSION} $title"
            val mapEvent = TrackAppUtils.gtmData(
                    ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                    ProductTrackingConstant.Category.PDP,
                    actionTitle,
                    "")
            mapEvent[ProductTrackingConstant.Tracking.KEY_USER_ID_VARIANT] = userId
            mapEvent[ProductTrackingConstant.Tracking.KEY_ISLOGGIN] = (userId.isNotEmpty()).toString()

            TrackingUtil.addComponentTracker(mapEvent, productInfo, null, actionTitle)
        }

        fun eventClickGoToSetting(title: String, userId: String, productInfo: DynamicProductInfoP1?) {
            val actionTitle = "${ProductTrackingConstant.Action.CLICK_GO_TO_SETTING} $title"
            val mapEvent = TrackAppUtils.gtmData(
                    ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                    ProductTrackingConstant.Category.PDP,
                    actionTitle,
                    "")

            mapEvent[ProductTrackingConstant.Tracking.KEY_USER_ID_VARIANT] = userId
            mapEvent[ProductTrackingConstant.Tracking.KEY_ISLOGGIN] = (userId.isNotEmpty()).toString()
            TrackingUtil.addComponentTracker(mapEvent, productInfo, null, actionTitle)
        }

        fun eventClickLater(title: String, userId: String, productInfo: DynamicProductInfoP1?) {
            val actionTitle = "${ProductTrackingConstant.Action.CLICK_PERMISSION_LATER} $title"
            val mapEvent = TrackAppUtils.gtmData(
                    ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                    ProductTrackingConstant.Category.PDP,
                    actionTitle, "")

            mapEvent[ProductTrackingConstant.Tracking.KEY_USER_ID_VARIANT] = userId
            mapEvent[ProductTrackingConstant.Tracking.KEY_ISLOGGIN] = (userId.isNotEmpty()).toString()

            TrackingUtil.addComponentTracker(mapEvent, productInfo, null, actionTitle)
        }

        fun eventClickButtonNonLogin(actionButton: Int, productInfo: DynamicProductInfoP1?, userId: String, shopType: String, buttonActionText: String) {
            if (actionButton == ProductDetailConstant.ATC_BUTTON) {
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
                    "")
            mapEvent[ProductTrackingConstant.Tracking.KEY_USER_ID] = userId
            mapEvent[ProductTrackingConstant.Tracking.KEY_SHOP_ID_SELLER] = productInfo?.basic?.shopID
                    ?: ""
            mapEvent[ProductTrackingConstant.Tracking.KEY_SHOP_TYPE] = shopType
            mapEvent[ProductTrackingConstant.Tracking.KEY_ISLOGGIN] = (userId.isNotEmpty()).toString()

            TrackingUtil.addComponentTracker(mapEvent, productInfo, null, ProductTrackingConstant.Action.IMPRESSION_CHOOSE_VARIANT_NOTIFICATION)
        }

        private fun eventBuyButtonNonLogin(productInfo: DynamicProductInfoP1?, userId: String, shopType: String, buttonActionText: String) {
            val mapEvent = TrackAppUtils.gtmData(
                    ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                    ProductTrackingConstant.Category.PDP,
                    "click $buttonActionText on pdp - non login",
                    "")
            mapEvent[ProductTrackingConstant.Tracking.KEY_USER_ID] = userId
            mapEvent[ProductTrackingConstant.Tracking.KEY_SHOP_ID_SELLER] = productInfo?.basic?.shopID
                    ?: ""
            mapEvent[ProductTrackingConstant.Tracking.KEY_SHOP_TYPE] = shopType
            mapEvent[ProductTrackingConstant.Tracking.KEY_ISLOGGIN] = (userId.isNotEmpty()).toString()


            TrackingUtil.addComponentTracker(mapEvent, productInfo, null, ProductTrackingConstant.Action.IMPRESSION_CHOOSE_VARIANT_NOTIFICATION)
        }

        fun eventEcommerceBuy(actionButton: Int, buttonText: String, userId: String,
                             cartId: String, trackerAttribution: String, multiOrigin: Boolean, variantString: String,
                              productInfo: DynamicProductInfoP1?) {
            val productId = productInfo?.basic?.productID ?: ""
            val shopId = productInfo?.basic?.shopID ?: ""
            val productName = productInfo?.data?.name ?: ""
            val productPrice = productInfo?.finalPrice.toString()
            val shopType = productInfo?.shopTypeString ?: ""
            val shopName = productInfo?.basic?.shopName ?: ""

            val quantity = productInfo?.basic?.minOrder ?: 0
            val isFreeOngkir = productInfo?.data?.isFreeOngkir?.isActive ?: false
            val generateButtonActionString = when (actionButton) {
                ProductDetailConstant.OCS_BUTTON -> "$buttonText ocs"
                ProductDetailConstant.OCC_BUTTON -> "$buttonText occ"
                else -> "$buttonText normal"
            }

            val categoryName = productInfo?.basic?.category?.detail?.map {
                it.name
            }?.joinToString("/", postfix = "/ ${productInfo.basic.category.id}") ?: ""

            val categoryId = productInfo?.basic?.category?.detail?.map {
                it.id
            }?.joinToString("/") ?: ""

            TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(DataLayer.mapOf(
                    ProductTrackingConstant.Tracking.KEY_EVENT, "addToCart",
                    ProductTrackingConstant.Tracking.KEY_CATEGORY, ProductTrackingConstant.Category.PDP,
                    ProductTrackingConstant.Tracking.KEY_ACTION, if (actionButton == ProductDetailConstant.ATC_BUTTON) "click - tambah ke keranjang on pdp" else "click - $buttonText on pdp",
                    ProductTrackingConstant.Tracking.KEY_LABEL, if (actionButton == ProductDetailConstant.ATC_BUTTON) "" else "fitur : $generateButtonActionString",
                    ProductTrackingConstant.Tracking.KEY_PRODUCT_ID, productId,
                    ProductTrackingConstant.Tracking.KEY_LAYOUT, "layout:${productInfo?.layoutName};catName:${productInfo?.basic?.category?.name};catId:${productInfo?.basic?.category?.id};",
                    ProductTrackingConstant.Tracking.KEY_USER_ID, userId,
                    ProductTrackingConstant.Tracking.KEY_SHOP_ID_SELLER, shopId,
                    ProductTrackingConstant.Tracking.KEY_SHOP_TYPE, shopType,
                    ProductTrackingConstant.Tracking.KEY_ISLOGGIN, (userId.isNotEmpty()).toString(),

                    ProductTrackingConstant.Tracking.KEY_ECOMMERCE, DataLayer.mapOf(
                    ProductTrackingConstant.Tracking.CURRENCY_CODE, ProductTrackingConstant.Tracking.CURRENCY_DEFAULT_VALUE,
                    ProductTrackingConstant.Tracking.KEY_ADD, DataLayer.mapOf(
                    ProductTrackingConstant.Tracking.PRODUCTS, DataLayer.listOf(
                    DataLayer.mapOf(
                            ProductTrackingConstant.Tracking.NAME, productName,
                            ProductTrackingConstant.Tracking.ID, productId,
                            ProductTrackingConstant.Tracking.PRICE, productPrice,
                            ProductTrackingConstant.Tracking.BRAND, ProductTrackingConstant.Tracking.VALUE_NONE_OTHER,
                            ProductTrackingConstant.Tracking.CATEGORY, categoryName,
                            ProductTrackingConstant.Tracking.VARIANT, variantString,
                            ProductTrackingConstant.Tracking.QUANTITY, quantity,
                            ProductTrackingConstant.Tracking.KEY_DIMENSION_79, shopId,
                            ProductTrackingConstant.Tracking.KEY_DIMENSION_80, shopName,
                            ProductTrackingConstant.Tracking.KEY_DIMENSION_81, shopType,
                            ProductTrackingConstant.Tracking.KEY_DIMENSION_45, cartId,
                            ProductTrackingConstant.Tracking.KEY_DIMENSION_82, categoryId,
                            ProductTrackingConstant.Tracking.KEY_DIMENSION_40, "null",
                            ProductTrackingConstant.Tracking.KEY_DIMENSION_54, TrackingUtil.getMultiOriginAttribution(multiOrigin),
                            ProductTrackingConstant.Tracking.KEY_DIMENSION_83, if (isFreeOngkir) ProductTrackingConstant.Tracking.VALUE_BEBAS_ONGKIR else ProductTrackingConstant.Tracking.VALUE_NONE_OTHER,
                            ProductTrackingConstant.Tracking.KEY_DIMENSION_38, trackerAttribution
                    ))))))
        }

        fun onEditProductClicked(productInfo: DynamicProductInfoP1?, componentTrackDataModel: ComponentTrackDataModel?) {
            val mapEvent = TrackAppUtils.gtmData(
                    ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                    ProductTrackingConstant.Category.PDP_SELLER,
                    ProductTrackingConstant.Action.CLICK_EDIT_PRODUCT,
                    "")
            TrackingUtil.addComponentTracker(mapEvent, productInfo, componentTrackDataModel, ProductTrackingConstant.Action.CLICK_EDIT_PRODUCT)
        }

        fun onVariantErrorPartialySelected(productInfo: DynamicProductInfoP1?, actionButton: Int) {
            val mapEvent = TrackAppUtils.gtmData(
                    ProductTrackingConstant.PDP.EVENT_VIEW_PDP_IRIS,
                    ProductTrackingConstant.Category.PDP,
                    ProductTrackingConstant.Action.IMPRESSION_CHOOSE_VARIANT_NOTIFICATION,
                    if (actionButton == ProductDetailConstant.BUY_BUTTON) "beli button name" else "tambah ke keranjang")

            TrackingUtil.addComponentTracker(mapEvent, productInfo, null, ProductTrackingConstant.Action.IMPRESSION_CHOOSE_VARIANT_NOTIFICATION)
        }

        fun onVariantGuideLineClicked(productInfo: DynamicProductInfoP1?, variantData: VariantDataModel?, variantPosition: Int) {
            val mapEvent = TrackAppUtils.gtmData(
                    ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                    ProductTrackingConstant.Category.PDP,
                    ProductTrackingConstant.Action.CLICK_VARIANT_GUIDELINE,
                    "")

            TrackingUtil.addComponentTracker(mapEvent, productInfo, generateComponentTrackModel(variantData, variantPosition), ProductTrackingConstant.Action.CLICK_CHOOSE_PRODUCT_VARIANT)
        }

        fun onVariantLevel1Clicked(productInfo: DynamicProductInfoP1?, variantData: VariantDataModel?, variantCommonData: ProductVariantCommon?, variantPosition: Int) {

            val variantLevel = variantData?.listOfVariantCategory?.size.toString().toList().joinToString(prefix = "level : ", postfix = ";")
            val variantTitle = variantData?.listOfVariantCategory?.map {
                it.identifier
            }?.joinToString(",", prefix = "variant_title : ", postfix = ";") ?: ""
            val variantValue = "variant_value: " + generateVariantString(variantCommonData, productInfo?.basic?.productID
                    ?: "") + ";"
            val variantId = "variant : " + productInfo?.basic?.productID + ";"

            val mapEvent = TrackAppUtils.gtmData(
                    ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                    ProductTrackingConstant.Category.PDP,
                    ProductTrackingConstant.Action.CLICK_CHOOSE_PRODUCT_VARIANT,
                    variantLevel + variantTitle + variantValue + variantId)

            TrackingUtil.addComponentTracker(mapEvent, productInfo, generateComponentTrackModel(variantData, variantPosition), ProductTrackingConstant.Action.CLICK_CHOOSE_PRODUCT_VARIANT)
        }

        fun eventFollowShop(productInfo: DynamicProductInfoP1?, componentTrackDataModel: ComponentTrackDataModel?,
                            shopName: String) {
            val mapEvent = TrackAppUtils.gtmData(
                    ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                    ProductTrackingConstant.Category.PDP,
                    ProductTrackingConstant.Action.CLICK_FOLLOW,
                    shopName)

            TrackingUtil.addComponentTracker(mapEvent, productInfo, componentTrackDataModel, ProductTrackingConstant.Action.CLICK_FOLLOW)
        }

        fun eventUnfollowShop(productInfo: DynamicProductInfoP1?, componentTrackDataModel: ComponentTrackDataModel?,
                              shopName: String) {
            val mapEvent = TrackAppUtils.gtmData(
                    ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                    ProductTrackingConstant.Category.PDP,
                    ProductTrackingConstant.Action.CLICK_UNFOLLOW,
                    shopName)

            TrackingUtil.addComponentTracker(mapEvent, productInfo, componentTrackDataModel, ProductTrackingConstant.Action.CLICK_UNFOLLOW)
        }

        fun trackTradein(usedPrice: Int, productInfo: DynamicProductInfoP1?, componentTrackDataModel: ComponentTrackDataModel) {
            if (usedPrice > 0)
                trackTradeinAfterDiagnotics(productInfo, componentTrackDataModel)
            else
                trackTradeinBeforeDiagnotics(productInfo, componentTrackDataModel)
        }

        fun trackTradeinBeforeDiagnotics(productInfo: DynamicProductInfoP1?, componentTrackDataModel: ComponentTrackDataModel) {
            val mapEvent = TrackAppUtils.gtmData(
                    ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                    ProductTrackingConstant.Category.PDP,
                    ProductTrackingConstant.Action.CLICK_TRADEIN,
                    "before diagnostic")

            TrackingUtil.addComponentTracker(mapEvent, productInfo, componentTrackDataModel, ProductTrackingConstant.Action.CLICK_TRADEIN)
        }

        fun trackTradeinAfterDiagnotics(productInfo: DynamicProductInfoP1?, componentTrackDataModel: ComponentTrackDataModel) {
            val mapEvent = TrackAppUtils.gtmData(
                    ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                    ProductTrackingConstant.Category.PDP,
                    ProductTrackingConstant.Category.PDP,
                    "after diagnostic")
            TrackingUtil.addComponentTracker(mapEvent, productInfo, componentTrackDataModel, ProductTrackingConstant.Category.PDP)
        }

        fun eventClickSeeMoreRecomWidget(widgetName: String, productInfo: DynamicProductInfoP1?, componentTrackDataModel: ComponentTrackDataModel) {
            val recomSeeAllAction = String.format(ProductTrackingConstant.Action.CLICK_SEE_MORE_WIDGET, widgetName)
            val mapEvent = TrackAppUtils.gtmData(
                    ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                    ProductTrackingConstant.Category.PDP,
                    recomSeeAllAction,
                    ""
            )
            TrackingUtil.addComponentTracker(mapEvent, productInfo, componentTrackDataModel, recomSeeAllAction)
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
                    "$categoryId - $categoryName")

            TrackingUtil.addComponentTracker(mapEvent, productInfo, componentTrackDataModel, ProductTrackingConstant.Action.CLICK_CATEGORY)
        }


        fun eventEtalaseClicked(etalaseId: String, etalaseName: String, productInfo: DynamicProductInfoP1?, componentTrackDataModel: ComponentTrackDataModel) {
            val mapEvent = TrackAppUtils.gtmData(
                    ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                    ProductTrackingConstant.Category.PDP,
                    ProductTrackingConstant.Action.CLICK_ETALASE,
                    "$etalaseId - $etalaseName")

            TrackingUtil.addComponentTracker(mapEvent, productInfo, componentTrackDataModel, ProductTrackingConstant.Action.CLICK_ETALASE)
        }

        fun eventClickMerchantVoucherUse(merchantVoucherViewModel: MerchantVoucherViewModel, shopId: String, position: Int,
                                         productInfo: DynamicProductInfoP1?, componentTrackDataModel: ComponentTrackDataModel) {
            val mapEvent = TrackAppUtils.gtmData(
                    ProductTrackingConstant.MerchantVoucher.PROMO_CLICK,
                    ProductTrackingConstant.Category.PDP,
                    ProductTrackingConstant.Action.CLICK_USE_MERCHANT_VOUCHER,
                    ""
            )

            mapEvent[ProductTrackingConstant.Tracking.KEY_PROMO_ID] = mapOf(ProductTrackingConstant.Tracking.KEY_PROMO_ID to merchantVoucherViewModel.voucherId.toString())
            mapEvent[ProductTrackingConstant.Tracking.KEY_ECOMMERCE] = DataLayer.mapOf(
                    ProductTrackingConstant.MerchantVoucher.PROMO_CLICK, DataLayer.mapOf(
                    ProductTrackingConstant.Tracking.KEY_PROMOTIONS, TrackingUtil.createMVCMap(listOf(merchantVoucherViewModel), shopId, position)))

            TrackingUtil.addComponentTracker(mapEvent, productInfo, componentTrackDataModel, ProductTrackingConstant.Action.CLICK_USE_MERCHANT_VOUCHER)
        }

        fun eventClickMerchantVoucherSeeDetail(voucherId: Int, productInfo: DynamicProductInfoP1?, componentTrackDataModel: ComponentTrackDataModel) {
            val mapEvent = TrackAppUtils.gtmData(
                    ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                    ProductTrackingConstant.Category.PDP,
                    ProductTrackingConstant.Action.CLICK_DETAIL_MERCHANT_VOUCHER,
                    productInfo?.basic?.productID ?: ""
            )

            mapEvent[ProductTrackingConstant.Tracking.KEY_PROMO_ID] = voucherId.toString()
            TrackingUtil.addComponentTracker(mapEvent, productInfo, componentTrackDataModel, ProductTrackingConstant.Action.CLICK_DETAIL_MERCHANT_VOUCHER)
        }


        fun eventClickMerchantVoucherSeeAll(productInfo: DynamicProductInfoP1?, componentTrackDataModel: ComponentTrackDataModel) {
            val mapEvent = TrackAppUtils.gtmData(
                    ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                    ProductTrackingConstant.Category.PDP,
                    ProductTrackingConstant.Action.CLICK_SEE_ALL_MERCHANT_VOUCHER,
                    productInfo?.basic?.productID ?: ""
            )

            TrackingUtil.addComponentTracker(mapEvent, productInfo, componentTrackDataModel, ProductTrackingConstant.Action.CLICK_SEE_ALL_MERCHANT_VOUCHER)
        }

        fun eventProductImageOnSwipe(productInfo: DynamicProductInfoP1?, swipeDirection: String, imagePosition: Int, componentTrackDataModel: ComponentTrackDataModel) {
            val mapEvent = TrackAppUtils.gtmData(
                    ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                    ProductTrackingConstant.Category.PDP,
                    ProductTrackingConstant.Action.SWIPE_PRODUCT_PICTURE,
                    "$swipeDirection - " + ProductTrackingConstant.Label.PDP + " - $imagePosition"
            )
            TrackingUtil.addComponentTracker(mapEvent, productInfo, componentTrackDataModel, ProductTrackingConstant.Action.SWIPE_PRODUCT_PICTURE)

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

        fun eventClickPdpShare(productInfo: DynamicProductInfoP1?) {

            val mapEvent = TrackAppUtils.gtmData(
                    ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                    ProductTrackingConstant.Category.TOP_NAV_SHARE_PDP,
                    ProductTrackingConstant.Action.CLICK_SHARE_PDP,
                    ""
            )
            TrackingUtil.addComponentTracker(mapEvent, productInfo, null, ProductTrackingConstant.Action.CLICK_SHARE_PDP)

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

        fun eventSearchToolbarClicked(productInfo: DynamicProductInfoP1?) {
            val mapEvent = TrackAppUtils.gtmData(
                    ProductTrackingConstant.PDP.EVENT_CLICK_TOP_NAV,
                    ProductTrackingConstant.Category.TOP_NAV_SEARCH_PDP,
                    ProductTrackingConstant.Action.CLICK_SEARCH_BOX,
                    ""
            )
            TrackingUtil.addComponentTracker(mapEvent, productInfo, null, ProductTrackingConstant.Action.CLICK_APPLY_LEASING)
        }

        fun eventClickApplyLeasing(productInfo: DynamicProductInfoP1?, variant: String) {
            val mapEvent = TrackAppUtils.gtmData(
                    ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                    ProductTrackingConstant.Category.PDP,
                    ProductTrackingConstant.Action.CLICK_APPLY_LEASING,
                    variant
            )
            TrackingUtil.addComponentTracker(mapEvent, productInfo, null, ProductTrackingConstant.Action.CLICK_APPLY_LEASING)
        }

        fun eventSocialProfShippingClicked(productInfo: DynamicProductInfoP1?, componentTrackDataModel: ComponentTrackDataModel) {
            val mapEvent = TrackAppUtils.gtmData(
                    ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                    ProductTrackingConstant.Category.PDP,
                    ProductTrackingConstant.Action.CLICK_SHIPPING,
                    ""
            )
            TrackingUtil.addComponentTracker(mapEvent, productInfo, componentTrackDataModel, ProductTrackingConstant.Action.CLICK_SHIPPING)
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


        fun eventClickReviewOnBuyersImage(productInfo: DynamicProductInfoP1?, componentTrackDataModel: ComponentTrackDataModel,
                                          reviewId: String?) {
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
            mapEvent[ProductTrackingConstant.Tracking.KEY_SHOP_TYPE] = productInfo?.shopTypeString ?: ""

            TrackingUtil.addComponentTracker(mapEvent, productInfo, componentTrackDataModel, ProductTrackingConstant.Action.CLICK_SEE_ALL_ULASAN)
        }

        fun eventClickReviewOnMostHelpfulReview(productInfo: DynamicProductInfoP1?,
                                                componentTrackDataModel: ComponentTrackDataModel,
                                                reviewId: String?) {
            val productId = productInfo?.basic?.productID ?: ""

            val mapEvent = TrackAppUtils.gtmData(
                    ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                    ProductTrackingConstant.Category.PDP,
                    ProductTrackingConstant.Action.CLICK_IMAGE_MOST_HELPFULL_REVIEW,
                    "product_id: $productId - review_id : $reviewId"
            )
            TrackingUtil.addComponentTracker(mapEvent, productInfo, componentTrackDataModel, ProductTrackingConstant.Action.CLICK_IMAGE_MOST_HELPFULL_REVIEW)
        }

        fun eventRecommendationClick(product: RecommendationItem, position: Int, isSessionActive: Boolean, pageName: String, pageTitle: String,
                                     productInfo: DynamicProductInfoP1?, componentTrackDataModel: ComponentTrackDataModel) {
            val listValue = ProductTrackingConstant.Tracking.LIST_DEFAULT + pageName +
                    (if (!isSessionActive) " - ${ProductTrackingConstant.Tracking.USER_NON_LOGIN}" else "") +
                    ProductTrackingConstant.Tracking.LIST_RECOMMENDATION + product.recommendationType + (if (product.isTopAds) " - product topads" else "")
            val topAdsAction = ProductTrackingConstant.Action.TOPADS_CLICK + (if (!isSessionActive) " - ${ProductTrackingConstant.Tracking.USER_NON_LOGIN}" else "")
            TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                    DataLayer.mapOf(
                            ProductTrackingConstant.Tracking.KEY_EVENT, ProductTrackingConstant.Action.PRODUCT_CLICK,
                            ProductTrackingConstant.Tracking.KEY_CATEGORY, ProductTrackingConstant.Category.PDP,
                            ProductTrackingConstant.Tracking.KEY_ACTION, topAdsAction,
                            ProductTrackingConstant.Tracking.KEY_LABEL, pageTitle,
                            ProductTrackingConstant.Tracking.KEY_PRODUCT_ID, product.productId.toString(),
                            ProductTrackingConstant.Tracking.KEY_LAYOUT, "layout:${productInfo?.layoutName};catName:${productInfo?.basic?.category?.name};catId:${productInfo?.basic?.category?.id};",
                            ProductTrackingConstant.Tracking.KEY_COMPONENT, "comp:${componentTrackDataModel.componentType};temp:${componentTrackDataModel.componentName};elem:${topAdsAction};cpos:${componentTrackDataModel.adapterPosition};",
                            ProductTrackingConstant.Tracking.KEY_ECOMMERCE, DataLayer.mapOf(
                            ProductTrackingConstant.Tracking.CURRENCY_CODE, ProductTrackingConstant.Tracking.CURRENCY_DEFAULT_VALUE,
                            ProductTrackingConstant.Action.CLICK, DataLayer.mapOf(
                            ProductTrackingConstant.Tracking.ACTION_FIELD, DataLayer.mapOf(ProductTrackingConstant.Tracking.LIST, listValue),
                            ProductTrackingConstant.Tracking.PRODUCTS, DataLayer.listOf(
                            DataLayer.mapOf(ProductTrackingConstant.Tracking.PROMO_NAME, product.name,
                                    ProductTrackingConstant.Tracking.ID, product.productId.toString(), ProductTrackingConstant.Tracking.PRICE, TrackingUtil.removeCurrencyPrice(product.price),
                                    ProductTrackingConstant.Tracking.BRAND, ProductTrackingConstant.Tracking.DEFAULT_VALUE,
                                    ProductTrackingConstant.Tracking.VARIANT, ProductTrackingConstant.Tracking.DEFAULT_VALUE,
                                    ProductTrackingConstant.Tracking.CATEGORY, product.categoryBreadcrumbs.toLowerCase(),
                                    ProductTrackingConstant.Tracking.PROMO_POSITION, position + 1,
                                    ProductTrackingConstant.Tracking.KEY_DIMENSION_83, if (product.isFreeOngkirActive) ProductTrackingConstant.Tracking.VALUE_BEBAS_ONGKIR else ProductTrackingConstant.Tracking.VALUE_NONE_OTHER,
                                    ProductTrackingConstant.Tracking.KEY_PRODUCT_ID, product.productId.toString()
                            )
                    ))
                    ))
            )
        }

        fun eventEditProductClick(isSessionActive: Boolean, productInfo: DynamicProductInfoP1?,
                                  componentTrackDataModel: ComponentTrackDataModel) {
            val topAdsAction = ProductTrackingConstant.Action.TOPADS_CLICK + (if (!isSessionActive) " - ${ProductTrackingConstant.Tracking.USER_NON_LOGIN}" else "")

            val editProductData = DataLayer.mapOf(
                    ProductTrackingConstant.Tracking.KEY_EVENT, ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                    ProductTrackingConstant.Tracking.KEY_CATEGORY, ProductTrackingConstant.Category.PRODUCT_DETAIL_PAGE_SELLER,
                    ProductTrackingConstant.Tracking.KEY_ACTION, ProductTrackingConstant.Action.CLICK_EDIT_PRODUCT,
                    ProductTrackingConstant.Tracking.KEY_LABEL, "",
                    ProductTrackingConstant.Tracking.KEY_PRODUCT_ID, productInfo?.parentProductId,
                    ProductTrackingConstant.Tracking.KEY_LAYOUT, "layout:${productInfo?.layoutName};catName:${productInfo?.basic?.category?.name};catId:${productInfo?.basic?.category?.id};",
                    ProductTrackingConstant.Tracking.KEY_COMPONENT, "comp:${componentTrackDataModel.componentType};temp:${componentTrackDataModel.componentName};elem:${topAdsAction};cpos:${componentTrackDataModel.adapterPosition};"
            )
            TrackApp.getInstance().gtm.sendGeneralEvent(editProductData)
        }

        fun eventClickAffiliate(userId: String, shopID: Int, isRegularPdp: Boolean = false, productInfo: DynamicProductInfoP1?) {
            val productId = productInfo?.basic?.productID ?: ""
            val mapEvent: MutableMap<String, Any> = if (isRegularPdp) {
                mutableMapOf(ProductTrackingConstant.Tracking.KEY_EVENT to ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                        ProductTrackingConstant.Tracking.KEY_CATEGORY to ProductTrackingConstant.Category.PDP,
                        ProductTrackingConstant.Tracking.KEY_ACTION to ProductTrackingConstant.Action.CLICK_BY_ME,
                        ProductTrackingConstant.Tracking.KEY_LABEL to "$shopID - $productId")
            } else {
                mutableMapOf(ProductTrackingConstant.Tracking.KEY_EVENT to ProductTrackingConstant.Affiliate.CLICK_AFFILIATE,
                        ProductTrackingConstant.Tracking.KEY_CATEGORY to ProductTrackingConstant.Affiliate.CATEGORY,
                        ProductTrackingConstant.Tracking.KEY_ACTION to ProductTrackingConstant.Affiliate.ACTION,
                        ProductTrackingConstant.Tracking.KEY_LABEL to productId)
            }
            mapEvent[ProductTrackingConstant.Tracking.KEY_USER_ID] = userId

            TrackingUtil.addComponentTracker(mapEvent, productInfo, null, ProductTrackingConstant.Action.CLICK_BY_ME)
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

        fun eventButtonChatShopClicked(productInfo: DynamicProductInfoP1?, componentTrackDataModel: ComponentTrackDataModel?) {
            val mapEvent = TrackAppUtils.gtmData(
                    ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                    ProductTrackingConstant.Category.PDP,
                    ProductTrackingConstant.Action.CLICK_PAGE_CHAT,
                    productInfo?.basic?.productID ?: "")

            TrackingUtil.addComponentTracker(mapEvent, productInfo, componentTrackDataModel, ProductTrackingConstant.Action.CLICK_BUTTON_CHAT)
        }

        fun eventButtonChatClicked(productInfo: DynamicProductInfoP1?) {
            if (productInfo?.basic?.productID?.isEmpty() == true) return
            val mapEvent = TrackAppUtils.gtmData(
                    ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                    ProductTrackingConstant.Category.PDP,
                    ProductTrackingConstant.Action.CLICK_BUTTON_CHAT,
                    productInfo?.basic?.productID ?: "")

            TrackingUtil.addComponentTracker(mapEvent, productInfo, null, ProductTrackingConstant.Action.CLICK_BUTTON_CHAT)
        }

        fun eventShippingRateEstimationClicked(postalCode: String, districtName: String, productInfo: DynamicProductInfoP1?,
                                               componentTrackDataModel: ComponentTrackDataModel) {
            val mapEvent = TrackAppUtils.gtmData(
                    ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                    ProductTrackingConstant.Category.PDP,
                    ProductTrackingConstant.Action.CLICK_RATE_ESTIMATE,
                    "$districtName - $postalCode")

            TrackingUtil.addComponentTracker(mapEvent, productInfo, componentTrackDataModel, ProductTrackingConstant.Action.CLICK_RATE_ESTIMATE)
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
            val action = if (notifyMe) ProductDetailCommonConstant.VALUE_TEASER_TRACKING_UNREGISTER else
                ProductDetailCommonConstant.VALUE_TEASER_TRACKING_REGISTER
            val mapEvent = TrackAppUtils.gtmData(
                    ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                    ProductTrackingConstant.Category.PDP,
                    "${ProductTrackingConstant.Action.CLICK_NOTIFY_ME} - $action",
                    ProductTrackingConstant.Label.EMPTY_LABEL)

            mapEvent[ProductTrackingConstant.Tracking.KEY_USER_ID_VARIANT] = userId
            mapEvent[ProductTrackingConstant.Tracking.KEY_ISLOGGIN] = (userId.isNotEmpty()).toString()

            TrackingUtil.addComponentTracker(mapEvent, productInfo, componentTrackDataModel, ProductTrackingConstant.Action.CLICK_NOTIFY_ME)
        }

        fun eventActivationOvo(productId: String, userId: String) {
            val mapEvent = TrackAppUtils.gtmData(
                    ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                    ProductTrackingConstant.Category.PDP,
                    ProductTrackingConstant.Action.CLICK_BUY_ACTIVATION_OVO,
                    ProductTrackingConstant.Label.EMPTY_LABEL)
            TrackingUtil.addComponentOvoTracker(mapEvent, productId, userId)
        }

        fun eventSeeBottomSheetOvo(title: String, productId: String, userId: String) {
            val mapEvent = TrackAppUtils.gtmData(
                    ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                    ProductTrackingConstant.Category.PDP,
                    "${ProductTrackingConstant.Action.CLICK_SEE_BOTTOMSHEET_OVO} $title",
                    ProductTrackingConstant.Label.EMPTY_LABEL)
            TrackingUtil.addComponentOvoTracker(mapEvent, productId, userId)
        }

        fun eventTopupBottomSheetOvo(title: String, buttonTitle: String, productId: String, userId: String) {
            val mapEvent = TrackAppUtils.gtmData(
                    ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                    ProductTrackingConstant.Category.PDP,
                    "${ProductTrackingConstant.Action.CLICK} - $buttonTitle ${ProductTrackingConstant.Action.CLICK_TOPUP_BOTTOMSHEET_OVO} $title",
                    ProductTrackingConstant.Label.EMPTY_LABEL)
            TrackingUtil.addComponentOvoTracker(mapEvent, productId, userId)
        }

        fun eventAddToCartRecommendationClick(product: RecommendationItem, position: Int, isSessionActive: Boolean, pageName: String, pageTitle: String) {
            val valueLoginOrNotLogin = if (!isSessionActive)
                " ${ProductTrackingConstant.Tracking.USER_NON_LOGIN} - "
            else ""
            val listValue = ProductTrackingConstant.Tracking.LIST_PRODUCT_AFTER_ATC + pageName + ProductTrackingConstant.Tracking.LIST_RECOMMENDATION + valueLoginOrNotLogin +
                    product.recommendationType + (if (product.isTopAds) " - product topads" else "")
            val actionValuePostfix = if (!isSessionActive)
                " - ${ProductTrackingConstant.Tracking.USER_NON_LOGIN}"
            else
                ""
            val mapEvent = TrackAppUtils.gtmData(
                    ProductTrackingConstant.Action.PRODUCT_CLICK,
                    ProductTrackingConstant.Category.PDP_AFTER_ATC,
                    ProductTrackingConstant.Action.TOPADS_ATC_CLICK + actionValuePostfix,
                    pageTitle
            )

            mapEvent[ProductTrackingConstant.Tracking.KEY_ECOMMERCE] = DataLayer.mapOf(
                    ProductTrackingConstant.Action.CLICK, DataLayer.mapOf(
                    ProductTrackingConstant.Tracking.ACTION_FIELD, DataLayer.mapOf(ProductTrackingConstant.Tracking.LIST, listValue),
                    ProductTrackingConstant.Tracking.PRODUCTS, DataLayer.listOf(
                    DataLayer.mapOf(
                            ProductTrackingConstant.Tracking.PROMO_NAME, product.name,
                            ProductTrackingConstant.Tracking.ID, product.productId.toString(),
                            ProductTrackingConstant.Tracking.PRICE, removeCurrencyPrice(product.price),
                            ProductTrackingConstant.Tracking.BRAND, ProductTrackingConstant.Tracking.DEFAULT_VALUE,
                            ProductTrackingConstant.Tracking.CATEGORY, product.categoryBreadcrumbs.toLowerCase(),
                            ProductTrackingConstant.Tracking.VARIANT, ProductTrackingConstant.Tracking.DEFAULT_VALUE,
                            ProductTrackingConstant.Tracking.PROMO_POSITION, position,
                            ProductTrackingConstant.Tracking.KEY_DIMENSION_83, if (product.isFreeOngkirActive) ProductTrackingConstant.Tracking.VALUE_BEBAS_ONGKIR else ProductTrackingConstant.Tracking.VALUE_NONE_OTHER
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
                            String.format(ProductTrackingConstant.Label.DISCUSSION_SEE_ALL, numberOfThreadsShown)),
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
                            String.format(ProductTrackingConstant.Label.DISCUSSION_DETAIL, talkId, numberOfThreadsShown)),
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
                            String.format(ProductTrackingConstant.Label.DISCUSSION_EMPTY_QUESTION, isVariantSelected, totalAvailableVariants)),
                    userId
            )

            TrackingUtil.addComponentTracker(mapEvent, productInfo, componentTrackDataModel, ProductTrackingConstant.Action.CLICK_SEND_QUESTION)
        }

        fun eventClickByMe(productInfo: DynamicProductInfoP1?, componentTrackDataModel: ComponentTrackDataModel?) {
            val shopId = productInfo?.basic?.shopID ?: ""
            val productId = productInfo?.basic?.productID ?: ""
            val eventLabel = "{$shopId} - {$productId}"

            val mapEvent = TrackAppUtils.gtmData(
                    ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                    ProductTrackingConstant.Category.PDP,
                    ProductTrackingConstant.Action.CLICK_BY_ME,
                    eventLabel)

            TrackingUtil.addComponentTracker(mapEvent, productInfo, componentTrackDataModel, "")
        }

        fun eventTopAdsImageViewClicked(trackingQueue: TrackingQueue, userId: String, bannerId: String, position: Int, bannerName:String) {
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
                                            ))
                            )))

            trackingQueue.putEETracking(mapEvent)
        }
    }

    object Iris {

        fun eventDiscussionClickedIris(productInfo: DynamicProductInfoP1?, deeplinkUrl: String,
                                       shopName: String, componentTrackDataModel: ComponentTrackDataModel) {

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

            val mapOfData = mutableMapOf(ProductTrackingConstant.Tracking.KEY_EVENT to ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
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
                    "productId" to (productInfo?.basic?.getProductId() ?: ""),
                    "productUrl" to (productInfo?.basic?.url ?: ""),
                    "productDeeplinkUrl" to deeplinkUrl,
                    "productImageUrl" to imageUrl,
                    "productPrice" to (productInfo?.data?.price?.value ?: ""),
                    "isOfficialStore" to isOs.toString(),
                    "shopId" to (productInfo?.basic?.shopID ?: ""),
                    "shopName" to shopName,
                    "productPriceFormatted" to TrackingUtil.getFormattedPrice(productInfo?.data?.price?.value
                            ?: 0)) as MutableMap<String, Any>

            TrackingUtil.addComponentTracker(mapOfData, productInfo, componentTrackDataModel, ProductTrackingConstant.Action.CLICK_DISKUSI_PRODUCT_TAB)

        }

        fun eventReviewClickedIris(productInfo: DynamicProductInfoP1?, deeplinkUrl: String,
                                   shopName: String) {
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

            val mapOfData: Map<String, Any?> = mapOf(ProductTrackingConstant.Tracking.KEY_EVENT to "clickPDP",
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
                    "productId" to productInfo?.basic?.getProductId(),
                    "productUrl" to productInfo?.basic?.url,
                    "productDeeplinkUrl" to deeplinkUrl,
                    "productImageUrl" to imageUrl,
                    "productPrice" to productInfo?.data?.price?.value,
                    "isOfficialStore" to isOs.toString(),
                    "shopId" to productInfo?.basic?.shopID,
                    "shopName" to shopName,
                    "productPriceFormatted" to TrackingUtil.getFormattedPrice(productInfo?.data?.price?.value
                            ?: 0)
            )

            TrackApp.getInstance().gtm.sendGeneralEvent(mapOfData)
        }
    }

    object Branch {
        fun eventBranchItemView(productInfo: DynamicProductInfoP1?, userId: String?, description: String) {
            if (productInfo != null) {
                LinkerManager.getInstance().sendEvent(LinkerUtils.createGenericRequest(LinkerConstants.EVENT_ITEM_VIEW, TrackingUtil.createLinkerData(productInfo, userId, description)))
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
            TrackApp.getInstance().moEngage.sendEvent("Share_Event",
                    mutableMapOf<String, Any>(
                            "channel" to "lainnya",
                            "source" to "pdp_share"
                    )
            )
        }

        private fun sendMoEngage(productInfo: DynamicProductInfoP1,
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
    }

    object Impression {
        fun eventViewErrorWhenAddToCart(errorMessage: String, productId: String, userId: String) {
            val mapEvent = TrackAppUtils.gtmData(ProductTrackingConstant.PDP.EVENT_VIEW_PDP,
                    ProductTrackingConstant.Category.PDP,
                    ProductTrackingConstant.Action.ACTION_VIEW_ERROR_WHEN_ADD_TO_CART,
                    "not success - $errorMessage")
            mapEvent[ProductTrackingConstant.Tracking.KEY_USER_ID_VARIANT] = userId
            mapEvent[ProductTrackingConstant.Tracking.KEY_ISLOGGIN] = (userId.isNotEmpty()).toString()
            mapEvent[ProductTrackingConstant.Tracking.PROMO_ID] = productId
            TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
        }

        fun eventEcommerceDynamicComponent(trackingQueue: TrackingQueue?, componentTrackDataModel: ComponentTrackDataModel, productInfo: DynamicProductInfoP1?) {
            val productId = productInfo?.basic?.productID ?: ""
            val listOfCategoryId = productInfo?.basic?.category?.detail
            val categoryName = productInfo?.basic?.category?.name.orEmpty()
            val categoryString = "${listOfCategoryId?.getOrNull(0)?.id.orEmpty()} / ${listOfCategoryId?.getOrNull(1)?.id.orEmpty()} / ${listOfCategoryId?.getOrNull(2)?.id.orEmpty()} / $categoryName "

            val mapEvent = DataLayer.mapOf(
                    ProductTrackingConstant.Tracking.KEY_EVENT, "promoView",
                    ProductTrackingConstant.Tracking.KEY_CATEGORY, ProductTrackingConstant.Category.PDP,
                    ProductTrackingConstant.Tracking.KEY_ACTION, "impression - modular component",
                    ProductTrackingConstant.Tracking.KEY_LABEL, "",
                    "categoryId", "productId : $productId",
                    ProductTrackingConstant.Tracking.KEY_ECOMMERCE, DataLayer.mapOf(
                    "promoView", DataLayer.mapOf(
                    "promotions", DataLayer.listOf(
                    DataLayer.mapOf(
                            "id", "",
                            "name", "product detail page - $productId",
                            "creative", "layout:${productInfo?.layoutName};comp:${componentTrackDataModel.componentType};temp:${componentTrackDataModel.componentName};",
                            "creative_url", "",
                            "position", componentTrackDataModel.adapterPosition,
                            "category", categoryString,
                            "promo_id", "",
                            "promo_code", ""
                    )
            ))))
            mapEvent[ProductTrackingConstant.Tracking.KEY_PRODUCT_ID] = productInfo?.basic?.productID
                    ?: ""
            mapEvent[ProductTrackingConstant.Tracking.KEY_LAYOUT] = "layout:${productInfo?.layoutName};catName:${productInfo?.basic?.category?.name};catId:${productInfo?.basic?.category?.id};"
            mapEvent[ProductTrackingConstant.Tracking.KEY_COMPONENT] = "comp:${componentTrackDataModel.componentType};temp:${componentTrackDataModel.componentName};elem:${"impression - modular component"};cpos:${componentTrackDataModel.adapterPosition};"

            trackingQueue?.putEETracking(mapEvent as HashMap<String, Any>?)
        }

        val generateProduct = { irisSessionId: String, trackerListName: String?, productInfo: DynamicProductInfoP1?,
                                trackerAttribution: String?,
                                isTradeIn: Boolean, isDiagnosed: Boolean,
                                multiOrigin: Boolean, deeplinkUrl: String,
                                isStockAvailable: String ->

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

            arrayListOf(Product(
                    productInfo?.getProductName ?: "",
                    productInfo?.basic?.getProductId().toString(),
                    productInfo?.data?.price?.value?.toDouble() ?: 0.0,
                    productInfo?.getProductName,
                    ProductTrackingConstant.Tracking.DEFAULT_VALUE,
                    TrackingUtil.getEnhanceCategoryFormatted(productInfo?.basic?.category?.detail),
                    null,
                    trackerAttribution ?: ProductTrackingConstant.Tracking.DEFAULT_VALUE,
                    dimension55,
                    TrackingUtil.getMultiOriginAttribution(multiOrigin),
                    dimension83 ?: "",
                    productInfo?.shopTypeString ?: "",
                    if (isStockAvailable == "0") "not available" else "available",
                    1
            ))
        }

        fun sendTrackingBundle(key: String, bundle: Bundle) {
            TrackApp
                    .getInstance()
                    .gtm
                    .sendEnhanceEcommerceEvent(
                            key, bundle
                    )
        }

        val generateProductViewBundle = { irisSessionId: String, trackerListName: String?, productInfo: DynamicProductInfoP1?,
                                          shopInfo: ShopInfo?, trackerAttribution: String?,
                                          isTradeIn: Boolean, isDiagnosed: Boolean,
                                          multiOrigin: Boolean, deeplinkUrl: String
                                          , isStockAvailable: String ->

            val categoryIdLevel1 = productInfo?.basic?.category?.detail?.firstOrNull()?.id ?: ""
            val categoryNameLevel1 = productInfo?.basic?.category?.detail?.firstOrNull()?.name ?: ""

            val subCategoryIdLevel2 = productInfo?.basic?.category?.detail?.getOrNull(1)?.id ?: ""
            val subCategoryNameLevel2 = productInfo?.basic?.category?.detail?.getOrNull(1)?.name ?: ""

            val categoryIdLevel3 = productInfo?.basic?.category?.detail?.getOrNull(2)?.id ?: ""
            val categoryNameLevel3 = productInfo?.basic?.category?.detail?.getOrNull(2)?.name ?: ""

            val isPmInt = if (productInfo?.data?.isPowerMerchant == true) 1 else 0
            val isOsInt = if (productInfo?.data?.isOS == true) 1 else 0

            val productImageUrl = productInfo?.data?.media?.filter {
                it.type == "image"
            }?.firstOrNull()?.uRLOriginal ?: ""

            val products = generateProduct(irisSessionId, trackerListName, productInfo,
                    trackerAttribution, isTradeIn, isDiagnosed, multiOrigin, deeplinkUrl, isStockAvailable)

            val label = "${productInfo?.shopTypeString ?: ""} - ${productInfo?.basic?.shopName ?: ""} - ${productInfo?.data?.name ?: ""}"

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
                            TrackingUtil.getFormattedPrice(productInfo?.data?.price?.value ?: 0),
                            productInfo?.basic?.productID ?: "",
                            "layout:${productInfo?.layoutName};catName:${productInfo?.basic?.category?.name};catId:${productInfo?.basic?.category?.id}",
                            "",
                            (productInfo?.finalPrice.orZero().toString()),
                            productInfo?.getProductName,
                            categoryNameLevel3,
                            categoryIdLevel3,
                            categoryNameLevel1,
                            irisSessionId,
                            null,
                            ProductDetailViewsBundler.KEY,
                            "product page",
                            "view product page",
                            label,
                            null,
                            null
                    )
        }

        fun eventEnhanceEcommerceProductDetail(irisSessionId: String,
                                               trackerListName: String?,
                                               productInfo: DynamicProductInfoP1?,
                                               shopInfo: ShopInfo?,
                                               trackerAttribution: String?,
                                               isTradeIn: Boolean,
                                               isDiagnosed: Boolean,
                                               multiOrigin: Boolean,
                                               deeplinkUrl: String,
                                               isStockAvailable: String): Bundle {

            val sentBundle = generateProductViewBundle(
                    irisSessionId, trackerListName, productInfo, shopInfo,
                    trackerAttribution, isTradeIn, isDiagnosed, multiOrigin, deeplinkUrl,
                    isStockAvailable
            )
            sendTrackingBundle(
                    ProductDetailViewsBundler.KEY,
                    sentBundle
            )

            return sentBundle
        }

        fun eventRecommendationImpression(trackingQueue: TrackingQueue?, position: Int, product: RecommendationItem, isSessionActive: Boolean, pageName: String, pageTitle: String,
                                          productInfo: DynamicProductInfoP1?, componentTrackDataModel: ComponentTrackDataModel) {
            val listValue = ProductTrackingConstant.Tracking.LIST_DEFAULT + pageName +
                    (if (!isSessionActive) " - ${ProductTrackingConstant.Tracking.USER_NON_LOGIN}" else "") +
                    ProductTrackingConstant.Tracking.LIST_RECOMMENDATION + product.recommendationType + (if (product.isTopAds) " - product topads" else "")
            val topAdsAction = ProductTrackingConstant.Action.TOPADS_CLICK + (if (!isSessionActive) " - ${ProductTrackingConstant.Tracking.USER_NON_LOGIN}" else "")

            val enhanceEcommerceData = DataLayer.mapOf(
                    ProductTrackingConstant.Tracking.KEY_EVENT, ProductTrackingConstant.Action.PRODUCT_VIEW,
                    ProductTrackingConstant.Tracking.KEY_CATEGORY, ProductTrackingConstant.Category.PDP,
                    ProductTrackingConstant.Tracking.KEY_ACTION, topAdsAction,
                    ProductTrackingConstant.Tracking.KEY_LABEL, pageTitle,
                    ProductTrackingConstant.Tracking.KEY_PRODUCT_ID, product.productId.toString(),
                    ProductTrackingConstant.Tracking.KEY_LAYOUT, "layout:${productInfo?.layoutName};catName:${productInfo?.basic?.category?.name};catId:${productInfo?.basic?.category?.id};",
                    ProductTrackingConstant.Tracking.KEY_COMPONENT, "comp:${componentTrackDataModel.componentType};temp:${componentTrackDataModel.componentName};elem:${topAdsAction};cpos:${componentTrackDataModel.adapterPosition};",
                    ProductTrackingConstant.Tracking.KEY_ECOMMERCE, DataLayer.mapOf(
                    ProductTrackingConstant.Tracking.CURRENCY_CODE, ProductTrackingConstant.Tracking.CURRENCY_DEFAULT_VALUE,
                    ProductTrackingConstant.Tracking.IMPRESSIONS, DataLayer.listOf(
                    DataLayer.mapOf(ProductTrackingConstant.Tracking.PROMO_NAME, product.name,
                            ProductTrackingConstant.Tracking.ID, product.productId.toString(),
                            ProductTrackingConstant.Tracking.PRICE, TrackingUtil.removeCurrencyPrice(product.price),
                            ProductTrackingConstant.Tracking.BRAND, ProductTrackingConstant.Tracking.DEFAULT_VALUE,
                            ProductTrackingConstant.Tracking.VARIANT, ProductTrackingConstant.Tracking.DEFAULT_VALUE,
                            ProductTrackingConstant.Tracking.CATEGORY, product.categoryBreadcrumbs.toLowerCase(),
                            ProductTrackingConstant.Tracking.PROMO_POSITION, position + 1,
                            ProductTrackingConstant.Tracking.LIST, listValue,
                            ProductTrackingConstant.Tracking.KEY_DIMENSION_83, if (product.isFreeOngkirActive) ProductTrackingConstant.Tracking.VALUE_BEBAS_ONGKIR else ProductTrackingConstant.Tracking.VALUE_NONE_OTHER,
                            ProductTrackingConstant.Tracking.KEY_PRODUCT_ID, product.productId.toString()
                    )
            ))
            )
            trackingQueue?.putEETracking(enhanceEcommerceData as HashMap<String, Any>?)
        }


        fun eventImpressionMerchantVoucherUse(shopId: Int, merchantVoucherViewModelList: List<MerchantVoucherViewModel>,
                                              productInfo: DynamicProductInfoP1?) {
            if (merchantVoucherViewModelList.isNullOrEmpty()) return

            val promoId = merchantVoucherViewModelList[0].voucherId
            val mapEvent = TrackAppUtils.gtmData(
                    ProductTrackingConstant.MerchantVoucher.PROMO_VIEW,
                    ProductTrackingConstant.Category.PDP,
                    ProductTrackingConstant.Action.IMPRESSION_USE_MERCHANT_VOUCHER,
                    productInfo?.basic?.productID ?: ""
            )

            mapEvent[ProductTrackingConstant.Tracking.KEY_PROMO_ID] = promoId.toString()
            mapEvent[ProductTrackingConstant.Tracking.KEY_ECOMMERCE] = DataLayer.mapOf(
                    ProductTrackingConstant.MerchantVoucher.PROMO_VIEW, DataLayer.mapOf(
                    ProductTrackingConstant.Tracking.KEY_PROMOTIONS, TrackingUtil.createMvcListMap(merchantVoucherViewModelList, shopId, 0))
            )
            TrackingUtil.addComponentTracker(mapEvent, productInfo, null, ProductTrackingConstant.Action.IMPRESSION_USE_MERCHANT_VOUCHER)

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
                                            ))
                            )))

            trackingQueue.putEETracking(mapEvent)
        }
    }
}