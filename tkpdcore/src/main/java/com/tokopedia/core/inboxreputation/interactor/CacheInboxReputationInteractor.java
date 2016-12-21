package com.tokopedia.core.inboxreputation.interactor;

import com.tokopedia.core.inboxreputation.model.inboxreputation.InboxReputation;
import com.tokopedia.core.inboxreputation.model.inboxreputationdetail.InboxReputationDetail;
import com.tokopedia.core.inboxreputation.model.param.ActReviewPass;

/**
 * Created by Nisie on 21/01/16.
 */
public interface CacheInboxReputationInteractor {

    void getInboxReputationCache(String nav,GetInboxReputationCacheListener listener);

    void setInboxReputationCache(String nav, InboxReputation inboxReputation);

    void getInboxReputationDetailCache(String reputationId, GetInboxReputationDetailCacheListener listener);

    void setInboxReputationDetailCache(String reputationId , InboxReputationDetail response);

    void getInboxReputationFormCache(GetInboxReputationFormCacheListener listener);

    void setInboxReputationFormCache(String reviewId , ActReviewPass reviewPass);

    void deleteCache();

    interface GetInboxReputationCacheListener {
        void onSuccess(InboxReputation inboxReputation);

        void onError(Throwable e);
    }

    interface GetInboxReputationDetailCacheListener {
        void onSuccess(InboxReputationDetail inboxReputation);

        void onError(Throwable e);
    }

    interface GetInboxReputationFormCacheListener {
        void onSuccess(ActReviewPass review);

        void onError(Throwable e);
    }
}
