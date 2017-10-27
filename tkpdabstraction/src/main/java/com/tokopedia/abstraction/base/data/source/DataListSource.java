package com.tokopedia.abstraction.base.data.source;

import com.tokopedia.abstraction.base.data.source.cache.DataListCacheSource;
import com.tokopedia.abstraction.base.data.source.cloud.DataListCloudSource;
import com.tokopedia.abstraction.base.data.source.database.DataListDBSource;

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
                        return dataListDBManager.insertAll(ts).flatMap(new Func1<Boolean, Observable<List<U>>>() {
                            @Override
                            public Observable<List<U>> call(Boolean isSuccessInsertData) {
                                if (!isSuccessInsertData) {
                                    return null;
                                } else {
                                    return dataListCacheManager.updateExpiredTime().flatMap(new Func1<Boolean, Observable<List<U>>>() {
                                        @Override
                                        public Observable<List<U>> call(Boolean isSuccessUpdateExpiredTime) {
                                            if (!isSuccessUpdateExpiredTime) {
                                                return null;
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

    public Observable<List<U>> getDataList(final HashMap<String, Object> params){
        return dataListCacheManager.isExpired().flatMap(new Func1<Boolean, Observable<List<U>>>() {
            @Override
            public Observable<List<U>> call(Boolean isCacheExpired) {
                if (isCacheExpired) {
                    return getRefreshedData(params);
                } else {
                    return dataListDBManager.isDataAvailable().flatMap(new Func1<Boolean, Observable<List<U>>>() {
                        @Override
                        public Observable<List<U>> call(Boolean isDataAvailable) {
                            if (!isDataAvailable) {
                                return getRefreshedData(params);
                            } else {
                                return dataListDBManager.getData(params);
                            }
                        }
                    });
                }
            }
        });
    }
}
