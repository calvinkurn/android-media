package com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.sendreview;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.repository.ReputationRepository;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.sendreview.SkipReviewDomain;

import rx.Observable;


/**
 * @author by nisie on 9/12/17.
 */

public class SkipReviewUseCase extends UseCase<SkipReviewDomain> {

    public static final String PARAM_REPUTATION_ID = "reputation_id";
    public static final String PARAM_SHOP_ID = "shop_id";
    public static final String PARAM_PRODUCT_ID = "product_id";
    public static final String PARAM_USER_ID = "user_id";

    private ReputationRepository reputationRepository;

    public SkipReviewUseCase(ThreadExecutor threadExecutor,
                             PostExecutionThread postExecutionThread,
                             ReputationRepository reputationRepository) {
        super(threadExecutor, postExecutionThread);
        this.reputationRepository = reputationRepository;
    }

    @Override
    public Observable<SkipReviewDomain> createObservable(RequestParams requestParams) {
        return reputationRepository.skipReview(requestParams);
    }

    public static RequestParams getParam(String reputationId, String shopId,
                                         String productId, String userId) {
        RequestParams params = RequestParams.create();
        params.putString(PARAM_REPUTATION_ID, reputationId);
        params.putString(PARAM_SHOP_ID, shopId);
        params.putString(PARAM_PRODUCT_ID, productId);
        params.putString(PARAM_USER_ID, userId);
        return params;
    }
}
