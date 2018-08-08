package com.tokopedia.tkpd.thankyou.data.pojo.digital.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ActionField {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("affiliation")
    @Expose
    private String affiliation;
    @SerializedName("revenue")
    @Expose
    private String revenue;
    @SerializedName("tax")
    @Expose
    private String tax;
    @SerializedName("shipping")
    @Expose
    private String shipping;
    @SerializedName("coupon")
    @Expose
    private String coupon;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAffiliation() {
        return affiliation;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    public String getRevenue() {
        return revenue;
    }

    public void setRevenue(String revenue) {
        this.revenue = revenue;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getShipping() {
        return shipping;
    }

    public void setShipping(String shipping) {
        this.shipping = shipping;
    }

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }
}