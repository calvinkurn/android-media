
package com.tokopedia.digital.common.data.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Vishal Gupta 7th May, 2018
 */
public class Attributes_ {

    @SerializedName("desc")
    @Expose
    private String desc;
    @SerializedName("detail")
    @Expose
    private String detail;
    @SerializedName("detail_compact")
    @Expose
    private String detailCompact;
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
    private Integer pricePlain;
    @SerializedName("promo")
    @Expose
    private Promo promo;
    @SerializedName("status")
    @Expose
    private Integer status;

    public String getDesc() {
        return desc;
    }

    public String getDetail() {
        return detail;
    }

    public String getDetailCompact() {
        return detailCompact;
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

    public Integer getPricePlain() {
        return pricePlain;
    }

    public Promo getPromo() {
        return promo;
    }

    public Integer getStatus() {
        return status;
    }

}
