package com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.inboxdetail;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.repository.ReputationRepository;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.SendSmileyReputationDomain;

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

    public SendSmileyReputationUseCase(ThreadExecutor threadExecutor,
                                       PostExecutionThread postExecutionThread,
                                       ReputationRepository reputationRepository) {
        super(threadExecutor, postExecutionThread);
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
