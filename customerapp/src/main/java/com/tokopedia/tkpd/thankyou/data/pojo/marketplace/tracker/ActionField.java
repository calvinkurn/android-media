package com.tokopedia.tkpd.thankyou.data.pojo.marketplace.tracker;

import java.util.HashMap;

/**
 * Created by okasurya on 12/12/17.
 */
public class ActionField extends HashMap<String, Object> {

    private static final String ID = "id";
    private static final String AFFILIATION = "affiliation";
    private static final String REVENUE = "revenue";
    private static final String TAX = "tax";
    private static final String SHIPPING = "shipping";
    private static final String COUPON = "coupon";

    public void setId(String id) {
        this.put(ID, id);
    }

    public void setAffiliation(String affiliation) {
        this.put(AFFILIATION, affiliation);
    }

    public void setRevenue(String revenue) {
        this.put(REVENUE, revenue);
    }

    public void setTax(String tax) {
        this.put(TAX, tax);
    }

    public void setShipping(String shipping) {
        this.put(SHIPPING, shipping);
    }

    public void setCoupon(String coupon) {
        this.put(COUPON, coupon);
    }
}
