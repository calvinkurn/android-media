package com.tokopedia.core.product.model.goldmerchant;

import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.var.Badge;
import com.tokopedia.core.var.Label;

import java.util.ArrayList;
import java.util.List;

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

    @SerializedName("product_id")
    String productId;

    @SerializedName("labels")
    public List<Label> labels = new ArrayList<>();

    @SerializedName("badges")
    public List<Badge> badges = new ArrayList<>();

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

    public String getProductId() {
        return productId;
    }

    public List<Label> getLabels() {
        return labels;
    }

    public List<Badge> getBadges() {
        return badges;
    }
}
