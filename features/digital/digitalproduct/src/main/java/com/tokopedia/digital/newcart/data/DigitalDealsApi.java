package com.tokopedia.digital.newcart.data;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.digital.newcart.data.entity.DealCategoryEntity;
import com.tokopedia.digital.newcart.data.entity.DealProductsResponse;

import java.util.List;
import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

public interface DigitalDealsApi {
    @GET(DigitalDealsUrl.PATH_CATEGORY)
    Observable<Response<DataResponse<List<DealCategoryEntity>>>> getCategories(@QueryMap Map<String, Object> params);

    @GET
    Observable<Response<DataResponse<DealProductsResponse>>> getProducts(@Url String url);
}
