package com.tokopedia.posapp.react.datasource.cache;

import com.google.gson.Gson;
import com.tokopedia.posapp.cache.data.factory.EtalaseFactory;
import com.tokopedia.posapp.react.datasource.ReactDataSource;
import com.tokopedia.posapp.shop.domain.model.EtalaseDomain;
import com.tokopedia.posapp.react.datasource.model.CacheResult;
import com.tokopedia.posapp.react.datasource.model.ListResult;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by okasurya on 9/25/17.
 */

public class ReactEtalaseCacheSource implements ReactDataSource {
    private Gson gson;
    private EtalaseFactory etalaseFactory;

    @Inject
    public ReactEtalaseCacheSource(EtalaseFactory etalaseFactory, Gson gson) {
        this.gson = gson;
        this.etalaseFactory = etalaseFactory;
    }

    @Override
    public Observable<String> getData(String id) {
        return null;
    }

    @Override
    public Observable<String> getDataList(int offset, int limit) {
        return etalaseFactory.local().getListEtalase(offset, limit).map(getListMapper()).map(toJson());
    }

    @Override
    public Observable<String> getDataAll() {
        return etalaseFactory.local().getAllEtalase().map(getListMapper()).map(toJson());
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
        return Observable.error(new RuntimeException("Method not implemented yet"));
    }

    private Func1<List<EtalaseDomain>, CacheResult> getListMapper() {
        return new Func1<List<EtalaseDomain>, CacheResult>() {
            @Override
            public CacheResult call(List<EtalaseDomain> etalaseDomains) {
                CacheResult<ListResult<EtalaseDomain>> result = new CacheResult<>();
                ListResult<EtalaseDomain> list = new ListResult<>();
                list.setList(etalaseDomains);
                result.setData(list);
                return result;
            }
        };
    }

    private Func1<CacheResult, String> toJson() {
        return new Func1<CacheResult, String>() {
            @Override
            public String call(CacheResult cacheResult) {
                return gson.toJson(cacheResult);
            }
        };
    }
}
