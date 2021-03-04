package com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail;

/**
 * @author by nisie on 8/31/17.
 */

public class SendSmileyReputationDomain {
    private final boolean isSuccess;

    public SendSmileyReputationDomain(int isSuccess) {
        this.isSuccess = isSuccess == 1;
    }

    public boolean isSuccess() {
        return isSuccess;
    }
}
