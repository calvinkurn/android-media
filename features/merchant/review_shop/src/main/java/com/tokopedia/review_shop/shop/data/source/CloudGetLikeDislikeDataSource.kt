package com.tokopedia.review_shop.shop.data.source

import com.tokopedia.review_shop.shop.data.mapper.GetLikeDislikeMapper
import com.tokopedia.review_shop.shop.data.network.ReputationService
import com.tokopedia.review_shop.shop.domain.model.GetLikeDislikeReviewDomain
import com.tokopedia.usecase.RequestParams
import rx.Observable

/**
 * @author by nisie on 9/29/17.
 */
class CloudGetLikeDislikeDataSource(private val reputationService: ReputationService,
                                    private val getLikeDislikeMapper: GetLikeDislikeMapper) {
    fun getLikeDislikeReview(requestParams: RequestParams): Observable<GetLikeDislikeReviewDomain> {
        return reputationService.api!!
                .getLikeDislikeReview(requestParams.parameters)!!
                .map(getLikeDislikeMapper)
    }

}