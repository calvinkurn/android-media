package com.tokopedia.buyerorder.list.data.surveyresponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InsertSurveyResponse {


    @SerializedName("insert_survey_bom")
    @Expose
    private CheckResponseData checkResponseData;


    public CheckResponseData getCheckResponseData() {
        return checkResponseData;
    }
}
