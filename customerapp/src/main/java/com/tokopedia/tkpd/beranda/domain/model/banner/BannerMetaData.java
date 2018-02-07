package com.tokopedia.tkpd.beranda.domain.model.banner;

import com.google.gson.annotations.SerializedName;

/**
 * @author by errysuprayogi on 11/28/17.
 */
public class BannerMetaData {

    @SerializedName("total_data")
    private int totalData;

    public int getTotalData() {
        return totalData;
    }

    public void setTotalData(int totalData) {
        this.totalData = totalData;
    }
}
