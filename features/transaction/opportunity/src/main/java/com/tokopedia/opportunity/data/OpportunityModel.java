package com.tokopedia.opportunity.data;

import com.tokopedia.core.network.entity.replacement.opportunitydata.OpportunityData;

/**
 * Created by nisie on 3/3/17.
 */

public class OpportunityModel {

    private boolean success;
    private OpportunityData opportunityData;
    private String errorMessage;
    private int errorCode;

    public OpportunityModel() {
        this.success = false;
        this.opportunityData = new OpportunityData();
        this.errorMessage = "";
        this.errorCode = 0;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setOpportunityDataData(OpportunityData opportunityData) {
        this.opportunityData = opportunityData;
    }

    public OpportunityData getOpportunityData() {
        return opportunityData;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public static OpportunityModel createEmptyModel() {
        return new OpportunityModel();
    }
}
