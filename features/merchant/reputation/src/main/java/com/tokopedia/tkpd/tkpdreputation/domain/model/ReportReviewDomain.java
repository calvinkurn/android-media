package com.tokopedia.tkpd.tkpdreputation.domain.model;

/**
 * @author by nisie on 9/13/17.
 */

public class ReportReviewDomain {
    private final boolean isSuccess;

    public ReportReviewDomain(int isSuccess) {
        this.isSuccess = isSuccess == 1;
    }

    public boolean isSuccess() {
        return isSuccess;
    }
}
