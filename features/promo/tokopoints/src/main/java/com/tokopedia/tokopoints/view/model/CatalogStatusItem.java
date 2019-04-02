package com.tokopedia.tokopoints.view.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CatalogStatusItem {

    @Expose
    @SerializedName("catalogID")
    private int catalogID;

    @Expose
    @SerializedName("isDisabled")
    private boolean isDisabled;

    @Expose
    @SerializedName("isDisabledButton")
    private boolean isDisabledButton;

    @Expose
    @SerializedName("quota")
    private int quota;

    @Expose
    @SerializedName("upperTextDesc")
    private List<String> upperTextDesc;

    public int getCatalogID() {
        return catalogID;
    }

    public void setCatalogID(int catalogID) {
        this.catalogID = catalogID;
    }

    public boolean isDisabled() {
        return isDisabled;
    }

    public void setDisabled(boolean disabled) {
        isDisabled = disabled;
    }

    public boolean isDisabledButton() {
        return isDisabledButton;
    }

    public void setDisabledButton(boolean disabledButton) {
        isDisabledButton = disabledButton;
    }

    public int getQuota() {
        return quota;
    }

    public void setQuota(int quota) {
        this.quota = quota;
    }

    public List<String> getUpperTextDesc() {
        return upperTextDesc;
    }

    public void setUpperTextDesc(List<String> upperTextDesc) {
        this.upperTextDesc = upperTextDesc;
    }

    @Override
    public String toString() {
        return "CatalogStatusItem{" +
                "catalogID=" + catalogID +
                ", isDisabled=" + isDisabled +
                ", isDisabledButton=" + isDisabledButton +
                ", quota=" + quota +
                ", upperTextDesc=" + upperTextDesc +
                '}';
    }
}
