package com.tokopedia.core.product.model.goldmerchant;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HenryPri on 16/06/17.
 */

public class FeaturedProductItem {
    @SerializedName("name")
    String name;

    @SerializedName("uri")
    String uri;

    @SerializedName("price")
    String price;

    @SerializedName("image_uri")
    String imageUri;

    public String getName() {
        return name;
    }

    public String getUri() {
        return uri;
    }

    public String getPrice() {
        return price;
    }

    public String getImageUri() {
        return imageUri;
    }
}
