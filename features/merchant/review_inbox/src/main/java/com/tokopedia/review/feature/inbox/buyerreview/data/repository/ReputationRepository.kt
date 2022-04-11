package com.tokopedia.review.feature.inbox.buyerreview.data.repository

import com.tokopedia.review.feature.inbox.buyerreview.data.factory.ReputationFactory
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.InboxReputationDomain
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.CheckShopFavoriteDomain
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.ReviewDomain
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.SendSmileyReputationDomain
import com.tokopedia.usecase.RequestParams
import rx.Observable
import javax.inject.Inject

/**
 * @author by nisie on 8/14/17.
 */
class ReputationRepository @Inject constructor(
    private val reputationFactory: ReputationFactory
) {

    fun getInboxReputationFromCloud(requestParams: RequestParams): Observable<InboxReputationDomain> {
        return reputationFactory
            .createCloudInboxReputationDataSource()
            .getInboxReputation(requestParams)
    }

    fun getInboxReputationFromLocal(requestParams: RequestParams): Observable<InboxReputationDomain> {
        return reputationFactory
            .createLocalInboxReputationDataSource()
            .getInboxReputationFromCache(requestParams)
    }

    fun getReviewFromCloud(requestParams: RequestParams): Observable<ReviewDomain> {
        return reputationFactory
            .createCloudInboxReputationDetailDataSource()
            .getInboxReputationDetail(requestParams)
    }

    fun sendSmiley(requestParams: RequestParams): Observable<SendSmileyReputationDomain> {
        return reputationFactory
            .createCloudSendSmileyReputationDataSource()
            .sendSmiley(requestParams)
    }

    fun checkIsShopFavorited(requestParams: RequestParams): Observable<CheckShopFavoriteDomain> {
        return reputationFactory
            .createCloudCheckShopFavoriteDataSource()
            .checkShopIsFavorited(requestParams)
    }
}