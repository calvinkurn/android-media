package com.tokopedia.shop.review.shop.domain.repository

import com.tokopedia.shop.review.product.data.model.reviewlist.DataResponseReviewShop
import com.tokopedia.shop.review.shop.data.factory.ReputationFactory
import com.tokopedia.shop.review.shop.domain.model.DeleteReviewResponseDomain
import com.tokopedia.shop.review.shop.domain.model.GetLikeDislikeReviewDomain
import com.tokopedia.shop.review.shop.domain.model.LikeDislikeDomain
import com.tokopedia.usecase.RequestParams
import rx.Observable
import java.util.*

/**
 * @author by nisie on 8/14/17.
 */
class ReputationRepositoryImpl(var reputationFactory: ReputationFactory) : ReputationRepository {
    override fun deleteReviewResponse(requestParams: RequestParams?): Observable<DeleteReviewResponseDomain> {
        return reputationFactory
                .createCloudDeleteReviewResponseDataSource()
                .deleteReviewResponse(requestParams!!)
    }

    override fun getLikeDislikeReview(requestParams: RequestParams?): Observable<GetLikeDislikeReviewDomain> {
        return reputationFactory
                .createCloudGetLikeDislikeDataSource()
                .getLikeDislikeReview(requestParams!!)
    }

    override fun likeDislikeReview(requestParams: RequestParams?): Observable<LikeDislikeDomain> {
        return reputationFactory
                .createCloudLikeDislikeDataSource()
                .getLikeDislikeReview(requestParams!!)
    }

    override fun getReviewShopList(params: HashMap<String?, String?>?): Observable<DataResponseReviewShop> {
        return reputationFactory
                .createCloudGetReviewShopList()
                .getReviewShopList(params)
    }

}