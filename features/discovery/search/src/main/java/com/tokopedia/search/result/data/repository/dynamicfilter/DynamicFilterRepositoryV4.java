package com.tokopedia.search.result.data.repository.dynamicfilter;

import com.tokopedia.discovery.common.data.DynamicFilterModel;
import com.tokopedia.discovery.common.domain.Repository;
import com.tokopedia.search.result.data.source.DynamicFilterDataSource;

import java.util.Map;

import rx.Observable;

final class DynamicFilterRepositoryV4 implements Repository<DynamicFilterModel> {

    private final DynamicFilterDataSource dynamicFilterDataSource;

    public DynamicFilterRepositoryV4(DynamicFilterDataSource dynamicFilterDataSource) {
        this.dynamicFilterDataSource = dynamicFilterDataSource;
    }

    @Override
    public Observable<DynamicFilterModel> query(Map<String, Object> parameters) {
        return dynamicFilterDataSource.getDynamicAttributeV4(parameters);
    }
}
