package com.tokopedia.tkpd.tkpdreputation.inbox.data.repository;

import com.tokopedia.tkpd.tkpdreputation.domain.model.GetLikeDislikeReviewDomain;
import com.tokopedia.tkpd.tkpdreputation.domain.model.LikeDislikeDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.factory.ReputationFactory;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.DeleteReviewResponseDomain;
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewlist.DataResponseReviewHelpful;
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewlist.DataResponseReviewProduct;
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewstarcount.DataResponseReviewStarCount;
import com.tokopedia.usecase.RequestParams;

import rx.Observable;

/**
 * @author by nisie on 8/14/17.
 */

public class ReputationRepositoryImpl implements ReputationRepository {

    ReputationFactory reputationFactory;

    public ReputationRepositoryImpl(ReputationFactory reputationFactory) {
        this.reputationFactory = reputationFactory;
    }

    @Override
    public Observable<DeleteReviewResponseDomain> deleteReviewResponse(RequestParams requestParams) {
        return reputationFactory
                .createCloudDeleteReviewResponseDataSource()
                .deleteReviewResponse(requestParams);
    }

    @Override
    public Observable<GetLikeDislikeReviewDomain> getLikeDislikeReview(RequestParams requestParams) {
        return reputationFactory
                .createCloudGetLikeDislikeDataSource()
                .getLikeDislikeReview(requestParams);
    }

    @Override
    public Observable<LikeDislikeDomain> likeDislikeReview(RequestParams requestParams) {
        return reputationFactory
                .createCloudLikeDislikeDataSource()
                .getLikeDislikeReview(requestParams);
    }

    @Override
    public Observable<DataResponseReviewProduct> getReviewProductList(String productId,
                                                                      String page,
                                                                      String perPage,
                                                                      String rating,
                                                                      String withAttachment) {
        return reputationFactory
                .createCloudGetReviewProductList()
                .getReviewProductList(productId, page, perPage, rating, withAttachment);
    }

    @Override
    public Observable<DataResponseReviewHelpful> getReviewHelpful(String shopId, String productId) {
        return reputationFactory
                .createCloudGetReviewHelpful()
                .getReviewHelpfulList(shopId, productId);
    }

    @Override
    public Observable<DataResponseReviewStarCount> getReviewStarCount(String productId) {
        return reputationFactory
                .createCloudGetReviewStarCount()
                .getReviewStarCount(productId);
    }
}
