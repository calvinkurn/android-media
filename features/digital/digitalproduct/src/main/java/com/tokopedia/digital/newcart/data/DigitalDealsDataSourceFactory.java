package com.tokopedia.digital.newcart.data;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.digital.newcart.data.entity.DealCategoryEntity;
import com.tokopedia.digital.newcart.data.entity.DealProductsResponse;

import java.util.List;
import java.util.Map;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

public class DigitalDealsDataSourceFactory {
    private DigitalDealsApi digitalDealsApi;

    public DigitalDealsDataSourceFactory(DigitalDealsApi digitalDealsApi) {
        this.digitalDealsApi = digitalDealsApi;
    }

    public Observable<List<DealCategoryEntity>> getCategories(Map<String, Object> params) {
        return digitalDealsApi.getCategories(params).map(new Func1<Response<DataResponse<List<DealCategoryEntity>>>, List<DealCategoryEntity>>() {
            @Override
            public List<DealCategoryEntity> call(Response<DataResponse<List<DealCategoryEntity>>> dataResponseResponse) {
                return dataResponseResponse.body().getData();
            }
        });
    }

    public Observable<DealProductsResponse> getProducts(String url) {
        return digitalDealsApi.getProducts(url).map(new Func1<Response<DataResponse<DealProductsResponse>>, DealProductsResponse>() {
            @Override
            public DealProductsResponse call(Response<DataResponse<DealProductsResponse>> dataResponseResponse) {
                return dataResponseResponse.body().getData();
            }
        });
    }
}
