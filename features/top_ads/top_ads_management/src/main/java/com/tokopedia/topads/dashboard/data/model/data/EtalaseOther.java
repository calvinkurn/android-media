package com.tokopedia.topads.dashboard.data.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Hendry on 2/23/2017.
 */

public class EtalaseOther {
    @SerializedName("etalase_name")
    @Expose
    private String etalaseName;
    @SerializedName("etalase_url")
    @Expose
    private String etalaseUrl;
    @SerializedName("etalase_id")
    @Expose
    private String etalaseId;

    public String getEtalaseName() {
        return etalaseName;
    }

    public void setEtalaseName(String etalaseName) {
        this.etalaseName = etalaseName;
    }

    public String getEtalaseUrl() {
        return etalaseUrl;
    }

    public void setEtalaseUrl(String etalaseUrl) {
        this.etalaseUrl = etalaseUrl;
    }

    public String getEtalaseId() {
        return etalaseId;
    }

    public void setEtalaseId(String etalaseId) {
        this.etalaseId = etalaseId;
    }

}