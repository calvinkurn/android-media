package com.tokopedia.review.feature.inbox.buyerreview.data.source

import com.tokopedia.authentication.AuthHelper
import com.tokopedia.review.common.util.ReviewInboxUtil.convertMapObjectToString
import com.tokopedia.review.feature.inbox.buyerreview.data.mapper.DeleteReviewResponseMapper
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.DeleteReviewResponseDomain
import com.tokopedia.review.feature.inbox.buyerreview.network.ReputationService
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import rx.Observable

/**
 * @author by nisie on 9/27/17.
 */
class CloudDeleteReviewResponseDataSource(
    private val reputationService: ReputationService,
    private val deleteReviewResponseMapper: DeleteReviewResponseMapper,
    private val userSession: UserSessionInterface
) {
    fun deleteReviewResponse(requestParams: RequestParams): Observable<DeleteReviewResponseDomain> {
        return reputationService.api
            .deleteReviewResponse(
                AuthHelper.generateParamsNetwork(
                    userSession.userId,
                    userSession.deviceId,
                    convertMapObjectToString(requestParams.parameters)
                )
            )
            .map(deleteReviewResponseMapper)
    }
}