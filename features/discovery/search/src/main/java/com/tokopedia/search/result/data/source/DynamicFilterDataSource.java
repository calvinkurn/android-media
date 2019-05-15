package com.tokopedia.search.result.data.source;

import com.tokopedia.discovery.common.data.DynamicFilterModel;
import com.tokopedia.search.result.network.service.DynamicFilterService;

import java.util.Map;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

public class DynamicFilterDataSource {

    private final DynamicFilterService dynamicFilterApi;
    private final Func1<Response<String>, DynamicFilterModel> dynamicFilterMapper;

    DynamicFilterDataSource(DynamicFilterService dynamicFilterApi, Func1<Response<String>, DynamicFilterModel> dynamicFilterMapper) {
        this.dynamicFilterApi = dynamicFilterApi;
        this.dynamicFilterMapper = dynamicFilterMapper;
    }

    public Observable<DynamicFilterModel> getDynamicAttribute(Map<String, Object> param) {
        return dynamicFilterApi.getDynamicAttribute(param).map(dynamicFilterMapper);
    }

    public Observable<DynamicFilterModel> getDynamicAttributeV4(Map<String, Object> param) {
        return dynamicFilterApi.getDynamicAttributeV4(param).map(dynamicFilterMapper);
    }
}