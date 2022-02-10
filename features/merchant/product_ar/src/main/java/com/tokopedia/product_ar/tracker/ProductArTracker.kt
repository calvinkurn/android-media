package com.tokopedia.product_ar.tracker

import com.tokopedia.product.detail.common.ProductTrackingConstant
import com.tokopedia.product_ar.model.ArMetaData
import com.tokopedia.product_ar.model.ProductAr
import com.tokopedia.track.TrackApp

object ProductArTracker {

    private const val EVENT_AR = "clickProductAR"
    private const val EVENT_ATC = "add_to_cart"
    private const val CATEGORY_AR = "product augmented reality"
    private const val ACTION_CLICK_VARIANT_AR = "click - select variant on product augmented reality"
    private const val ACTION_CLICK_GALLERY_AR = "click - open image from gallery"
    private const val ACTION_CLICK_COMPARISSON_AR = "click - comparing variants floating button"
    private const val ACTION_CLICK_SAVE_PHOTO_AR = "click - simpan foto"
    private const val ACTION_CLICK_ATC_AR = "click - tambah ke keranjang on product augmented reality"
    private const val ACTION_CLICK_BACK_COMPARISSON_AR = "click - exit product augmented reality screen"

    fun openScreen(productId: String, userId: String) {

        val mapEvent = mapOf(
                "event" to "openScreen",
                "eventCategory" to ProductTrackingConstant.Category.PDP,
                "businessUnit" to ProductTrackingConstant.Tracking.BUSINESS_UNIT_PDP,
                "currentSite" to ProductTrackingConstant.Tracking.CURRENT_SITE,
                "productId" to productId,
                "userId" to userId,
                "isLoggedInStatus" to userId.isNotEmpty().toString()
        )

        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }

    fun clickVariant(productId: String,
                     variantName: String,
                     userId: String) {

        val mapEvent = mapOf(
                "event" to EVENT_AR,
                "eventAction" to ACTION_CLICK_VARIANT_AR,
                "eventCategory" to CATEGORY_AR,
                "eventLabel" to "child_id:${productId};variant_name:warna;variant_value:${variantName}",
                "businessUnit" to ProductTrackingConstant.Tracking.BUSINESS_UNIT_PDP,
                "currentSite" to ProductTrackingConstant.Tracking.CURRENT_SITE,
                "productId" to productId,
                "userId" to userId,
                "isLoggedInStatus" to userId.isNotEmpty().toString()
        )

        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }

    fun clickGallery(productId: String) {
        val mapEvent = mapOf(
                "event" to EVENT_AR,
                "eventAction" to ACTION_CLICK_GALLERY_AR,
                "eventCategory" to CATEGORY_AR,
                "eventLabel" to "is_success:true",
                "businessUnit" to ProductTrackingConstant.Tracking.BUSINESS_UNIT_PDP,
                "currentSite" to ProductTrackingConstant.Tracking.CURRENT_SITE,
                "productId" to productId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }

    fun clickComparisson(productId: String) {
        val mapEvent = mapOf(
                "event" to EVENT_AR,
                "eventAction" to ACTION_CLICK_COMPARISSON_AR,
                "eventCategory" to CATEGORY_AR,
                "eventLabel" to "",
                "businessUnit" to ProductTrackingConstant.Tracking.BUSINESS_UNIT_PDP,
                "currentSite" to ProductTrackingConstant.Tracking.CURRENT_SITE,
                "productId" to productId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }

    fun savePhoto(productId: String,
                  isSuccess: Boolean) {
        val mapEvent = mapOf(
                "event" to EVENT_AR,
                "eventAction" to ACTION_CLICK_SAVE_PHOTO_AR,
                "eventCategory" to CATEGORY_AR,
                "eventLabel" to "is_success:$isSuccess",
                "businessUnit" to ProductTrackingConstant.Tracking.BUSINESS_UNIT_PDP,
                "currentSite" to ProductTrackingConstant.Tracking.CURRENT_SITE,
                "productId" to productId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }


    fun successAtc(arData: ProductAr?,
                   shopId: String,
                   arMetaData: ArMetaData,
                   userId: String,
                   cartId: String) {
        val productId = arData?.productID ?: ""
        val productName = arData?.name ?: ""
        val price = arData?.getFinalPrice()?.toString() ?: ""
        val stock = arData?.getFinalStock()?.toString() ?: ""

        val categoryNameLvl1 = arMetaData.categoryDetail.firstOrNull()?.name ?: ""
        val categoryNameLvl2 = arMetaData.categoryDetail.getOrNull(1)?.name ?: ""
        val categoryNameLvl3 = arMetaData.categoryDetail.getOrNull(3)?.name ?: ""
        val itemCategory = "$categoryNameLvl1 / $categoryNameLvl2 / $categoryNameLvl3 /  ${arMetaData.categoryID}"
        val mapEvent = mapOf(
                "event" to EVENT_ATC,
                "eventAction" to ACTION_CLICK_ATC_AR,
                "eventCategory" to CATEGORY_AR,
                "eventLabel" to "",
                "businessUnit" to ProductTrackingConstant.Tracking.BUSINESS_UNIT_PDP,
                "currentSite" to ProductTrackingConstant.Tracking.CURRENT_SITE,
                "productId" to productId,
                "userId" to userId,
                "items" to listOf(mapOf(
                        "category_id" to arMetaData.categoryID,
                        "dimension45" to cartId,
                        "item_brand" to arMetaData.shopName,
                        "item_category" to itemCategory,
                        "item_id" to productId,
                        "item_name" to productName,
                        "item_variant" to productId,
                        "price" to price,
                        "quantity" to stock,
                        "shop_id" to shopId,
                        "shop_name" to arMetaData.shopName,
                        "shop_type" to arMetaData.shopType
                )
                )
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }

    fun interactionTimeTrack(productId: String, accessTime: Long, variantSize: Int, variantTried: Int) {
        val interactionTimeSecond = (System.currentTimeMillis() - accessTime) / 1000
        val mapEvent = mapOf(
                "event" to EVENT_AR,
                "eventAction" to ACTION_CLICK_BACK_COMPARISSON_AR,
                "eventCategory" to CATEGORY_AR,
                "eventLabel" to "interaction_time:$interactionTimeSecond;variants_available:$variantSize;variant_tried:$variantTried",
                "businessUnit" to ProductTrackingConstant.Tracking.BUSINESS_UNIT_PDP,
                "currentSite" to ProductTrackingConstant.Tracking.CURRENT_SITE,
                "productId" to productId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }
}