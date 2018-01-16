package com.tokopedia.tkpd.thankyou.data.pojo.marketplace.tracker;

import java.util.HashMap;

/**
 * Created by okasurya on 12/12/17.
 */
public class ActionField extends HashMap<String, Object> {
    private String id;
    private String affiliation;
    private String revenue;
    private String tax;
    private String shipping;
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
