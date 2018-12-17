package com.tokopedia.topads.sdk.domain.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Author errysuprayogi on 29,November,2018
 */
public class TopAdsGqlResponse {

    @SerializedName("displayAdsV3")
    @Expose
    TopAdsModel adsModel;

    public TopAdsModel getAdsModel() {
        return adsModel;
    }

    public void setAdsModel(TopAdsModel adsModel) {
        this.adsModel = adsModel;
    }
}
