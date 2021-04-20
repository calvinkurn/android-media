package com.tokopedia.review.feature.inbox.buyerreview.domain.model.report;

/**
 * @author by nisie on 9/13/17.
 */

public class ReportReviewDomain {
    private boolean isSuccess;
    private String errorMessage;

    public ReportReviewDomain(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public ReportReviewDomain(int isSuccess) {
        this.isSuccess = isSuccess == 1;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
