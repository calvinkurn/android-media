package com.tokopedia.shop.review.shop.data.source

import com.tokopedia.authentication.AuthHelper
import com.tokopedia.shop.review.shop.data.mapper.LikeDislikeMapper
import com.tokopedia.shop.review.shop.data.network.ReputationService
import com.tokopedia.shop.review.shop.domain.model.LikeDislikeDomain
import com.tokopedia.shop.review.util.ReputationUtil
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import rx.Observable

/**
 * @author by nisie on 9/29/17.
 */
class CloudLikeDislikeDataSource(private val reputationService: ReputationService,
                                 private val likeDislikeMapper: LikeDislikeMapper,
                                 private val userSession: UserSessionInterface) {
    fun getLikeDislikeReview(requestParams: RequestParams): Observable<LikeDislikeDomain> {
        return reputationService.api!!
                .likeDislikeReview(AuthHelper.generateParamsNetwork(
                        userSession.userId,
                        userSession.deviceId,
                        ReputationUtil.convertMapObjectToString(requestParams.parameters)
                ).toMap())!!.map(likeDislikeMapper)
    }

}