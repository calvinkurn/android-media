package com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail;

import com.tokopedia.review.feature.inbox.buyerreview.domain.model.InboxReputationDomain;

/**
 * @author by nisie on 9/26/17.
 */

public class InboxReputationDetailDomain {
    InboxReputationDomain inboxReputationDomain;
    ReviewDomain reviewDomain;

    public InboxReputationDetailDomain() {
    }

    public InboxReputationDomain getInboxReputationDomain() {
        return inboxReputationDomain;
    }

    public ReviewDomain getReviewDomain() {
        return reviewDomain;
    }

    public void setInboxReputationDomain(InboxReputationDomain inboxReputationDomain) {
        this.inboxReputationDomain = inboxReputationDomain;
    }

    public void setReviewDomain(ReviewDomain reviewDomain) {
        this.reviewDomain = reviewDomain;
    }
}
