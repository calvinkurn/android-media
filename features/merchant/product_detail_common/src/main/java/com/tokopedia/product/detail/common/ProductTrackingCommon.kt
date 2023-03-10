package com.tokopedia.product.detail.common

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.product.detail.common.ProductTrackingConstant.Tracking.BUSINESS_UNIT
import com.tokopedia.product.detail.common.ProductTrackingConstant.Tracking.BUSINESS_UNIT_PDP
import com.tokopedia.product.detail.common.ProductTrackingConstant.Tracking.CREATIVE
import com.tokopedia.product.detail.common.ProductTrackingConstant.Tracking.CURRENT_SITE
import com.tokopedia.product.detail.common.ProductTrackingConstant.Tracking.ID
import com.tokopedia.product.detail.common.ProductTrackingConstant.Tracking.KEY_ACTION
import com.tokopedia.product.detail.common.ProductTrackingConstant.Tracking.KEY_BUSINESS_UNIT
import com.tokopedia.product.detail.common.ProductTrackingConstant.Tracking.KEY_CATEGORY
import com.tokopedia.product.detail.common.ProductTrackingConstant.Tracking.KEY_COMPONENT
import com.tokopedia.product.detail.common.ProductTrackingConstant.Tracking.KEY_CURRENT_SITE
import com.tokopedia.product.detail.common.ProductTrackingConstant.Tracking.KEY_ECOMMERCE
import com.tokopedia.product.detail.common.ProductTrackingConstant.Tracking.KEY_EVENT
import com.tokopedia.product.detail.common.ProductTrackingConstant.Tracking.KEY_LABEL
import com.tokopedia.product.detail.common.ProductTrackingConstant.Tracking.KEY_LAYOUT
import com.tokopedia.product.detail.common.ProductTrackingConstant.Tracking.KEY_PRODUCT_ID
import com.tokopedia.product.detail.common.ProductTrackingConstant.Tracking.KEY_PROMOTIONS
import com.tokopedia.product.detail.common.ProductTrackingConstant.Tracking.KEY_SHOP_ID_SELLER
import com.tokopedia.product.detail.common.ProductTrackingConstant.Tracking.KEY_TRACKER_ID
import com.tokopedia.product.detail.common.ProductTrackingConstant.Tracking.KEY_USER_ID
import com.tokopedia.product.detail.common.ProductTrackingConstant.Tracking.NAME
import com.tokopedia.product.detail.common.ProductTrackingConstant.Tracking.POSITION
import com.tokopedia.product.detail.common.ProductTrackingConstant.Tracking.PROMO_VIEW
import com.tokopedia.product.detail.common.data.model.pdplayout.ProductDetailGallery
import com.tokopedia.product.detail.common.data.model.rates.P2RatesEstimateData
import com.tokopedia.product.detail.common.data.model.re.RestrictionAction
import com.tokopedia.product.detail.common.data.model.re.RestrictionData
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.trackingoptimizer.TrackingQueue

/**
 * Created by Yehezkiel on 17/05/21
 */
object ProductTrackingCommon {

    fun onFollowNplClickedVariantBottomSheet(
        productId: String,
        pageSource: String,
        shopId: String
    ) {
        val mapEvent = TrackAppUtils.gtmData(
            ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
            String.format(ProductTrackingConstant.Category.GLOBAL_VARIANT_BOTTOM_SHEET, pageSource),
            ProductTrackingConstant.Action.CLICK_FOLLOW,
            String.format(ProductTrackingConstant.Tracking.BUILDER_SHOP_ID, shopId)
        )
        addAdditionalParams(productId, mapEvent, pageSource)
    }

    fun onTokoCabangClicked(productId: String, pageSource: String) {
        val mapEvent = TrackAppUtils.gtmData(
            ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
            String.format(ProductTrackingConstant.Category.GLOBAL_VARIANT_BOTTOM_SHEET, pageSource),
            ProductTrackingConstant.Action.ACTION_CLICK_TOKOCABANG,
            ""
        )
        addAdditionalParams(productId, mapEvent, pageSource)
    }

    fun eventClickPilihVariant(
        productId: String,
        pageSource: String,
        cartType: String,
        parentId: String,
        productIdFromPreviousPage: String
    ) {
        val source = when (cartType) {
            ProductDetailCommonConstant.KEY_SAVE_BUNDLING_BUTTON -> ProductDetailCommonConstant.VALUE_PRODUCT_BUNDLING
            ProductDetailCommonConstant.KEY_SAVE_TRADEIN_BUTTON -> ProductDetailCommonConstant.VALUE_TRADE_IN
            else -> ""
        }

        val mapEvent = TrackAppUtils.gtmData(
            ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
            String.format(ProductTrackingConstant.Category.GLOBAL_VARIANT_BOTTOM_SHEET, pageSource),
            ProductTrackingConstant.Action.ACTION_CLICK_VARIANT,
            String.format(
                ProductTrackingConstant.Label.EVENT_LABEL_FLOW_CHOOSE_VARIANT,
                source,
                parentId,
                productId
            )
        )
        addAdditionalParams(productIdFromPreviousPage, mapEvent, pageSource)
    }

