package com.tokopedia.search.result.data.repository.dynamicfilter;

import com.tokopedia.discovery.common.Repository;
import com.tokopedia.discovery.common.data.DynamicFilterModel;
import com.tokopedia.search.result.data.source.dynamicfilter.DynamicFilterDataSource;

import java.util.Map;

import rx.Observable;

final class DynamicFilterRepository implements Repository<DynamicFilterModel> {

    private final DynamicFilterDataSource dynamicFilterDataSource;

    DynamicFilterRepository(DynamicFilterDataSource dynamicFilterDataSource) {
        this.dynamicFilterDataSource = dynamicFilterDataSource;
    }

    @Override
    public Observable<DynamicFilterModel> query(Map<String, Object> parameters) {
        return dynamicFilterDataSource.getDynamicAttribute(parameters);
    }
}
