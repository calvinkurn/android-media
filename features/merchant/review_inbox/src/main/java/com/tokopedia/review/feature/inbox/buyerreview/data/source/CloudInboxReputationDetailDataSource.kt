package com.tokopedia.review.feature.inbox.buyerreview.data.source

import com.tokopedia.network.authentication.AuthHelper
import com.tokopedia.review.common.util.ReviewInboxUtil.convertMapObjectToString
import com.tokopedia.review.feature.inbox.buyerreview.data.mapper.InboxReputationDetailMapper
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.ReviewDomain
import com.tokopedia.review.feature.inbox.buyerreview.network.ReputationService
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import rx.Observable

/**
 * @author by nisie on 8/19/17.
 */
class CloudInboxReputationDetailDataSource(
    private val reputationService: ReputationService,
    private val inboxReputationDetailMapper: InboxReputationDetailMapper,
    private val userSession: UserSessionInterface
) {
    fun getInboxReputationDetail(requestParams: RequestParams): Observable<ReviewDomain> {
        return reputationService.api.getInboxDetail(
            AuthHelper.generateParamsNetwork(
                userSession.userId,
                userSession.deviceId,
                convertMapObjectToString(requestParams.parameters)
            )
        )
            .map(inboxReputationDetailMapper)
    }
}