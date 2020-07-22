package com.tokopedia.buyerorder.list.data.surveyrequest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckBOMSurveyParams {

    @SerializedName("source")
    @Expose
    private String source;


    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
