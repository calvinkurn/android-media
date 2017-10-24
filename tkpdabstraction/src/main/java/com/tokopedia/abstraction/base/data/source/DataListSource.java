package com.tokopedia.abstraction.base.data.source;

import com.tokopedia.abstraction.base.data.source.cache.DataListCacheSource;
import com.tokopedia.abstraction.base.data.source.cloud.DataListCloudSource;
import com.tokopedia.abstraction.base.data.source.database.DataListDBSource;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by nathan on 10/23/17.
 */

public abstract class DataListSource<T> {

    private DataListCacheSource dataListCacheManager;
    private DataListDBSource<T> dataListDBManager;
    private DataListCloudSource<T> dataListCloudManager;

    public DataListSource(DataListCacheSource dataListCacheManager, DataListDBSource<T> dataListDBManager, DataListCloudSource<T> dataListCloudManager) {
        this.dataListCacheManager = dataListCacheManager;
        this.dataListDBManager = dataListDBManager;
        this.dataListCloudManager = dataListCloudManager;
    }

    private Observable<Boolean> updateLatestData() {
        return dataListDBManager.isDataAvailable().flatMap(new Func1<Boolean, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(Boolean aBoolean) {
                if (!aBoolean) {
                    return getRefreshDataObservable();
                }
                return dataListCacheManager.isExpired().flatMap(new Func1<Boolean, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Boolean aBoolean) {
                        if (!aBoolean) {
                            return Observable.just(true);
                        }
                        return getRefreshDataObservable();
                    }
                });
            }
        });
    }

    private Observable<Boolean> getRefreshDataObservable() {
        return dataListDBManager.deleteAll().flatMap(new Func1<Boolean, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(Boolean aBoolean) {
                return dataListCloudManager.getData().flatMap(new Func1<List<T>, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(List<T> ts) {
                        return dataListDBManager.insertAll(ts).flatMap(new Func1<Boolean, Observable<Boolean>>() {
                            @Override
                            public Observable<Boolean> call(Boolean aBoolean) {
                                return dataListCacheManager.updateExpiredTime();
                            }
                        });
                    }
                });
            }
        });
    }
}
