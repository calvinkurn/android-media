package com.tokopedia.applink.internal

import com.tokopedia.applink.internal.ApplinkConstInternal.HOST_MARKETPLACE
import com.tokopedia.applink.internal.ApplinkConstInternal.INTERNAL_SCHEME

/**
 * This class is used to store deeplink "tokopedia-android-internal://marketplace".
 */
object ApplinkConstInternalMarketplace {

    val INTERNAL_MARKETPLACE = "${INTERNAL_SCHEME}://${HOST_MARKETPLACE}"

    // ProductDetailActivity
    @JvmField
    val PRODUCT_DETAIL = "$INTERNAL_MARKETPLACE/product/{id}/"
    @JvmField
    val PRODUCT_DETAIL_DOMAIN = "$INTERNAL_MARKETPLACE/product/{shop_domain}/{product_key}/"

    // ProductEditActivity
    @JvmField
    val PRODUCT_EDIT = "$INTERNAL_MARKETPLACE/product/{id}/edit"
    // ReviewProductActivity, "x_prd_nm" = productName
    @JvmField
    val PRODUCT_REVIEW = "$INTERNAL_MARKETPLACE/product/{id}/review"

    // CheckoutVariantActivity, "EXTRA_ATC_REQUEST" = AtcRequestParam
    @JvmField
    val EXPRESS_CHECKOUT = "$INTERNAL_MARKETPLACE/checkoutvariant"

    // IntermediaryActivity
    @JvmField
    val DISCOVERY_CATEGORY_DETAIL = "$INTERNAL_MARKETPLACE/category/{DEPARTMENT_ID}/"
    // ImageReviewGalleryActivity
    @JvmField
    val IMAGE_REVIEW_GALLERY = "$INTERNAL_MARKETPLACE/product/{id}/review/gallery"

    @JvmField
    val OPEN_SHOP = "$INTERNAL_MARKETPLACE/buka-toko"
}
