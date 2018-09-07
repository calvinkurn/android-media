package com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.sendreview;

/**
 * @author by nisie on 9/12/17.
 */

public class SkipReviewDomain {
    private final boolean isSuccess;

    public SkipReviewDomain(int isSuccess) {
        this.isSuccess = isSuccess == 1;
    }

    public boolean isSuccess() {
        return isSuccess;
    }
}
