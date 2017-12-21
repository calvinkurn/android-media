package com.tokopedia.core.manage.general.districtrecommendation.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Irfan Khoirul on 07/11/17.
 */

public class TokenEntity {

    @SerializedName("district_recommendation")
    @Expose
    private String districtRecommendation;
    @SerializedName("ut")
    @Expose
    private int unixTime;

    public String getDistrictRecommendation() {
        return districtRecommendation;
    }

    public void setDistrictRecommendation(String districtRecommendation) {
        this.districtRecommendation = districtRecommendation;
    }

    public int getUnixTime() {
        return unixTime;
    }

    public void setUnixTime(int unixTime) {
        this.unixTime = unixTime;
    }

}
