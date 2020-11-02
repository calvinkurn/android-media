package com.tokopedia.review.feature.inbox.buyerreview.data.repository;

import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.DeleteReviewResponseDomain;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.SendReplyReviewDomain;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.InboxReputationDomain;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.ReviewDomain;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.report.ReportReviewDomain;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.CheckShopFavoriteDomain;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.SendSmileyReputationDomain;
import com.tokopedia.usecase.RequestParams;

import rx.Observable;

/**
 * @author by nisie on 8/14/17.
 */

public interface ReputationRepository {

    Observable<DeleteReviewResponseDomain> deleteReviewResponse(RequestParams requestParams);

    Observable<InboxReputationDomain> getInboxReputationFromCloud(RequestParams requestParams);

    Observable<InboxReputationDomain> getInboxReputationFromLocal(RequestParams requestParams);

    Observable<ReviewDomain> getReviewFromCloud(RequestParams requestParams);

    Observable<SendSmileyReputationDomain> sendSmiley(RequestParams requestParams);

    Observable<ReportReviewDomain> reportReview(RequestParams requestParams);

    Observable<CheckShopFavoriteDomain> checkIsShopFavorited(RequestParams requestParams);

    Observable<SendReplyReviewDomain> insertReviewResponse(RequestParams requestParams);
}
