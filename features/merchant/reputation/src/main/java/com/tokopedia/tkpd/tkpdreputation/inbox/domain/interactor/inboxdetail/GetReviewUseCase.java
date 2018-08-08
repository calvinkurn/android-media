package com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.inboxdetail;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.repository.ReputationRepository;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.ReviewDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.activity.InboxReputationActivity;

import rx.Observable;

/**
 * @author by nisie on 9/26/17.
 */

public class GetReviewUseCase extends UseCase<ReviewDomain> {

    public static final String PARAM_REPUTATION_ID = "reputation_id";
    public static final String PARAM_USER_ID = "user_id";
    public static final String PARAM_ROLE = "role";
    protected static final int ROLE_BUYER = 1;
    protected static final int ROLE_SELLER = 2;

    protected ReputationRepository reputationRepository;

    public GetReviewUseCase(ThreadExecutor threadExecutor,
                            PostExecutionThread postExecutionThread,
                            ReputationRepository reputationRepository) {
        super(threadExecutor, postExecutionThread);
        this.reputationRepository = reputationRepository;
    }


    @Override
    public Observable<ReviewDomain> createObservable(RequestParams requestParams) {
        return reputationRepository.getReviewFromCloud(requestParams);
    }

    public static RequestParams getParam(String id, String userId, int tab) {
        RequestParams params = RequestParams.create();
        params.putString(PARAM_REPUTATION_ID, id);
        params.putString(PARAM_USER_ID, userId);
        params.putInt(PARAM_ROLE, getRole(tab));
        return params;
    }

    protected static int getRole(int tab) {
        switch (tab) {
            case InboxReputationActivity.TAB_WAITING_REVIEW:
                return ROLE_BUYER;
            case InboxReputationActivity.TAB_MY_REVIEW:
                return ROLE_BUYER;
            case InboxReputationActivity.TAB_BUYER_REVIEW:
                return ROLE_SELLER;
            default:
                return ROLE_BUYER;
        }
    }

}