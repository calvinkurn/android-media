package com.tokopedia.posapp.react.datasource;

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
            return reactCacheFactory.createCacheDataSource(tableName).getDataList(offset, limit);
        } catch (TableNotFoundException e) {
            return Observable.error(e);
        }
    }

    @Override
    public Observable<String> getDataAll(String tableName) {
        try {
            return reactCacheFactory.createCacheDataSource(tableName).getDataAll();
        } catch (TableNotFoundException e) {
            return Observable.error(e);
        }
    }

    @Override
    public Observable<String> deleteAll(String tableName) {
        try {
            return reactCacheFactory.createCacheDataSource(tableName).deleteAll();
        } catch (TableNotFoundException e) {
            return Observable.error(e);
        }
    }

    @Override
    public Observable<String> deleteItem(String tableName, String id) {
        try {
            return reactCacheFactory.createCacheDataSource(tableName).deleteItem(id);
        } catch (TableNotFoundException e) {
            return Observable.error(e);
        }
    }

    @Override
    public Observable<String> update(String tableName, String data) {
        try {
            return reactCacheFactory.createCacheDataSource(tableName).update(data);
        } catch (TableNotFoundException e) {
            return Observable.error(e);
        }
    }

    @Override
    public Observable<String> insert(String tableName, String data) {
        try {
            return reactCacheFactory.createCacheDataSource(tableName).insert(data);
        } catch (TableNotFoundException e) {
            return Observable.error(e);
        }
    }
}
