package com.tokopedia.logisticdata.data.entity.ratescourierrecommendation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Irfan Khoirul on 02/08/18.
 */

public class ProductTextData {

    @SerializedName("text_etd")
    @Expose
    private String textEtd;

    @SerializedName("text_price")
    @Expose
    private String textPrice;

    public ProductTextData() {
    }

    public String getTextEtd() {
        return textEtd;
    }

    public void setTextEtd(String textEtd) {
        this.textEtd = textEtd;
    }

    public String getTextPrice() {
        return textPrice;
    }

    public void setTextPrice(String textPrice) {
        this.textPrice = textPrice;
    }
}
