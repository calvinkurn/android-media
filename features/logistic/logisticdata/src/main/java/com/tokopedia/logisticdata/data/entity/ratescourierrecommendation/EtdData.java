package com.tokopedia.logisticdata.data.entity.ratescourierrecommendation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Irfan Khoirul on 02/08/18.
 */

public class EtdData {

    @SerializedName("min_etd")
    @Expose
    private int minEtd;
    @SerializedName("max_etd")
    @Expose
    private int max_etd;

    public EtdData() {
    }

    public int getMinEtd() {
        return minEtd;
    }

    public void setMinEtd(int minEtd) {
        this.minEtd = minEtd;
    }

    public int getMax_etd() {
        return max_etd;
    }

    public void setMax_etd(int max_etd) {
        this.max_etd = max_etd;
    }
}
