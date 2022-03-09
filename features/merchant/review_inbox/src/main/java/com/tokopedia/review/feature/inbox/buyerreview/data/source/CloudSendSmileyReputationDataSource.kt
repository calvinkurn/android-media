package com.tokopedia.review.feature.inbox.buyerreview.data.source

import com.tokopedia.network.authentication.AuthHelper
import com.tokopedia.review.common.util.ReviewInboxUtil.convertMapObjectToString
import com.tokopedia.review.feature.inbox.buyerreview.data.mapper.SendSmileyReputationMapper
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.SendSmileyReputationDomain
import com.tokopedia.review.feature.inbox.buyerreview.network.ReputationService
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import rx.Observable

/**
 * @author by nisie on 8/31/17.
 */
class CloudSendSmileyReputationDataSource(
    private val reputationService: ReputationService,
    private val sendSmileyReputationMapper: SendSmileyReputationMapper,
    private val userSession: UserSessionInterface
) {
    fun sendSmiley(requestParams: RequestParams): Observable<SendSmileyReputationDomain> {
        return reputationService.api.sendSmiley(
                AuthHelper.generateParamsNetwork(
                    userSession.userId,
                    userSession.deviceId,
                    convertMapObjectToString(requestParams.parameters)
                )
            ).map(sendSmileyReputationMapper)
    }
}