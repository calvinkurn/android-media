package com.tokopedia.tokopoints.view.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class PromoCouponEntity {

    @Expose
    @SerializedName("coupons")
    private List<CouponValueEntity> coupons;

    @Expose
    @SerializedName("tokopointsEmptyMessage")
    private Map<String, String> emptyMessage;

    @SerializedName("tokopointsPaging")
    private TokopointPaging paging;

    public TokopointPaging getPaging() {
        return paging;
    }

    public void setPaging(TokopointPaging paging) {
        this.paging = paging;
    }

    public Map<String, String> getEmptyMessage() {
        return emptyMessage;
    }

    public void setEmptyMessage(Map<String, String> emptyMessage) {
        this.emptyMessage = emptyMessage;
    }

    public List<CouponValueEntity> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<CouponValueEntity> coupons) {
        this.coupons = coupons;
    }

    @Override
    public String toString() {
        return "PromoCouponEntity{" +
                "coupons=" + coupons +
                ", emptyMessage=" + emptyMessage +
                '}';
    }
}
