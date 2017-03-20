
package com.tokopedia.core.product.model.shopscore.detail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ShopScoreDetailDataServiceModel {

    @SerializedName("Data")
    @Expose
    private List<ShopScoreDetailItemServiceModel> data = null;
    @SerializedName("BadgeScore")
    @Expose
    private Integer badgeScore;
    @SerializedName("SumData")
    @Expose
    private SumData sumData;

    public List<ShopScoreDetailItemServiceModel> getData() {
        return data;
    }

    public void setData(List<ShopScoreDetailItemServiceModel> data) {
        this.data = data;
    }

    public Integer getBadgeScore() {
        return badgeScore;
    }

    public void setBadgeScore(Integer badgeScore) {
        this.badgeScore = badgeScore;
    }

    public SumData getSumData() {
        return sumData;
    }

    public void setSumData(SumData sumData) {
        this.sumData = sumData;
    }

}
