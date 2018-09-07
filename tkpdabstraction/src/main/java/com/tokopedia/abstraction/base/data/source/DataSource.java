package com.tokopedia.abstraction.base.data.source;

import com.tokopedia.abstraction.base.data.source.cache.DataCacheSource;
import com.tokopedia.abstraction.base.data.source.cloud.DataCloudSource;
import com.tokopedia.abstraction.base.data.source.database.DataDBSource;

import java.util.HashMap;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by nathan on 10/23/17.
 */

public abstract class DataSource<T, U> {

    private DataCacheSource dataCacheManager;
    private DataDBSource<T, U> dataDBManager;
    private DataCloudSource<T> dataCloudManager;

    public DataSource(DataCacheSource dataCacheManager, DataDBSource<T, U> dataDBManager, DataCloudSource<T> dataCloudManager) {
        this.dataCacheManager = dataCacheManager;
        this.dataDBManager = dataDBManager;
        this.dataCloudManager = dataCloudManager;
    }

    protected Observable<U> getCloudData(final HashMap<String, Object> params) {
        return dataCloudManager.getData(params).flatMap(new Func1<T, Observable<U>>() {
            @Override
            public Observable<U> call(T ts) {
                if (isCloudDataEmpty(ts)) {
                    return getEmptyObservable();
                }
                return dataDBManager.insertAll(ts).flatMap(new Func1<Boolean, Observable<U>>() {
                    @Override
                    public Observable<U> call(Boolean isSuccessInsertData) {
                        if (!isSuccessInsertData) {
                            return getEmptyObservable();
                        } else {
                            return dataCacheManager.updateExpiredTime().flatMap(new Func1<Boolean, Observable<U>>() {
                                @Override
                                public Observable<U> call(Boolean isSuccessUpdateExpiredTime) {
                                    if (!isSuccessUpdateExpiredTime) {
                                        return null;
                                    } else {
                                        return dataDBManager.getData(params);
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });
    }

    protected Observable<U> getRefreshedData(final HashMap<String, Object> params) {
        return dataDBManager.deleteAll().flatMap(new Func1<Boolean, Observable<U>>() {
            @Override
            public Observable<U> call(Boolean aBoolean) {
                return getCloudData(params);
            }
        });
    }

    protected boolean isCloudDataEmpty(T data) {
        return data == null;
    }

    protected boolean isCacheDataEmpty(U data) {
        return data == null;
    }

    protected Observable<U> getEmptyObservable() {
        return Observable.just(null);
    }

    protected Observable<U> getData(final HashMap<String, Object> params) {
        return dataCacheManager.isExpired().flatMap(new Func1<Boolean, Observable<U>>() {
            @Override
            public Observable<U> call(Boolean isCacheExpired) {
                if (isCacheExpired) {
                    return getRefreshedData(params);
                } else {
                    return getCacheData(params);
                }
            }
        });
    }

    public Observable<U> getCacheData(final HashMap<String, Object> params) {
        return dataDBManager.getData(params).flatMap(new Func1<U, Observable<U>>() {
            @Override
            public Observable<U> call(U cacheList) {
                if (isCacheDataEmpty(cacheList)) {
                    return dataDBManager.isDataAvailable().flatMap(new Func1<Boolean, Observable<U>>() {
                        @Override
                        public Observable<U> call(Boolean isDataAvailable) {
                            if (isDataAvailable) {
                                return getEmptyObservable();
                            } else {
                                return getRefreshedData(params);
                            }
                        }
                    });
                } else {
                    return Observable.just(cacheList);
                }
            }
        });
    }

    public Observable<Boolean> setCacheExpired() {
        return dataCacheManager.setExpired();
    }

    public Observable<Boolean> isCacheExpired() {
        return dataCacheManager.isExpired();
    }

    public Observable<Boolean> deleteCache() {
        return dataDBManager.deleteAll().flatMap(new Func1<Boolean, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(Boolean aBoolean) {
                return setCacheExpired();
            }
        });
    }


}
