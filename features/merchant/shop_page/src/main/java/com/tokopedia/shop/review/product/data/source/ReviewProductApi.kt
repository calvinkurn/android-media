package com.tokopedia.shop.review.product.data.source

import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.shop.review.constant.ReputationBaseURL
import com.tokopedia.shop.review.product.data.model.reviewlist.DataResponseReviewShop
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap
import rx.Observable

/**
 * Created by zulfikarrahman on 1/15/18.
 */
interface ReviewProductApi {
    @GET(ReputationBaseURL.PATH_GET_REVIEW_SHOP_LIST)
    fun getReviewShopList(@QueryMap params: Map<String?, String?>?): Observable<Response<DataResponse<DataResponseReviewShop>>>
}