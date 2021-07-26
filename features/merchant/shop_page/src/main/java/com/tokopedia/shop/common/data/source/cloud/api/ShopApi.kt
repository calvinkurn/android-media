package com.tokopedia.shop.common.data.source.cloud.api

import com.tokopedia.abstraction.common.data.model.response.PagingList
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.shop.product.data.source.cloud.model.ShopProduct
import retrofit2.http.GET
import com.tokopedia.shop.sort.data.source.cloud.model.ShopProductSortList
import retrofit2.Response
import retrofit2.http.QueryMap
import retrofit2.http.Url
import rx.Observable

/**
 * Created by Hendry on 4/20/2017.
 */
interface ShopApi {
    @GET
    fun getShopProductList(@Url url: String?, @QueryMap params: Map<String?, String?>?): Observable<Response<DataResponse<PagingList<ShopProduct?>?>?>?>?

    @GET
    fun getDynamicFilter(@Url url: String?, @QueryMap params: Map<String?, String?>?): Observable<Response<DataResponse<ShopProductSortList?>?>?>?
}