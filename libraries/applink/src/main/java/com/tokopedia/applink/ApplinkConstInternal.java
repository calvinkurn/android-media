package com.tokopedia.applink;

/**
 * This class is used to store deeplink started with scheme "tokopedia-android-internal".
 * Since it is for android internal only, if the applink is shared between iOS or site,
 * please create with "tokopedia" scheme and put into different file.
 * <p>
 * To make this deeplnks work, These deeplinks must be registered in the manifest using intent filter
 * (see ProductDetailActivity manifest for example)
 */
public class ApplinkConstInternal {
    public static final String INTERNAL_SCHEME = "tokopedia-android-internal";

    public static final String HOST_MARKETPLACE = "marketplace";

    public static final String INTERNAL_MARKETPLACE = INTERNAL_SCHEME + "://" + HOST_MARKETPLACE;

    // ProductDetailActivity
    public static final String PRODUCT_DETAIL = INTERNAL_MARKETPLACE + "/product/{id}/";

    // ProductEditActivity
    public static final String PRODUCT_EDIT = INTERNAL_MARKETPLACE + "/product/{id}/edit";
    // ReviewProductActivity, "x_prd_nm" = productName
    public static final String PRODUCT_REVIEW = INTERNAL_MARKETPLACE + "/product/{id}/review";

    // CheckoutVariantActivity, "EXTRA_ATC_REQUEST" = AtcRequestParam
    public static final String EXPRESS_CHECKOUT = INTERNAL_MARKETPLACE + "checkoutvariant";

    // IntermediaryActivity
    public static final String DISCOVERY_CATEGORY_DETAIL = INTERNAL_MARKETPLACE + "/category/{DEPARTMENT_ID}/";
}