    fun eventActivationOvo(productId: String, userId: String) {
        val mapEvent = TrackAppUtils.gtmData(
            ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
            ProductTrackingConstant.Category.PDP,
            ProductTrackingConstant.Action.CLICK_BUY_ACTIVATION_OVO,
            ProductTrackingConstant.Label.EMPTY_LABEL
        )
        addComponentOvoTracker(mapEvent, productId, userId)
    }

    fun eventSeeBottomSheetOvo(title: String, productId: String, userId: String) {
        val mapEvent = TrackAppUtils.gtmData(
            ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
            ProductTrackingConstant.Category.PDP,
            "${ProductTrackingConstant.Action.CLICK_SEE_BOTTOMSHEET_OVO} $title",
            ProductTrackingConstant.Label.EMPTY_LABEL
        )
        addComponentOvoTracker(mapEvent, productId, userId)
    }

    fun eventTopupBottomSheetOvo(
        title: String,
        buttonTitle: String,
        productId: String,
        userId: String
    ) {
        val mapEvent = TrackAppUtils.gtmData(
            ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
            ProductTrackingConstant.Category.PDP,
            "${ProductTrackingConstant.Action.CLICK} - $buttonTitle ${ProductTrackingConstant.Action.CLICK_TOPUP_BOTTOMSHEET_OVO} $title",
            ProductTrackingConstant.Label.EMPTY_LABEL
        )
        addComponentOvoTracker(mapEvent, productId, userId)
    }

    fun addComponentOvoTracker(
        mapEvent: MutableMap<String, Any>,
        productId: String,
        userId: String
    ) {
        mapEvent[KEY_PRODUCT_ID] = productId
        mapEvent[ProductTrackingConstant.Tracking.KEY_USER_ID_VARIANT] = userId
        mapEvent[ProductTrackingConstant.Tracking.KEY_ISLOGGIN] = (userId != "0").toString()
        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }

    fun onRemindMeClicked(productId: String, pageSource: String) {
        val mapEvent = TrackAppUtils.gtmData(
            ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
            String.format(ProductTrackingConstant.Category.GLOBAL_VARIANT_BOTTOM_SHEET, pageSource),
            ProductTrackingConstant.Action.CLICK_NOTIFY_ME_VARIANT_BOTTOMSHEET,
            ""
        )

        addAdditionalParams(productId, mapEvent, pageSource)
    }

    fun onQuantityEditorClicked(
        productId: String,
        pageSource: String,
        oldQuantity: Int,
        newQuantity: Int
    ) {
        val label = "quantity button:${if (newQuantity > oldQuantity) "plus" else "minus"}"
        val mapEvent = TrackAppUtils.gtmData(
            ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
            String.format(ProductTrackingConstant.Category.GLOBAL_VARIANT_BOTTOM_SHEET, pageSource),
            ProductTrackingConstant.Action.CLICK_VARIANT_QUANTITY_EDITOR,
            label
        )

        addAdditionalParams(productId, mapEvent, pageSource)
    }

    fun onWishlistCheckClicked(productId: String, pageSource: String) {
        val mapEvent = TrackAppUtils.gtmData(
            ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
            String.format(ProductTrackingConstant.Category.GLOBAL_VARIANT_BOTTOM_SHEET, pageSource),
            ProductTrackingConstant.Action.CLICK_CHECK_WISHLIST,
            ""
        )

        addAdditionalParams(productId, mapEvent, pageSource)
    }

    fun onSeeCartVariantBottomSheetClicked(message: String, productId: String, pageSource: String) {
        val mapEvent = TrackAppUtils.gtmData(
            ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
            String.format(ProductTrackingConstant.Category.GLOBAL_VARIANT_BOTTOM_SHEET, pageSource),
            ProductTrackingConstant.Action.CLICK_TOASTER_LIHAT_SUCCESS_ATC,
            "toaster message:$message"
        )

        addAdditionalParams(productId, mapEvent, pageSource)
    }

