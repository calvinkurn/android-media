package com.tokopedia.posapp.react.datasource.cache;

import com.google.gson.Gson;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.database.model.SimpleDatabaseModel;
import com.tokopedia.posapp.react.datasource.model.CacheResult;
import com.tokopedia.posapp.react.datasource.model.ReactGlobalCacheData;
import com.tokopedia.posapp.react.datasource.model.StatusResult;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by okasurya on 11/7/17.
 */

public class ReactGlobalCacheSource implements ReactCacheSource {
    private GlobalCacheManager globalCacheManager;
    private Gson gson;

    public ReactGlobalCacheSource(Gson gson) {
        this.globalCacheManager = new GlobalCacheManager();
        globalCacheManager.setCacheDuration(3600000);
        this.gson = gson;
    }

    @Override
    public Observable<String> getData(String id) {
        return Observable.just(id)
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        CacheResult<String> result = new CacheResult<>();
                        result.setData(globalCacheManager.getValueString(s));
                        return gson.toJson(result);
                    }
                })
                .onErrorResumeNext(onErrorResult());
    }

    @Override
    public Observable<String> getDataList(int offset, int limit) {
        return null;
    }

    @Override
    public Observable<String> getDataAll() {
        return null;
    }

    @Override
    public Observable<String> deleteAll() {
        return null;
    }

    @Override
    public Observable<String> deleteItem(String id) {
        return null;
    }

    @Override
    public Observable<String> update(String data) {
        return null;
    }

    @Override
    public Observable<String> insert(String data) {
        return Observable.just(data)
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String data) {
                        ReactGlobalCacheData cacheData = gson.fromJson(data, ReactGlobalCacheData.class);
                        globalCacheManager.setKey(cacheData.getKey());
                        globalCacheManager.setValue(cacheData.getData());
                        globalCacheManager.store();
                        return gson.toJson(getOperationSuccessResult());
                    }
                })
                .onErrorResumeNext(onErrorResult());
    }

    private Func1<Throwable, Observable<? extends  String>> onErrorResult() {
        return new Func1<Throwable, Observable<? extends String>>() {
            @Override
            public Observable<? extends String> call(Throwable throwable) {
                return Observable.just(gson.toJson(getOperationFailedResult(throwable)));
            }
        };
    }

    private CacheResult<StatusResult> getOperationSuccessResult() {
        CacheResult<StatusResult> cacheResult = new CacheResult<>();
        StatusResult statusResult = new StatusResult();
        statusResult.setStatus(true);
        statusResult.setMessage("OK");
        cacheResult.setData(statusResult);
        return cacheResult;
    }

    private CacheResult<StatusResult> getOperationFailedResult(Throwable throwable) {
        CacheResult<StatusResult> cacheResult = new CacheResult<>();
        StatusResult statusResult = new StatusResult();
        statusResult.setStatus(false);
        statusResult.setMessage(throwable.getMessage());
        cacheResult.setData(statusResult);
        return cacheResult;
    }
}
