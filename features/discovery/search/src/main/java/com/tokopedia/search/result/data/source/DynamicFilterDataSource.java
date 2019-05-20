package com.tokopedia.search.result.data.source;

import com.tokopedia.discovery.common.data.DynamicFilterModel;
import com.tokopedia.search.result.data.mapper.DynamicFilterMapper;
import com.tokopedia.search.result.network.service.DynamicFilterService;

import java.util.Map;

import rx.Observable;

public class DynamicFilterDataSource {

    private final DynamicFilterService attributeApi;
    private final DynamicFilterMapper dynamicFilterMapper;

    public DynamicFilterDataSource(DynamicFilterService attributeApi, DynamicFilterMapper dynamicFilterMapper) {
        this.attributeApi = attributeApi;
        this.dynamicFilterMapper = dynamicFilterMapper;
    }

    public Observable<DynamicFilterModel> getDynamicAttribute(Map<String, Object> param) {
        return attributeApi.getDynamicAttribute(param).map(dynamicFilterMapper);
    }

    public Observable<DynamicFilterModel> getDynamicAttributeV4(Map<String, Object> param) {
        return attributeApi.getDynamicAttributeV4(param).map(dynamicFilterMapper);
    }
}