    fun onVariantImageBottomSheetClicked(productId: String, pageSource: String) {
        val mapEvent = TrackAppUtils.gtmData(
            ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
            String.format(ProductTrackingConstant.Category.GLOBAL_VARIANT_BOTTOM_SHEET, pageSource),
            ProductTrackingConstant.Action.CLICK_PRODUCT_IMAGE,
            ""
        )

        addAdditionalParams(productId, mapEvent, pageSource)
    }

    fun onVariantGuidelineClicked(productId: String, pageSource: String) {
        val mapEvent = TrackAppUtils.gtmData(
            ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
            String.format(ProductTrackingConstant.Category.GLOBAL_VARIANT_BOTTOM_SHEET, pageSource),
            ProductTrackingConstant.Action.CLICK_VARIANT_BOTTOMSHEET_GUIDELINE,
            ""
        )

        addAdditionalParams(productId, mapEvent, pageSource)
    }

    fun onVariantPartiallySelected(errorMessage: String, productId: String, pageSource: String) {
        val mapEvent = TrackAppUtils.gtmData(
            ProductTrackingConstant.PDP.EVENT_VIEW_PDP_IRIS,
            String.format(ProductTrackingConstant.Category.GLOBAL_VARIANT_BOTTOM_SHEET, pageSource),
            ProductTrackingConstant.Action.VIEW_CHOOSE_VARIANT_ERROR,
            "not success - $errorMessage"
        )

        addAdditionalParams(productId, mapEvent, pageSource)
    }

    private fun addAdditionalParams(
        productId: String,
        mapEvent: MutableMap<String, Any>,
        pageSource: String
    ) {
        mapEvent[KEY_BUSINESS_UNIT] = generateBusinessUnit(pageSource)
        mapEvent[KEY_CURRENT_SITE] = CURRENT_SITE
        mapEvent[KEY_PRODUCT_ID] = productId

        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }

    fun eventEcommerceAtcError(
        errorMessage: String,
        productId: String,
        userId: String,
        pageSource: String
    ) {
        val mapEvent = TrackAppUtils.gtmData(
            ProductTrackingConstant.PDP.EVENT_VIEW_PDP_IRIS,
            String.format(ProductTrackingConstant.Category.GLOBAL_VARIANT_BOTTOM_SHEET, pageSource),
            ProductTrackingConstant.Action.ACTION_VIEW_ERROR_WHEN_ADD_TO_CART,
            "not success - $errorMessage"
        )
        mapEvent[ProductTrackingConstant.Tracking.KEY_USER_ID_VARIANT] = userId
        mapEvent[ProductTrackingConstant.Tracking.KEY_ISLOGGIN] = (userId.isNotEmpty()).toString()
        mapEvent[KEY_PRODUCT_ID] = productId
        mapEvent[KEY_BUSINESS_UNIT] = generateBusinessUnit(pageSource)
        mapEvent[KEY_CURRENT_SITE] = CURRENT_SITE
        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }

