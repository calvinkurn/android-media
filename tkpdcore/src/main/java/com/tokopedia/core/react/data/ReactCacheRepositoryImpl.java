package com.tokopedia.core.react.data;

import com.tokopedia.core.react.data.datasource.cache.TableNotFoundException;
import com.tokopedia.core.react.data.factory.ReactCacheFactory;
import com.tokopedia.core.react.domain.ReactCacheRepository;

import rx.Observable;

/**
 * Created by okasurya on 8/25/17.
 */

public class ReactCacheRepositoryImpl implements ReactCacheRepository {
    ReactCacheFactory cacheFactory;

    public ReactCacheRepositoryImpl(ReactCacheFactory cartFactory) {
        this.cacheFactory = cartFactory;
    }

    @Override
    public Observable<String> getData(String tableName, String id) {
        try {
            return cacheFactory.createCacheDataSource(tableName).getData(id);
        } catch (TableNotFoundException e) {
            return Observable.error(e);
        }
    }

    @Override
    public Observable<String> getDataList(String tableName, int offset, int limit) {
        try {
            return cacheFactory.createCacheDataSource(tableName).getListData(offset, limit);
        } catch (TableNotFoundException e) {
            return Observable.error(e);
        }
    }
}
