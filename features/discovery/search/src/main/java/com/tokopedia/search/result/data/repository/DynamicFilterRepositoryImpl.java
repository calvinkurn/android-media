package com.tokopedia.search.result.data.repository;

import com.tokopedia.discovery.common.data.DynamicFilterModel;
import com.tokopedia.search.result.data.source.DynamicFilterDataSource;
import com.tokopedia.search.result.domain.repository.DynamicFilterRepository;

import java.util.Map;

import rx.Observable;

final class DynamicFilterRepositoryImpl implements DynamicFilterRepository {

    private final DynamicFilterDataSource dynamicFilterDataSource;

    public DynamicFilterRepositoryImpl(DynamicFilterDataSource dynamicFilterDataSource) {
        this.dynamicFilterDataSource = dynamicFilterDataSource;
    }

    @Override
    public Observable<DynamicFilterModel> getDynamicFilter(Map<String, Object> param) {
        return dynamicFilterDataSource.getDynamicAttribute(param);
    }

    @Override
    public Observable<DynamicFilterModel> getDynamicFilterV4(Map<String, Object> param) {
        return dynamicFilterDataSource.getDynamicAttributeV4(param);
    }
}
