package com.tokopedia.review.feature.inbox.buyerreview.data.repository

import com.tokopedia.review.feature.inbox.buyerreview.domain.model.InboxReputationDomain
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.*
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.report.ReportReviewDomain
import com.tokopedia.usecase.RequestParams
import rx.Observable

/**
 * @author by nisie on 8/14/17.
 */
interface ReputationRepository {
    fun deleteReviewResponse(requestParams: RequestParams): Observable<DeleteReviewResponseDomain>
    fun getInboxReputationFromCloud(requestParams: RequestParams): Observable<InboxReputationDomain>
    fun getInboxReputationFromLocal(requestParams: RequestParams): Observable<InboxReputationDomain>
    fun getReviewFromCloud(requestParams: RequestParams): Observable<ReviewDomain>
    fun sendSmiley(requestParams: RequestParams): Observable<SendSmileyReputationDomain>
    fun reportReview(requestParams: RequestParams): Observable<ReportReviewDomain>
    fun checkIsShopFavorited(requestParams: RequestParams): Observable<CheckShopFavoriteDomain>
    fun insertReviewResponse(requestParams: RequestParams): Observable<SendReplyReviewDomain>
}