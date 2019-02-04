package com.tokopedia.logisticdata.data.entity.ratescourierrecommendation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by fajarnuha on 18/12/18.
 */
public class CodData {

    @SerializedName("is_cod")
    @Expose
    private int isCod;
    @SerializedName("cod_text")
    @Expose
    private String codText;

    public CodData() {
    }

    public int getIsCod() {
        return isCod;
    }

    public void setIsCod(int isCod) {
        this.isCod = isCod;
    }

    public String getCodText() {
        return codText;
    }

    public void setCodText(String codText) {
        this.codText = codText;
    }
}
