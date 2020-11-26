package com.tokopedia.shop.common.data.source.cloud.api;

import com.tokopedia.network.data.model.response.DataResponse;
import com.tokopedia.abstraction.common.data.model.response.PagingList;
import com.tokopedia.shop.product.data.source.cloud.model.ShopProduct;
import com.tokopedia.shop.sort.data.source.cloud.model.ShopProductSortList;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by Hendry on 4/20/2017.
 */

public interface ShopApi {
    @GET
    Observable<Response<DataResponse<PagingList<ShopProduct>>>> getShopProductList(@Url String url, @QueryMap Map<String, String> params);

    @GET
    Observable<Response<DataResponse<ShopProductSortList>>> getDynamicFilter(@Url String url, @QueryMap Map<String, String> params);
}
