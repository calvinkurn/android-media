package com.tokopedia.loyalty.view.data;

import java.util.List;

/**
 * @author  by alvarisi on 4/4/18.
 */

public class CouponsDataWrapper {
    private EmptyMessage emptyMessage;
    private List<CouponData> coupons;

    public CouponsDataWrapper() {
    }

    public EmptyMessage getEmptyMessage() {
        return emptyMessage;
    }

    public void setEmptyMessage(EmptyMessage emptyMessage) {
        this.emptyMessage = emptyMessage;
    }

    public List<CouponData> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<CouponData> coupons) {
        this.coupons = coupons;
    }
}
