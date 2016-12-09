
package com.tokopedia.core.database.recharge.product;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Attributes {

    @SerializedName("desc")
    @Expose
    private String desc;
    @SerializedName("detail")
    @Expose
    private String detail;
    @SerializedName("detail_url")
    @Expose
    private String detailUrl;
    @SerializedName("info")
    @Expose
    private String info;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("promo")
    @Expose
    private Promo promo;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("weight")
    @Expose
    private Integer weight;

    /**
     *
     * @return
     *     The desc
     */
    public String getDesc() {
        return desc;
    }

    /**
     *
     * @param desc
     *     The desc
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     *
     * @return
     *     The detail
     */
    public String getDetail() {
        return detail;
    }

    /**
     *
     * @param detail
     *     The detail
     */
    public void setDetail(String detail) {
        this.detail = detail;
    }

    /**
     *
     * @return
     *     The detailUrl
     */
    public String getDetailUrl() {
        return detailUrl;
    }

    /**
     *
     * @param detailUrl
     *     The detail_url
     */
    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }

    /**
     *
     * @return
     *     The info
     */
    public String getInfo() {
        return info;
    }

    /**
     *
     * @param info
     *     The info
     */
    public void setInfo(String info) {
        this.info = info;
    }

    /**
     *
     * @return
     *     The price
     */
    public String getPrice() {
        return price;
    }

    /**
     *
     * @param price
     *     The price
     */
    public void setPrice(String price) {
        this.price = price;
    }

    /**
     *
     * @return
     *     The promo
     */
    public Promo getPromo() {
        return promo;
    }

    /**
     *
     * @param promo
     *     The promo
     */
    public void setPromo(Promo promo) {
        this.promo = promo;
    }

    /**
     *
     * @return
     *     The status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     *
     * @param status
     *     The status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }
}