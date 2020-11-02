package com.tokopedia.tkpd.tkpdreputation.review.product.domain;

import com.tokopedia.tkpd.tkpdreputation.inbox.data.repository.ReputationRepository;
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewstarcount.DataResponseReviewStarCount;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 1/17/18.
 */

public class ReviewProductGetRatingUseCase extends UseCase<DataResponseReviewStarCount> {
    public static final String PRODUCT_ID = "product_id";
    private ReputationRepository reputationRepository;

    @Inject
    public ReviewProductGetRatingUseCase(ReputationRepository reputationRepository) {
        this.reputationRepository = reputationRepository;
    }

    @Override
    public Observable<DataResponseReviewStarCount> createObservable(RequestParams requestParams) {
        return reputationRepository.getReviewStarCount(requestParams.getString(PRODUCT_ID, ""));
    }

    public RequestParams createRequestParams(String productId){
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PRODUCT_ID, productId);
        return requestParams;
    }
}
