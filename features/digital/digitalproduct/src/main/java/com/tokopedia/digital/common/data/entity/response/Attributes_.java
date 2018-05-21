
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

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Integer getPricePlain() {
        return pricePlain;
    }

    public void setPricePlain(Integer pricePlain) {
        this.pricePlain = pricePlain;
    }

    public Promo getPromo() {
        return promo;
    }

    public void setPromo(Promo promo) {
        this.promo = promo;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
