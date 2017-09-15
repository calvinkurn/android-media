package com.tokopedia.posapp.react.datasource;

import com.tokopedia.posapp.react.domain.ReactCacheRepository;
import com.tokopedia.posapp.react.exception.TableNotFoundException;
import com.tokopedia.posapp.react.factory.ReactCacheFactory;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by okasurya on 8/25/17.
 */

public class ReactCacheRepositoryImpl implements ReactCacheRepository {
    ReactCacheFactory reactCacheFactory;

    @Inject
    public ReactCacheRepositoryImpl(ReactCacheFactory reactCacheFactory) {
        this.reactCacheFactory = reactCacheFactory;
    }

    @Override
    public Observable<String> getData(String tableName, String id) {
        try {
            return reactCacheFactory.createCacheDataSource(tableName).getData(id);
        } catch (TableNotFoundException e) {
            return Observable.error(e);
        }
    }

    @Override
    public Observable<String> getDataList(String tableName, int offset, int limit) {
        try {
            return reactCacheFactory.createCacheDataSource(tableName).getListData(offset, limit);
        } catch (TableNotFoundException e) {
            return Observable.error(e);
        }
    }

    @Override
    public Observable<String> getDataAll(String tableName) {
        try {
            return reactCacheFactory.createCacheDataSource(tableName).getAllData();
        } catch (TableNotFoundException e) {
            return Observable.error(e);
        }
    }
}
