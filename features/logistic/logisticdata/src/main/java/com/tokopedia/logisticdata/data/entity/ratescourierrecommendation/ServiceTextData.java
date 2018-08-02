package com.tokopedia.logisticdata.data.entity.ratescourierrecommendation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Irfan Khoirul on 02/08/18.
 */

public class ServiceTextData {

    @SerializedName("text_range_price")
    @Expose
    private String textRangePrice;
    @SerializedName("text_etd")
    @Expose
    private String textEtd;

    public ServiceTextData() {
    }

    public String getTextRangePrice() {
        return textRangePrice;
    }

    public void setTextRangePrice(String textRangePrice) {
        this.textRangePrice = textRangePrice;
    }

    public String getTextEtd() {
        return textEtd;
    }

    public void setTextEtd(String textEtd) {
        this.textEtd = textEtd;
    }
}
