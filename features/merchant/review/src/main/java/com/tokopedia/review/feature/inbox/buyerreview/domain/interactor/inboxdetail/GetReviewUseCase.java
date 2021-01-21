package com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inboxdetail;

import com.tokopedia.review.feature.inbox.buyerreview.data.repository.ReputationRepository;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.ReviewDomain;
import com.tokopedia.review.feature.inbox.common.ReviewInboxConstants;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

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

    public GetReviewUseCase(ReputationRepository reputationRepository) {
        super();
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
            case ReviewInboxConstants.TAB_WAITING_REVIEW:
                return ROLE_BUYER;
            case ReviewInboxConstants.TAB_MY_REVIEW:
                return ROLE_BUYER;
            case ReviewInboxConstants.TAB_BUYER_REVIEW:
                return ROLE_SELLER;
            default:
                return ROLE_BUYER;
        }
    }

}