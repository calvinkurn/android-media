package com.tokopedia.opportunity.data;

import com.tokopedia.core.network.entity.replacement.opportunitycategorydata.OpportunityCategoryData;

/**
 * Created by nisie on 3/6/17.
 */

public class OpportunityFilterModel {
    private boolean success;
    private OpportunityCategoryData opportunityCategoryData;
    private String errorMessage;
    private int errorCode;

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setOpportunityCategoryData(OpportunityCategoryData opportunityCategoryData) {
        this.opportunityCategoryData = opportunityCategoryData;
    }

    public OpportunityCategoryData getOpportunityCategoryData() {
        return opportunityCategoryData;
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
}
