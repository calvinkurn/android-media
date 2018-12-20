package com.tokopedia.transactiondata.entity.response.cod;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by fajarnuha on 19/12/18.
 */
public class Data {

    @SerializedName("error_message")
    @Expose
    private String errorMessage;
    @SerializedName("info_link")
    @Expose
    private String infoLink;
    @SerializedName("message")
    @Expose
    private Message message;
    @SerializedName("price_summary")
    @Expose
    private List<PriceSummary> priceSummary = null;
    @SerializedName("counter_info")
    @Expose
    private String counterInfo;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getInfoLink() {
        return infoLink;
    }

    public void setInfoLink(String infoLink) {
        this.infoLink = infoLink;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public List<PriceSummary> getPriceSummary() {
        return priceSummary;
    }

    public void setPriceSummary(List<PriceSummary> priceSummary) {
        this.priceSummary = priceSummary;
    }

    public String getCounterInfo() {
        return counterInfo;
    }

    public void setCounterInfo(String counterInfo) {
        this.counterInfo = counterInfo;
    }
}
