package com.tokopedia.review_shop.shop.data.source

import com.tokopedia.authentication.AuthHelper
import com.tokopedia.review_shop.shop.data.mapper.DeleteReviewResponseMapper
import com.tokopedia.review_shop.shop.data.network.ReputationService
import com.tokopedia.review_shop.shop.domain.model.DeleteReviewResponseDomain
import com.tokopedia.review_shop.util.ReputationUtil
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import rx.Observable

/**
 * @author by nisie on 9/27/17.
 */
class CloudDeleteReviewResponseDataSource(private val reputationService: ReputationService,
                                          private val deleteReviewResponseMapper: DeleteReviewResponseMapper,
                                          private val userSession: UserSessionInterface) {
    fun deleteReviewResponse(requestParams: RequestParams): Observable<DeleteReviewResponseDomain> {
        return reputationService.api!!.deleteReviewResponse(AuthHelper.generateParamsNetwork(
                        userSession.userId,
                        userSession.deviceId,
                        ReputationUtil.convertMapObjectToString(requestParams.parameters)).toMap())!!
                .map(deleteReviewResponseMapper)
    }

}