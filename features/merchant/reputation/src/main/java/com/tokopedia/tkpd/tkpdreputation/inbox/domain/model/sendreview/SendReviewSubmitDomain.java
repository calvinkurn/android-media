package com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.sendreview;

/**
 * @author by nisie on 9/5/17.
 */

public class SendReviewSubmitDomain {
    private int reviewId;
    private int isSuccess;

    public SendReviewSubmitDomain(int reviewId, int isSuccess) {
        this.reviewId = reviewId;
        this.isSuccess = isSuccess;
    }

    public int getReviewId() {
        return reviewId;
    }

    public int getIsSuccess() {
        return isSuccess;
    }
}
