package com.tokopedia.posapp.react.datasource;

import com.google.gson.Gson;
import com.tokopedia.posapp.react.datasource.model.CacheResult;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by okasurya on 8/28/17.
 */

public abstract class ReactDataSource {
    protected Gson gson;

    protected ReactDataSource(Gson gson) {
        this.gson = gson;
    }

    protected abstract Observable<String> getData(String id);

    protected abstract Observable<String> getDataList(int offset, int limit);

    protected abstract Observable<String> getDataAll();

    protected abstract Observable<String> deleteAll();

    protected abstract Observable<String> deleteItem(String id);

    protected abstract Observable<String> update(String data);

    protected abstract Observable<String> insert(String data);

    protected Func1<CacheResult, String> mapToJson() {
        return new Func1<CacheResult, String>() {
            @Override
            public String call(CacheResult cacheResult) {
                return gson.toJson(cacheResult);
            }
        };
    }
}
