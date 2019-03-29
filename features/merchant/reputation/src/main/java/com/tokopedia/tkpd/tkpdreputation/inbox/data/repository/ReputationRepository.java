package com.tokopedia.tkpd.tkpdreputation.inbox.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.tkpd.tkpdreputation.domain.model.GetLikeDislikeReviewDomain;
import com.tokopedia.tkpd.tkpdreputation.domain.model.LikeDislikeDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.SendReplyReviewDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.InboxReputationDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.DeleteReviewResponseDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.FavoriteShopDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.ReviewDomain;
import com.tokopedia.tkpd.tkpdreputation.domain.model.ReportReviewDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.CheckShopFavoriteDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.sendreview.SendReviewSubmitDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.sendreview.SendReviewValidateDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.SendSmileyReputationDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.sendreview.SkipReviewDomain;
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewlist.DataResponseReviewHelpful;
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewlist.DataResponseReviewProduct;
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewlist.DataResponseReviewShop;
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewstarcount.DataResponseReviewStarCount;

import java.util.HashMap;

import rx.Observable;

/**
 * @author by nisie on 8/14/17.
 */

public interface ReputationRepository {

    Observable<InboxReputationDomain> getInboxReputationFromCloud(RequestParams requestParams);

    Observable<InboxReputationDomain> getInboxReputationFromLocal(RequestParams requestParams);

    Observable<ReviewDomain> getReviewFromCloud(RequestParams requestParams);

    Observable<SendSmileyReputationDomain> sendSmiley(RequestParams requestParams);

    Observable<SendReviewValidateDomain> sendReviewValidation(RequestParams requestParams);

    Observable<SendReviewSubmitDomain> sendReviewSubmit(RequestParams requestParams);

    Observable<SkipReviewDomain> skipReview(RequestParams requestParams);

    Observable<SendReviewValidateDomain> editReviewValidation(RequestParams requestParams);

    Observable<SendReviewSubmitDomain> editReviewSubmit(RequestParams requestParams);

    Observable<ReportReviewDomain> reportReview(RequestParams requestParams);

    Observable<CheckShopFavoriteDomain> checkIsShopFavorited(RequestParams requestParams);

    Observable<FavoriteShopDomain> favoriteShop(RequestParams requestParams);

    Observable<DeleteReviewResponseDomain> deleteReviewResponse(RequestParams requestParams);

    Observable<SendReplyReviewDomain> insertReviewResponse(RequestParams requestParams);

    Observable<GetLikeDislikeReviewDomain> getLikeDislikeReview(RequestParams requestParams);

    Observable<LikeDislikeDomain> likeDislikeReview(RequestParams requestParams);

    Observable<DataResponseReviewShop> getReviewShopList(HashMap<String, String> params);

    Observable<DataResponseReviewHelpful> getReviewHelpful(String shopId, String productId);

    Observable<DataResponseReviewStarCount> getReviewStarCount(String productId);

    Observable<DataResponseReviewProduct> getReviewProductList(String productId,
                                                               String page,
                                                               String perPage,
                                                               String rating,
                                                               String withAttachment);
}
