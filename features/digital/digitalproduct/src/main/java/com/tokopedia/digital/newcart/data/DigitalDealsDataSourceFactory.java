package com.tokopedia.digital.newcart.data;

import com.tokopedia.digital.newcart.data.entity.DealCategoryEntity;
import com.tokopedia.digital.newcart.data.entity.DealProductsResponse;

import java.util.List;
import java.util.Map;

import rx.Observable;

public class DigitalDealsDataSourceFactory {
    private DigitalDealsApi digitalDealsApi;

    public DigitalDealsDataSourceFactory(DigitalDealsApi digitalDealsApi) {
        this.digitalDealsApi = digitalDealsApi;
    }

    public Observable<List<DealCategoryEntity>> getCategories(Map<String, Object> params) {
        return digitalDealsApi.getCategories(params).map(dataResponseResponse -> dataResponseResponse.body().getData());
    }

    public Observable<DealProductsResponse> getProducts(String url) {
        return digitalDealsApi.getProducts(url).map(dataResponseResponse -> dataResponseResponse.body().getData());
    }
}
