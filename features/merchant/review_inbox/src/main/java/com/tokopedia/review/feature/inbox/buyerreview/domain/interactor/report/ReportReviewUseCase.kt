package com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.report

import android.text.TextUtils
import com.tokopedia.review.feature.inbox.buyerreview.data.repository.ReputationRepository
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.report.ReportReviewDomain
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable

/**
 * @author by nisie on 9/13/17.
 */
class ReportReviewUseCase constructor(private val reputationRepository: ReputationRepository) :
    UseCase<ReportReviewDomain?>() {
    public override fun createObservable(requestParams: RequestParams): Observable<ReportReviewDomain?> {
        return (reputationRepository.reportReview(requestParams))!!
    }

    companion object {
        val REPORT_SPAM: Int = 1
        val REPORT_SARA: Int = 2
        val REPORT_OTHER: Int = 3
        private val PARAM_REVIEW_ID: String = "element_id"
        private val PARAM_SHOP_ID: String = "shop_id"
        private val PARAM_REASON: String = "reason"
        private val PARAM_OTHER_REASON: String = "otherreason"
        fun getParam(
            reviewId: String?,
            shopId: String?,
            reason: Int,
            otherReason: String?
        ): RequestParams {
            val params: RequestParams = RequestParams.create()
            params.putString(PARAM_REVIEW_ID, reviewId)
            params.putString(PARAM_SHOP_ID, shopId)
            params.putInt(PARAM_REASON, reason)
            if (!TextUtils.isEmpty(otherReason)) params.putString(PARAM_OTHER_REASON, otherReason)
            return params
        }
    }
}