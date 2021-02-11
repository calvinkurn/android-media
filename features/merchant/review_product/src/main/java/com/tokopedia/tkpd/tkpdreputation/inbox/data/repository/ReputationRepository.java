package com.tokopedia.tkpd.tkpdreputation.inbox.data.repository;

import com.tokopedia.tkpd.tkpdreputation.domain.model.GetLikeDislikeReviewDomain;
import com.tokopedia.tkpd.tkpdreputation.domain.model.LikeDislikeDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.DeleteReviewResponseDomain;
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewlist.DataResponseReviewHelpful;
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewlist.DataResponseReviewProduct;
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewstarcount.DataResponseReviewStarCount;
import com.tokopedia.usecase.RequestParams;

import rx.Observable;

/**
 * @author by nisie on 8/14/17.
 */

public interface ReputationRepository {

    Observable<DeleteReviewResponseDomain> deleteReviewResponse(RequestParams requestParams);

    Observable<GetLikeDislikeReviewDomain> getLikeDislikeReview(RequestParams requestParams);

    Observable<LikeDislikeDomain> likeDislikeReview(RequestParams requestParams);

    Observable<DataResponseReviewHelpful> getReviewHelpful(String shopId, String productId);

    Observable<DataResponseReviewStarCount> getReviewStarCount(String productId);

    Observable<DataResponseReviewProduct> getReviewProductList(String productId,
                                                               String page,
                                                               String perPage,
                                                               String rating,
                                                               String withAttachment);
}
