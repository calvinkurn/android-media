package com.tokopedia.digital.common.data.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 4/28/17.
 */

public class AttributesProduct {

    @SerializedName("desc")
    @Expose
    private String desc;
    @SerializedName("detail")
    @Expose
    private String detail;
    @SerializedName("detail_url")
    @Expose
    private String detailUrl;
    @SerializedName("detail_url_text")
    @Expose
    private String detailUrlText;
    @SerializedName("info")
    @Expose
    private String info;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("price_plain")
    @Expose
    private int pricePlain;
    @SerializedName("promo")
    @Expose
    private Promo promo;
    @SerializedName("status")
    @Expose
    private int status;

    public String getDesc() {
        return desc;
    }

    public String getDetail() {
        return detail;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public String getDetailUrlText() {
        return detailUrlText;
    }

    public String getInfo() {
        return info;
    }

    public String getPrice() {
        return price;
    }

    public int getPricePlain() {
        return pricePlain;
    }

    public Promo getPromo() {
        return promo;
    }

    public int getStatus() {
        return status;
    }
}
