package com.tokopedia.logisticdata.data.entity.ratescourierrecommendation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by fajarnuha on 18/12/18.
 */
public class CodProductData {

    @SerializedName("is_cod_available")
    @Expose
    private Integer isCodAvailable;
    @SerializedName("cod_text")
    @Expose
    private String codText;
    @SerializedName("cod_price")
    @Expose
    private Integer codPrice;
    @SerializedName("formatted_price")
    @Expose
    private String formattedPrice;
    @SerializedName("tnc_text")
    @Expose
    private String tncText;
    @SerializedName("tnc_link")
    @Expose
    private String tncLink;


    public Integer getIsCodAvailable() {
        return isCodAvailable;
    }

    public void setIsCodAvailable(Integer isCodAvailable) {
        this.isCodAvailable = isCodAvailable;
    }

    public String getCodText() {
        return codText;
    }

    public void setCodText(String codText) {
        this.codText = codText;
    }

    public Integer getCodPrice() {
        return codPrice;
    }

    public void setCodPrice(Integer codPrice) {
        this.codPrice = codPrice;
    }

    public String getFormattedPrice() {
        return formattedPrice;
    }

    public void setFormattedPrice(String formattedPrice) {
        this.formattedPrice = formattedPrice;
    }

    public String getTncText() {
        return tncText;
    }

    public void setTncText(String tncText) {
        this.tncText = tncText;
    }

    public String getTncLink() {
        return tncLink;
    }

    public void setTncLink(String tncLink) {
        this.tncLink = tncLink;
    }
}
