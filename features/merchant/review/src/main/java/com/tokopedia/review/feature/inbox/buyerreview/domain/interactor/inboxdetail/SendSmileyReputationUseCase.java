package com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inboxdetail;

import com.tokopedia.review.feature.inbox.buyerreview.data.repository.ReputationRepository;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.SendSmileyReputationDomain;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * @author by nisie on 8/31/17.
 */

public class SendSmileyReputationUseCase extends UseCase<SendSmileyReputationDomain> {

    private static final String PARAM_SCORE = "reputation_score";
    private static final String PARAM_REPUTATION_ID = "reputation_id";
    private static final String PARAM_ROLE = "buyer_seller";
    private static final String I_AM_SELLER = "2";
    private static final String I_AM_BUYER = "1";
    private static final int REVIEW_IS_FROM_BUYER = 1;

    private ReputationRepository reputationRepository;

    public SendSmileyReputationUseCase(ReputationRepository reputationRepository) {
        super();
        this.reputationRepository = reputationRepository;
    }

    @Override
    public Observable<SendSmileyReputationDomain> createObservable(RequestParams requestParams) {
        return reputationRepository.sendSmiley(requestParams);
    }

    public static RequestParams getParam(String reputationId, String score, int role) {
        RequestParams params = RequestParams.create();
        params.putString(PARAM_SCORE, score);
        params.putString(PARAM_REPUTATION_ID, reputationId);
        params.putString(PARAM_ROLE, getRole(role));
        return params;
    }

    private static String getRole(int role) {
        return role == REVIEW_IS_FROM_BUYER ? I_AM_SELLER : I_AM_BUYER;
    }
}
