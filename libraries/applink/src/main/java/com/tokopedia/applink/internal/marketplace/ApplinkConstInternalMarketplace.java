package com.tokopedia.applink.internal.marketplace;

import static com.tokopedia.applink.internal.ApplinkConstInternal.INTERNAL_MARKETPLACE;

/**
 * This class is used to store deeplink "tokopedia-android-internal://marketplace".
 */
public class ApplinkConstInternalMarketplace {

    // ProductDetailActivity
    public static final String PRODUCT_DETAIL = INTERNAL_MARKETPLACE + "/product/{id}/";
    public static final String PRODUCT_DETAIL_WITH_ATTRIBUTION = INTERNAL_MARKETPLACE +
            "/product/{id}/?tracker_attribution={tracker_attribution},tracker_list_name={tracker_list_name}";

    // ProductEditActivity
    public static final String PRODUCT_EDIT = INTERNAL_MARKETPLACE + "/product/{id}/edit";
    // ReviewProductActivity, "x_prd_nm" = productName
    public static final String PRODUCT_REVIEW = INTERNAL_MARKETPLACE + "/product/{id}/review";

    // CheckoutVariantActivity, "EXTRA_ATC_REQUEST" = AtcRequestParam
    public static final String EXPRESS_CHECKOUT = INTERNAL_MARKETPLACE + "/checkoutvariant";

    // IntermediaryActivity
    public static final String DISCOVERY_CATEGORY_DETAIL = INTERNAL_MARKETPLACE + "/category/{DEPARTMENT_ID}/";
    // ImageReviewGalleryActivity
    public static final String IMAGE_REVIEW_GALLERY = INTERNAL_MARKETPLACE + "/product/{id}/review/gallery";

    public static final String OPEN_SHOP = INTERNAL_MARKETPLACE + "/buka-toko";
}
