package com.tokopedia.wishlist.common.data.source.cloud.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductIsWishlistedResponse {

    @SerializedName("ProductWishlistQuery")
    @Expose
    private boolean isWishlisted;

    public boolean isWishlisted() {
        return isWishlisted;
    }

    public void setWishlisted(boolean wishlisted) {
        isWishlisted = wishlisted;
    }
}
