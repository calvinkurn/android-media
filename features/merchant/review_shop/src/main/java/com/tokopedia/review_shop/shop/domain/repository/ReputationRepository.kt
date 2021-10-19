package com.tokopedia.review_shop.shop.domain.repository

import com.tokopedia.review_shop.product.data.model.reviewlist.DataResponseReviewShop
import com.tokopedia.review_shop.shop.domain.model.DeleteReviewResponseDomain
import com.tokopedia.review_shop.shop.domain.model.GetLikeDislikeReviewDomain
import com.tokopedia.review_shop.shop.domain.model.LikeDislikeDomain
import com.tokopedia.usecase.RequestParams
import rx.Observable
import java.util.*

/**
 * @author by nisie on 8/14/17.
 */
interface ReputationRepository {
    fun deleteReviewResponse(requestParams: RequestParams?): Observable<DeleteReviewResponseDomain>
    fun getLikeDislikeReview(requestParams: RequestParams?): Observable<GetLikeDislikeReviewDomain>
    fun likeDislikeReview(requestParams: RequestParams?): Observable<LikeDislikeDomain>
    fun getReviewShopList(params: HashMap<String?, String?>?): Observable<DataResponseReviewShop>
}