package com.tokopedia.affiliate.feature.explore.data.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * @author by yfsx on 08/10/18.
 */
public class ExploreProductPojo {

    @SerializedName("adId")
    private int adId;
    @SerializedName("productId")
    private int productId;
    @SerializedName("image")
    private String image;
    @SerializedName("name")
    private String name;
    @SerializedName("commission")
    private String commission;

    public int getAdId() {
        return adId;
    }

    public void setAdId(int adId) {
        this.adId = adId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
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
