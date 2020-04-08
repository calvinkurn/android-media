
package com.tokopedia.tokopoints.view.model.section;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.tokopoints.view.model.CouponValueEntity;

import java.util.List;

public class LayoutCouponAttr {

    @SerializedName("couponList")
    @Expose
    private List<CouponValueEntity> couponList = null;

    public List<CouponValueEntity> getCouponList() {
        return couponList;
    }

    public void setCouponList(List<CouponValueEntity> couponList) {
        this.couponList = couponList;
    }

}
