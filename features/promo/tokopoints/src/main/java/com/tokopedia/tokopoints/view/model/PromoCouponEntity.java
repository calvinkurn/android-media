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
    @SerializedName("has_next")
    private boolean hasNext;

    @Expose
    @SerializedName("total_data")
    private int totalData;

    @Expose
    @SerializedName("extra_info")
    private List<CouponExtraInfoEntity> extraInfo;

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

    public List<CouponExtraInfoEntity> getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(List<CouponExtraInfoEntity> extraInfo) {
        this.extraInfo = extraInfo;
    }

    public List<CouponValueEntity> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<CouponValueEntity> coupons) {
        this.coupons = coupons;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public int getTotalData() {
        return totalData;
    }

    public void setTotalData(int totalData) {
        this.totalData = totalData;
    }

    @Override
    public String toString() {
        return "PromoCouponEntity{" +
                "coupons=" + coupons +
                ", hasNext=" + hasNext +
                ", totalData=" + totalData +
                ", extraInfo=" + extraInfo +
                ", emptyMessage=" + emptyMessage +
                '}';
    }
}
