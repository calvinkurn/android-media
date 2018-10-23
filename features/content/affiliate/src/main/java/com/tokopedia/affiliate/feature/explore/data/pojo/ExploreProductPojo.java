package com.tokopedia.affiliate.feature.explore.data.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * @author by yfsx on 08/10/18.
 */
public class ExploreProductPojo {

    @SerializedName("adId")
    private String adId;
    @SerializedName("productId")
    private String productId;
    @SerializedName("image")
    private String image;
    @SerializedName("name")
    private String name;
    @SerializedName("commission")
    private String commission;

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }
}
