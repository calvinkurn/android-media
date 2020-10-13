package com.tokopedia.shop.review.shop.data.source

import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.shop.review.product.data.model.reviewlist.DataResponseReviewShop
import com.tokopedia.shop.review.shop.data.network.ReviewProductService
import com.tokopedia.shop.review.util.GetData
import rx.Observable
import rx.functions.Func1
import java.util.*

/**
 * Created by zulfikarrahman on 1/15/18.
 */
class ReviewShopGetListReviewCloud(private val reviewProductService: ReviewProductService) {
    fun getReviewShopList(params: HashMap<String?, String?>?): Observable<DataResponseReviewShop> {
        return reviewProductService.api!!.getReviewShopList(params).map(GetData())
                .map { dataResponseReviewShopDataResponse -> dataResponseReviewShopDataResponse?.data }
    }

}