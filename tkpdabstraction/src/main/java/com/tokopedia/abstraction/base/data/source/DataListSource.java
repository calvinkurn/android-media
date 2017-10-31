package com.tokopedia.abstraction.base.data.source;

import com.tokopedia.abstraction.base.data.source.cache.DataListCacheSource;
import com.tokopedia.abstraction.base.data.source.cloud.DataListCloudSource;
import com.tokopedia.abstraction.base.data.source.database.DataListDBSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by nathan on 10/23/17.
 */

public abstract class DataListSource<T,U> {

    private DataListCacheSource dataListCacheManager;
    private DataListDBSource<T,U> dataListDBManager;
    private DataListCloudSource<T> dataListCloudManager;

    public DataListSource(DataListCacheSource dataListCacheManager, DataListDBSource<T,U> dataListDBManager, DataListCloudSource<T> dataListCloudManager) {
        this.dataListCacheManager = dataListCacheManager;
        this.dataListDBManager = dataListDBManager;
        this.dataListCloudManager = dataListCloudManager;
    }

    private Observable<List<U>> getRefreshedData(final HashMap<String, Object> params) {
        return dataListDBManager.deleteAll().flatMap(new Func1<Boolean, Observable<List<U>>>() {
            @Override
            public Observable<List<U>> call(Boolean aBoolean) {
                return dataListCloudManager.getData(params).flatMap(new Func1<List<T>, Observable<List<U>>>() {
                    @Override
                    public Observable<List<U>> call(List<T> ts) {
                        if (ts == null || ts.size() == 0) {
                            return Observable.just((List<U>)new ArrayList<U>());
                        }
                        return dataListDBManager.insertAll(ts).flatMap(new Func1<Boolean, Observable<List<U>>>() {
                            @Override
                            public Observable<List<U>> call(Boolean isSuccessInsertData) {
                                if (!isSuccessInsertData) {
                                    return Observable.just((List<U>)new ArrayList<U>());
                                } else {
                                    return dataListCacheManager.updateExpiredTime().flatMap(new Func1<Boolean, Observable<List<U>>>() {
                                        @Override
                                        public Observable<List<U>> call(Boolean isSuccessUpdateExpiredTime) {
                                            if (!isSuccessUpdateExpiredTime) {
                                                return Observable.just((List<U>)new ArrayList<U>());
                                            } else {
                                                return dataListDBManager.getData(params);
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                });
            }
        });
    }

    protected Observable<List<U>> getDataList(final HashMap<String, Object> params){
        return dataListCacheManager.isExpired().flatMap(new Func1<Boolean, Observable<List<U>>>() {
            @Override
            public Observable<List<U>> call(Boolean isCacheExpired) {
                if (isCacheExpired) {
                    return getRefreshedData(params);
                } else {
                    return dataListDBManager.getData(params).flatMap(new Func1<List<U>, Observable<List<U>>>() {
                        @Override
                        public Observable<List<U>> call(List<U> cacheList) {
                            if (cacheList == null || cacheList.size() == 0) {
                                return dataListDBManager.isDataAvailable().flatMap(new Func1<Boolean, Observable<List<U>>>() {
                                    @Override
                                    public Observable<List<U>> call(Boolean isDataAvalable) {
                                        if (isDataAvalable) {
                                            return Observable.just((List<U>)new ArrayList<U>());
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
            }
        });
    }
}