    fun eventEcommerceAddToCart(
        userId: String,
        cartId: String,
        buttonAction: Int,
        buttonText: String,
        productId: String,
        shopId: String,
        productName: String,
        productPrice: Double,
        quantity: Int,
        variantName: String,
        isMultiOrigin: Boolean,
        shopType: String = "",
        shopName: String = "",
        categoryName: String = "",
        categoryId: String = "",
        bebasOngkirType: String = "",
        pageSource: String = "",
        cdListName: String = "",
        isCod: Boolean,
        ratesEstimateData: P2RatesEstimateData?,
        buyerDistrictId: String,
        sellerDistrictId: String,
        lcaWarehouseId: String
    ) {
        val generateButtonActionString = when (buttonAction) {
            ProductDetailCommonConstant.OCS_BUTTON -> "$buttonText ocs"
            ProductDetailCommonConstant.OCC_BUTTON -> "$buttonText occ"
            else -> "$buttonText normal"
        }

        val multiOrigin = when (isMultiOrigin) {
            true -> "tokopedia"
            else -> "regular"
        }

        val cheapestShippingPrice = ratesEstimateData?.cheapestShippingPrice?.toLong()?.toString()
            ?: ""
        val shippingCourier = ratesEstimateData?.title ?: ""
        val shippingEta = ratesEstimateData?.etaText ?: ""
        val buyerSellerDistrictId = "$buyerDistrictId - $sellerDistrictId"

        val eventAction = if (buttonAction == ProductDetailCommonConstant.ATC_BUTTON)
            "click - tambah ke keranjang on global variant bottomsheet"
        else
            "click - $buttonText on global variant bottomsheet"

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            DataLayer.mapOf(
                ProductTrackingConstant.Tracking.KEY_EVENT,
                "addToCart",
                ProductTrackingConstant.Tracking.KEY_CATEGORY,
                String.format(
                    ProductTrackingConstant.Category.GLOBAL_VARIANT_BOTTOM_SHEET,
                    pageSource
                ),
                ProductTrackingConstant.Tracking.KEY_ACTION,
                eventAction,
                ProductTrackingConstant.Tracking.KEY_LABEL,
                if (buttonAction == ProductDetailCommonConstant.ATC_BUTTON) "" else "fitur : $generateButtonActionString",
                KEY_PRODUCT_ID,
                productId,
                ProductTrackingConstant.Tracking.KEY_HIT_USER_ID,
                userId,
                ProductTrackingConstant.Tracking.KEY_WAREHOUSE_ID,
                lcaWarehouseId,
                ProductTrackingConstant.Tracking.KEY_ISLOGGIN,
                (userId.isNotEmpty()).toString(),
                KEY_BUSINESS_UNIT,
                generateBusinessUnit(pageSource),
                KEY_CURRENT_SITE,
                CURRENT_SITE,

                ProductTrackingConstant.Tracking.KEY_ECOMMERCE,
                DataLayer.mapOf(
                    ProductTrackingConstant.Tracking.CURRENCY_CODE,
                    ProductTrackingConstant.Tracking.CURRENCY_DEFAULT_VALUE,
                    ProductTrackingConstant.Tracking.KEY_ADD,
                    DataLayer.mapOf(
                        ProductTrackingConstant.Tracking.PRODUCTS, DataLayer.listOf(
                            DataLayer.mapOf(
                                ProductTrackingConstant.Tracking.NAME,
                                productName,
                                ProductTrackingConstant.Tracking.ID,
                                productId,
                                ProductTrackingConstant.Tracking.PRICE,
                                productPrice,
                                ProductTrackingConstant.Tracking.BRAND,
                                ProductTrackingConstant.Tracking.VALUE_NONE_OTHER,
                                ProductTrackingConstant.Tracking.CATEGORY,
                                categoryName,
                                ProductTrackingConstant.Tracking.VARIANT,
                                variantName,
                                ProductTrackingConstant.Tracking.QUANTITY,
                                quantity,
                                ProductTrackingConstant.Tracking.KEY_PRODUCT_CATEGORY_ID,
                                categoryId,
                                ProductTrackingConstant.Tracking.KEY_PRODUCT_SHOP_ID,
                                shopId,
                                ProductTrackingConstant.Tracking.KEY_PRODUCT_SHOP_NAME,
                                shopName,
                                ProductTrackingConstant.Tracking.KEY_PRODUCT_SHOP_TYPE,
                                shopType,
                                ProductTrackingConstant.Tracking.KEY_DIMENSION_10,
                                isCod.toString(),
                                ProductTrackingConstant.Tracking.KEY_DIMENSION_12,
                                cheapestShippingPrice,
                                ProductTrackingConstant.Tracking.KEY_DIMENSION_14,
                                shippingCourier,
                                ProductTrackingConstant.Tracking.KEY_DIMENSION_16,
                                shippingEta,
                                ProductTrackingConstant.Tracking.KEY_DIMENSION_120,
                                buyerSellerDistrictId,
                                ProductTrackingConstant.Tracking.KEY_DIMENSION_79,
                                shopId,
                                ProductTrackingConstant.Tracking.KEY_DIMENSION_80,
                                shopName,
                                ProductTrackingConstant.Tracking.KEY_DIMENSION_81,
                                shopType,
                                ProductTrackingConstant.Tracking.KEY_DIMENSION_45,
                                cartId,
                                ProductTrackingConstant.Tracking.KEY_DIMENSION_82,
                                categoryId,
                                ProductTrackingConstant.Tracking.KEY_DIMENSION_40,
                                cdListName, //cd listname --> /tokonow - searchproduct - {organic/organic ads/topads productlist}
                                ProductTrackingConstant.Tracking.KEY_DIMENSION_54,
                                multiOrigin,
                                ProductTrackingConstant.Tracking.KEY_DIMENSION_83,
                                bebasOngkirType,
                                ProductTrackingConstant.Tracking.KEY_DIMENSION_38,
                                pageSource
                            )
                        )
                    )
                )
            )
        )
    }

    fun eventImageGallerySwipe(
        trackingQueue: TrackingQueue?,
        productId: String,
        mediaType: String,
        mediaUrl: String,
        position: String,
        userId: String,
        page: ProductDetailGallery.Page
    ) {

        val eventCategory: String
        val itemNameParam3: String

        when (page) {
            ProductDetailGallery.Page.ProductDetail -> {
                eventCategory = ProductTrackingConstant.Category.PDP
                itemNameParam3 = "overlay"

            }
            ProductDetailGallery.Page.VariantBottomSheet -> {
                eventCategory = ProductTrackingConstant.Category.PDP_VARIANT_BOTTOMSHEET
                itemNameParam3 = "overlay vbs"
            }
        }

        val mapEvent = hashMapOf<String, Any>(
            KEY_EVENT to PROMO_VIEW,
            KEY_ACTION to ProductTrackingConstant.Action.SWIPE_PRODUCT_PICTURE,
            KEY_CATEGORY to eventCategory,
            KEY_LABEL to "",
            BUSINESS_UNIT to BUSINESS_UNIT_PDP,
            KEY_CURRENT_SITE to CURRENT_SITE,
            KEY_PRODUCT_ID to productId,
            KEY_USER_ID to userId,
            KEY_ECOMMERCE to hashMapOf(
                PROMO_VIEW to hashMapOf(
                    KEY_PROMOTIONS to arrayListOf(
                        hashMapOf(
                            CREATIVE to "media type:$mediaType;",
                            POSITION to position,
                            NAME to "product detail page - $productId - $itemNameParam3",
                            ID to mediaUrl
                        )
                    )
                )
            )
        )

        trackingQueue?.putEETracking(mapEvent)
    }

    private fun generateBusinessUnit(pageSource: String) =
        if (pageSource == VariantPageSource.PDP_PAGESOURCE.source) {
            ProductTrackingConstant.Tracking.BUSINESS_UNIT_PDP
        } else {
            ProductTrackingConstant.Tracking.BUSINESS_UNIT
        }

    object Restriction {
        fun impressLocationRestriction(
            trackingQueue: TrackingQueue,
            data: RestrictionData,
            userId: String,
            shopId: String,
            pageSource: String = VariantPageSource.PDP_PAGESOURCE.source
        ) {
            val action = data.action.firstOrNull() ?: RestrictionAction()
            val productId = data.productId

            val label =
                "title:${action.title};subtitle:${action.description}"
            val itemId = "product detail page - bottomsheet restriction - $productId"

            val mapEvent = hashMapOf<String, Any>(
                KEY_EVENT to PROMO_VIEW,
                KEY_ACTION to ProductTrackingConstant.Action.IMPRESS_LOCATION_RESTRICTION,
                KEY_CATEGORY to ProductTrackingConstant.Category.PDP,
                KEY_LABEL to label,
                KEY_TRACKER_ID to ProductTrackingConstant.TrackerId.TRACKER_ID_IMPRESSION_LOCATION_RESTRICTION,
                KEY_CURRENT_SITE to CURRENT_SITE,
                KEY_PRODUCT_ID to productId,
                KEY_USER_ID to userId,
                KEY_SHOP_ID_SELLER to shopId,
                KEY_LAYOUT to "",
                KEY_COMPONENT to "",
                KEY_BUSINESS_UNIT to generateBusinessUnit(pageSource),
                KEY_ECOMMERCE to hashMapOf(
                    PROMO_VIEW to hashMapOf(
                        KEY_PROMOTIONS to arrayListOf(
                            hashMapOf(
                                CREATIVE to action.description,
                                POSITION to "0",
                                NAME to action.title,
                                ID to itemId
                            )
                        )
                    )
                )
            )

            trackingQueue?.putEETracking(mapEvent)
        }
        fun clickLocationRestriction(
            data: RestrictionData,
            userId: String,
            shopId: String,
            pageSource: String = VariantPageSource.PDP_PAGESOURCE.source
        ) {
            val action = data.action.firstOrNull() ?: RestrictionAction()
            val productId = data.productId

            val label =
                "title:${action.title};subtitle:${action.description};button_text:${action.buttonText}"
            val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PG,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.CLICK_LOCATION_RESTRICTION,
                label
            )
            mapEvent[KEY_TRACKER_ID] =
                ProductTrackingConstant.TrackerId.TRACKER_ID_CLICK_LOCATION_RESTRICTION
            mapEvent[ProductTrackingConstant.Tracking.KEY_USER_ID_VARIANT] = userId
            mapEvent[KEY_PRODUCT_ID] = productId
            mapEvent[KEY_SHOP_ID_SELLER] = shopId
            mapEvent[KEY_BUSINESS_UNIT] = generateBusinessUnit(pageSource)
            mapEvent[KEY_CURRENT_SITE] = CURRENT_SITE
            mapEvent[KEY_COMPONENT] = ""
            mapEvent[KEY_LAYOUT] = ""

            TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
        }
    }
}