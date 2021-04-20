package com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail;

/**
 * @author by nisie on 9/27/17.
 */

public class DeleteReviewResponseDomain {
    private final boolean isSuccess;

    public DeleteReviewResponseDomain(int isSuccess) {
        this.isSuccess = isSuccess == 1;
    }

    public boolean isSuccess() {
        return isSuccess;
    }
}
