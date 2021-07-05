package com.tokopedia.review.feature.inbox.buyerreview.data.repository;

import com.tokopedia.review.feature.inbox.buyerreview.data.factory.ReputationFactory;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.InboxReputationDomain;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.CheckShopFavoriteDomain;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.DeleteReviewResponseDomain;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.ReviewDomain;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.SendReplyReviewDomain;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.SendSmileyReputationDomain;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.report.ReportReviewDomain;
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
    public Observable<InboxReputationDomain> getInboxReputationFromCloud(RequestParams requestParams) {
        return reputationFactory
                .createCloudInboxReputationDataSource()
                .getInboxReputation(requestParams);
    }

    @Override
    public Observable<InboxReputationDomain> getInboxReputationFromLocal(RequestParams requestParams) {
        return reputationFactory
                .createLocalInboxReputationDataSource()
                .getInboxReputationFromCache(requestParams);
    }

    @Override
    public Observable<ReviewDomain> getReviewFromCloud(RequestParams requestParams) {
        return reputationFactory
                .createCloudInboxReputationDetailDataSource()
                .getInboxReputationDetail(requestParams);
    }

    @Override
    public Observable<SendSmileyReputationDomain> sendSmiley(RequestParams requestParams) {
        return reputationFactory
                .createCloudSendSmileyReputationDataSource()
                .sendSmiley(requestParams);
    }

    @Override
    public Observable<ReportReviewDomain> reportReview(RequestParams requestParams) {
        return reputationFactory
                .createCloudReportReviewDataSource()
                .reportReview(requestParams);
    }

    @Override
    public Observable<CheckShopFavoriteDomain> checkIsShopFavorited(RequestParams requestParams) {
        return reputationFactory
                .createCloudCheckShopFavoriteDataSource()
                .checkShopIsFavorited(requestParams);
    }

    @Override
    public Observable<SendReplyReviewDomain> insertReviewResponse(RequestParams requestParams) {
        return reputationFactory
                .createCloudReplyReviewDataSource()
                .insertReviewResponse(requestParams);
    }
}
