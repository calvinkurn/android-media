package com.tokopedia.shop.product.data.source.cloud.api

import com.tokopedia.network.data.model.response.DataResponse
import retrofit2.http.GET
import com.tokopedia.shop.common.constant.ShopUrl
import com.tokopedia.shop.product.data.source.cloud.model.ShopProductCampaign
import retrofit2.Response
import retrofit2.http.Query
import rx.Observable

/**
 * Created by Hendry on 4/20/2017.
 */
interface ShopOfficialStoreApi {
    @GET(ShopUrl.SHOP_PRODUCT_OS_DISCOUNT)
    fun getProductCampaigns(@Query("pid") ids: String?): Observable<Response<DataResponse<List<ShopProductCampaign?>?>?>?>?
}