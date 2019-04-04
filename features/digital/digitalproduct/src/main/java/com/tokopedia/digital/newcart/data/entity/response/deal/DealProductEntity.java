package com.tokopedia.digital.newcart.data.entity.response.deal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DealProductEntity {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("display_name")
    @Expose
    private String title;
    @SerializedName("thumbnail_web")
    @Expose
    private String imageThumbUrl;
    @SerializedName("sales_price")
    @Expose
    private long salesPrice;
    @SerializedName("mrp")
    @Expose
    private long mrp;
    @SerializedName("brand")
    @Expose
    private DealBrandEntity brand;

    public int getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public DealBrandEntity getBrand() {
        return brand;
    }

    public String getImageThumbUrl() {
        return imageThumbUrl;
    }

    public long getSalesPrice() {
        return salesPrice;
    }

    public long getMrp() {
        return mrp;
    }
}
