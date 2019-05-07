package com.tokopedia.search.result.domain.repository;

import com.tokopedia.discovery.common.data.DynamicFilterModel;

import java.util.Map;

import rx.Observable;

public interface DynamicFilterRepository {

    Observable<DynamicFilterModel> getDynamicFilter(Map<String, Object> requestParams);
    Observable<DynamicFilterModel> getDynamicFilterV4(Map<String, Object> requestParams);
}
