package com.tokopedia.review.feature.inbox.buyerreview.data.repository

import com.tokopedia.review.feature.inbox.buyerreview.data.factory.ReputationFactory
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.InboxReputationDomain
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.*
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.report.ReportReviewDomain
import com.tokopedia.usecase.RequestParams
import rx.Observable
import javax.inject.Inject

/**
 * @author by nisie on 8/14/17.
 */
class ReputationRepositoryImpl @Inject constructor(
    private val reputationFactory: ReputationFactory
) : ReputationRepository {

    override fun deleteReviewResponse(requestParams: RequestParams): Observable<DeleteReviewResponseDomain> {
        return reputationFactory
            .createCloudDeleteReviewResponseDataSource()
            .deleteReviewResponse(requestParams)
    }

    override fun getInboxReputationFromCloud(requestParams: RequestParams): Observable<InboxReputationDomain> {
        return reputationFactory
            .createCloudInboxReputationDataSource()
            .getInboxReputation(requestParams)
    }

    override fun getInboxReputationFromLocal(requestParams: RequestParams): Observable<InboxReputationDomain> {
        return reputationFactory
            .createLocalInboxReputationDataSource()
            .getInboxReputationFromCache(requestParams)
    }

    override fun getReviewFromCloud(requestParams: RequestParams): Observable<ReviewDomain> {
        return reputationFactory
            .createCloudInboxReputationDetailDataSource()
            .getInboxReputationDetail(requestParams)
    }

    override fun sendSmiley(requestParams: RequestParams): Observable<SendSmileyReputationDomain> {
        return reputationFactory
            .createCloudSendSmileyReputationDataSource()
            .sendSmiley(requestParams)
    }

    override fun reportReview(requestParams: RequestParams): Observable<ReportReviewDomain> {
        return reputationFactory
            .createCloudReportReviewDataSource()
            .reportReview(requestParams)
    }

    override fun checkIsShopFavorited(requestParams: RequestParams): Observable<CheckShopFavoriteDomain> {
        return reputationFactory
            .createCloudCheckShopFavoriteDataSource()
            .checkShopIsFavorited(requestParams)
    }

    override fun insertReviewResponse(requestParams: RequestParams): Observable<SendReplyReviewDomain> {
        return reputationFactory
            .createCloudReplyReviewDataSource()
            .insertReviewResponse(requestParams)
    }
}