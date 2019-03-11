package com.tokopedia.applink;

public class ApplinkConstInternal {
    public static final String INTERNAL_SCHEME = "tokopedia-android-internal";

    public static final String HOST_MARKETPLACE = "marketplace";

    // ProductDetailActivity
    public static final String PRODUCT_DETAIL   = INTERNAL_SCHEME + "://" + HOST_MARKETPLACE + "/product/{id}/";
    // ProductEditActivity
    public static final String PRODUCT_EDIT     = INTERNAL_SCHEME + "://" + HOST_MARKETPLACE + "/product/{id}/edit";
    // ReviewProductActivity, "x_prd_nm" = productName
    public static final String PRODUCT_REVIEW   = INTERNAL_SCHEME + "://" + HOST_MARKETPLACE + "/product/{id}/review";

    // CheckoutVariantActivity, "EXTRA_ATC_REQUEST" = AtcRequestParam
    public static final String EXPRESS_CHECKOUT = INTERNAL_SCHEME + "://" + HOST_MARKETPLACE + "checkoutvariant";

}
