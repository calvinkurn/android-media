package com.tokopedia.buyerorder.detail.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AdditionalTickerInfo {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("notes")
    @Expose
    private String notes;
    @SerializedName("urlDetail")
    @Expose
    private String urlDetail;
    @SerializedName("urlText")
    @Expose
    private String urlText;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getUrlDetail() {
        return urlDetail;
    }

    public void setUrlDetail(String urlDetail) {
        this.urlDetail = urlDetail;
    }

    public String getUrlText() {
        return urlText;
    }

    public void setUrlText(String urlText) {
        this.urlText = urlText;
    }

    @Override
    public String toString() {
        return "AdditionalTickerInfo{" +
                "title='" + title + '\'' +
                ", notes='" + notes + '\'' +
                ", urlDetail='" + urlDetail + '\'' +
                ", urlText='" + urlText + '\'' +
                '}';
    }
}
