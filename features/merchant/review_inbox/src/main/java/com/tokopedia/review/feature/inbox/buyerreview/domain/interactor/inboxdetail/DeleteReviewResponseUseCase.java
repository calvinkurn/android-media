package com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inboxdetail;

import com.tokopedia.review.feature.inbox.buyerreview.data.repository.ReputationRepository;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.DeleteReviewResponseDomain;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * @author by nisie on 9/27/17.
 */

public class DeleteReviewResponseUseCase extends UseCase<DeleteReviewResponseDomain> {

    private static final String PARAM_REVIEW_ID = "review_id";
    private static final String PARAM_PRODUCT_ID = "product_id";
    private static final String PARAM_SHOP_ID = "shop_id";
    private static final String PARAM_REPUTATION_ID = "reputation_id";

    private final ReputationRepository reputationRepository;

    public DeleteReviewResponseUseCase(ReputationRepository reputationRepository) {
        super();
        this.reputationRepository = reputationRepository;
    }

    @Override
    public Observable<DeleteReviewResponseDomain> createObservable(RequestParams requestParams) {
        return reputationRepository.deleteReviewResponse(requestParams);
    }

    public static RequestParams getParam(String reviewId, String productId, String shopId, String
            reputationId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PARAM_REVIEW_ID, reviewId);
        requestParams.putString(PARAM_PRODUCT_ID, productId);
        requestParams.putString(PARAM_SHOP_ID, shopId);
        requestParams.putString(PARAM_REPUTATION_ID, reputationId);
        return requestParams;
    }


}
