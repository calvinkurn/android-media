package com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inboxdetail;

import com.tokopedia.review.feature.inbox.buyerreview.data.repository.ReputationRepository;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.SendReplyReviewDomain;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * @author by nisie on 9/28/17.
 */

public class SendReplyReviewUseCase extends UseCase<SendReplyReviewDomain> {

    private static final String PARAM_REVIEW_ID = "review_id";
    private static final String PARAM_PRODUCT_ID = "product_id";
    private static final String PARAM_SHOP_ID = "shop_id";
    private static final String PARAM_REPUTATION_ID = "reputation_id";
    private static final String PARAM_RESPONSE_MESSAGE = "response_message";
    private static final String PARAM_ACTION = "action";
    private static final String ACTION_ = "action";


    protected ReputationRepository reputationRepository;

    public SendReplyReviewUseCase(ReputationRepository reputationRepository) {
        super();
        this.reputationRepository = reputationRepository;
    }

    @Override
    public Observable<SendReplyReviewDomain> createObservable(RequestParams requestParams) {
        return reputationRepository.insertReviewResponse(requestParams);
    }

    public static RequestParams getParam(String reputationId, String productId,
                                         String shopId, String reviewId, String replyReview) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PARAM_REVIEW_ID, reviewId);
        requestParams.putString(PARAM_PRODUCT_ID, productId);
        requestParams.putString(PARAM_SHOP_ID, shopId);
        requestParams.putString(PARAM_REPUTATION_ID, reputationId);
        requestParams.putString(PARAM_RESPONSE_MESSAGE, replyReview);
        return requestParams;
    }
}
