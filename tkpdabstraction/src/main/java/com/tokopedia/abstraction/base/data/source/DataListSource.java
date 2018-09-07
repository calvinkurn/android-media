package com.tokopedia.abstraction.base.data.source;

import com.tokopedia.abstraction.base.data.source.cache.DataCacheSource;
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

public abstract class DataListSource<T, U> extends DataSource<List<T>, List<U>>{

    private DataCacheSource dataListCacheManager;
    private DataListDBSource<T, U> dataListDBManager;

    public DataListSource(DataCacheSource dataListCacheManager, DataListDBSource<T, U> dataListDBManager, DataListCloudSource<T> dataListCloudManager) {
        super(dataListCacheManager, dataListDBManager, dataListCloudManager);
        this.dataListCacheManager = dataListCacheManager;
        this.dataListDBManager = dataListDBManager;
    }

    protected Observable<List<U>> getDataList(final HashMap<String, Object> params) {
        return super.getData(params);
    }

    public Observable<List<U>> getCacheDataList(final HashMap<String, Object> params) {
        return super.getCacheData(params);
    }

    @Override
    protected boolean isCloudDataEmpty(List<T> data){
        return data == null || data.size() == 0;
    }
    @Override
    protected boolean isCacheDataEmpty(List<U> data){
        return data == null || data.size() == 0;
    }

    @Override
    protected Observable<List<U>> getEmptyObservable(){
        return Observable.just((List<U>) new ArrayList<U>());
    }

    protected Observable<Integer> getDataListCount(final HashMap<String, Object> params) {
        return dataListCacheManager.isExpired().flatMap(new Func1<Boolean, Observable<Integer>>() {
            @Override
            public Observable<Integer> call(Boolean isCacheExpired) {
                if (isCacheExpired) {
                    return getRefreshedData(params).flatMap(new Func1<List<U>, Observable<Integer>>() {
                        @Override
                        public Observable<Integer> call(List<U> us) {
                            return Observable.just(us == null ? 0 : us.size());
                        }
                    });
                } else {
                    return getCacheDataListCount(params);
                }
            }
        });
    }

    public Observable<Integer> getCacheDataListCount(final HashMap<String, Object> params) {
        return dataListDBManager.getDataCount(params);
    }


}
