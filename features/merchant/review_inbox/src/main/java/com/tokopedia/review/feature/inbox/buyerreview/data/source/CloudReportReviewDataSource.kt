package com.tokopedia.review.feature.inbox.buyerreview.data.source

import com.tokopedia.authentication.AuthHelper
import com.tokopedia.review.common.util.ReviewInboxUtil.convertMapObjectToString
import com.tokopedia.review.feature.inbox.buyerreview.data.mapper.ReportReviewMapper
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.report.ReportReviewDomain
import com.tokopedia.review.feature.inbox.buyerreview.network.ReputationService
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import rx.Observable

/**
 * @author by nisie on 9/13/17.
 */
class CloudReportReviewDataSource(
    private val reputationService: ReputationService,
    private val reportReviewMapper: ReportReviewMapper,
    private val userSession: UserSessionInterface
) {
    fun reportReview(requestParams: RequestParams): Observable<ReportReviewDomain> {
        return reputationService.api
            .reportReview(
                AuthHelper.generateParamsNetwork(
                    userSession.userId,
                    userSession.deviceId,
                    convertMapObjectToString(requestParams.parameters)
                )
            )
            .map(reportReviewMapper)
    }
}