package com.tokopedia.shop.sort.data.source.cloud.api

import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.shop.common.constant.ShopUrl
import com.tokopedia.shop.sort.data.source.cloud.model.ShopProductSortList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap
import rx.Observable

/**
 * Created by Hendry on 4/20/2017.
 */
interface ShopAceApi {
    @GET(ShopUrl.SHOP_DYNAMIC_FILTER)
    fun getDynamicFilter(@QueryMap params: Map<String?, String?>?): Observable<Response<DataResponse<ShopProductSortList?>?>?>
}