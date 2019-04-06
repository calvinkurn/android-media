package com.tokopedia.applink.internal

import com.tokopedia.applink.internal.ApplinkConstInternal.INTERNAL_SCHEME

/**
 * This class is used to store deeplink "tokopedia-android-internal://marketplace".
 * Order by name
 */
object ApplinkConstInternalMarketplace {

    @JvmField
    val HOST_MARKETPLACE = "marketplace"

    @JvmField
    val INTERNAL_MARKETPLACE = "${INTERNAL_SCHEME}://${HOST_MARKETPLACE}"


    // IntermediaryActivity
    @JvmField
    val DISCOVERY_CATEGORY_DETAIL = "$INTERNAL_MARKETPLACE/category/{DEPARTMENT_ID}/"

    // CheckoutVariantActivity, "EXTRA_ATC_REQUEST" = AtcRequestParam
    @JvmField
    val EXPRESS_CHECKOUT = "$INTERNAL_MARKETPLACE/checkout-variant"


    // ProductDetailActivity
    @JvmField
    val PRODUCT_CATEGORY_PICKER = "$INTERNAL_MARKETPLACE/product-category-picker/{id}/"
    @JvmField
    val PRODUCT_DETAIL = "$INTERNAL_MARKETPLACE/product-detail/{id}/"
    @JvmField
    val PRODUCT_DETAIL_DOMAIN = "$INTERNAL_MARKETPLACE/product-detail/{shop_domain}/{product_key}/"
    // ProductEditActivity
    @JvmField
    val PRODUCT_EDIT_ITEM = "$INTERNAL_MARKETPLACE/product-edit-item/{id}/"
    @JvmField
    val PRODUCT_EDIT_VARIANT_DASHBOARD = "$INTERNAL_MARKETPLACE/product-edit-variant-dashboard/"
    @JvmField
    val PRODUCT_ETALASE_PICKER = "$INTERNAL_MARKETPLACE/product-etalase-picker/{id}/"
    @JvmField
    val PRODUCT_MANAGE_LIST = "$INTERNAL_MARKETPLACE/product-manage-list"
    // ReviewProductActivity, "x_prd_nm" = productName
    @JvmField
    val PRODUCT_REVIEW = "$INTERNAL_MARKETPLACE/product/{id}/review"




    // ImageReviewGalleryActivity
    @JvmField
    val IMAGE_REVIEW_GALLERY = "$INTERNAL_MARKETPLACE/product/{id}/review/gallery"

    //ShopOpenRoutingActivity
    @JvmField
    val OPEN_SHOP = "$INTERNAL_MARKETPLACE/shop-open"

    // Gold Merchant
    @JvmField
    val GOLD_MERCHANT_SUBSCRIBE_DASHBOARD = "$INTERNAL_MARKETPLACE/gold-merchant-subscribe-dashboard"
}
