package com.tokopedia.review.feature.inbox.buyerreview.data.source

import com.tokopedia.authentication.AuthHelper
import com.tokopedia.review.common.util.ReviewInboxUtil.convertMapObjectToString
import com.tokopedia.review.feature.inbox.buyerreview.data.mapper.ReplyReviewMapper
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.SendReplyReviewDomain
import com.tokopedia.review.feature.inbox.buyerreview.network.ReputationService
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import rx.Observable

/**
 * @author by nisie on 9/28/17.
 */
class CloudReplyReviewDataSource(
    private val reputationService: ReputationService,
    private val replyReviewMapper: ReplyReviewMapper,
    private val userSession: UserSessionInterface
) {
    fun insertReviewResponse(requestParams: RequestParams): Observable<SendReplyReviewDomain> {
        return reputationService.api
            .insertReviewResponse(
                AuthHelper.generateParamsNetwork(
                    userSession.userId,
                    userSession.deviceId,
                    convertMapObjectToString(requestParams.parameters)
                )
            )
            .map(replyReviewMapper)
    }
}