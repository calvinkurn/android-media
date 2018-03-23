package com.tokopedia.posapp.react.datasource;

import com.tokopedia.posapp.react.exception.TableNotFoundException;
import com.tokopedia.posapp.react.factory.ReactDataFactory;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by okasurya on 8/25/17.
 */

public class ReactCacheRepositoryImpl implements ReactCacheRepository {
    ReactDataFactory reactDataFactory;

    @Inject
    public ReactCacheRepositoryImpl(ReactDataFactory reactDataFactory) {
        this.reactDataFactory = reactDataFactory;
    }

    @Override
    public Observable<String> getData(String tableName, String id) {
        try {
            return reactDataFactory.createDataSource(tableName).getData(id);
        } catch (TableNotFoundException e) {
            return Observable.error(e);
        }
    }

    @Override
    public Observable<String> getDataList(String tableName, int offset, int limit) {
        try {
            return reactDataFactory.createDataSource(tableName).getDataList(offset, limit);
        } catch (TableNotFoundException e) {
            return Observable.error(e);
        }
    }

    @Override
    public Observable<String> getDataAll(String tableName) {
        try {
            return reactDataFactory.createDataSource(tableName).getDataAll();
        } catch (TableNotFoundException e) {
            return Observable.error(e);
        }
    }

    @Override
    public Observable<String> deleteAll(String tableName) {
        try {
            return reactDataFactory.createDataSource(tableName).deleteAll();
        } catch (TableNotFoundException e) {
            return Observable.error(e);
        }
    }

    @Override
    public Observable<String> deleteItem(String tableName, String id) {
        try {
            return reactDataFactory.createDataSource(tableName).deleteItem(id);
        } catch (TableNotFoundException e) {
            return Observable.error(e);
        }
    }

    @Override
    public Observable<String> update(String tableName, String data) {
        try {
            return reactDataFactory.createDataSource(tableName).update(data);
        } catch (TableNotFoundException e) {
            return Observable.error(e);
        }
    }

    @Override
    public Observable<String> insert(String tableName, String data) {
        try {
            return reactDataFactory.createDataSource(tableName).insert(data);
        } catch (TableNotFoundException e) {
            return Observable.error(e);
        }
    }
}
