package com.tokopedia.search.result.data.source.dynamicfilter;

import com.tokopedia.discovery.common.data.DynamicFilterModel;
import com.tokopedia.search.result.network.service.BrowseApi;

import java.util.Map;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

public class DynamicFilterDataSource {

    private final BrowseApi browseApi;
    private final Func1<Response<String>, DynamicFilterModel> dynamicFilterMapper;

    DynamicFilterDataSource(BrowseApi browseApi, Func1<Response<String>, DynamicFilterModel> dynamicFilterMapper) {
        this.browseApi = browseApi;
        this.dynamicFilterMapper = dynamicFilterMapper;
    }

    public Observable<DynamicFilterModel> getDynamicAttribute(Map<String, Object> param) {
        return browseApi.getDynamicAttribute(param).map(dynamicFilterMapper);
    }

    public Observable<DynamicFilterModel> getDynamicAttributeV4(Map<String, Object> param) {
        return browseApi.getDynamicAttributeV4(param).map(dynamicFilterMapper);
    }
}