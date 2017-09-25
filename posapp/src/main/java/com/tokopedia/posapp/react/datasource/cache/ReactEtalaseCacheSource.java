package com.tokopedia.posapp.react.datasource.cache;

import com.google.gson.Gson;
import com.tokopedia.posapp.data.factory.EtalaseFactory;
import com.tokopedia.posapp.domain.model.shop.EtalaseDomain;
import com.tokopedia.posapp.react.datasource.model.CacheResult;
import com.tokopedia.posapp.react.datasource.model.ListResult;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by okasurya on 9/25/17.
 */

public class ReactEtalaseCacheSource implements ReactCacheSource {
    private Gson gson;
    private EtalaseFactory etalaseFactory;
    
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
        return null;
    }

    @Override
    public Observable<String> getDataAll() {
        return etalaseFactory.local().getAllEtalase().map(new Func1<List<EtalaseDomain>, String>() {
            @Override
            public String call(List<EtalaseDomain> etalaseDomains) {
                CacheResult<ListResult<EtalaseDomain>> result = new CacheResult<>();
                ListResult<EtalaseDomain> list = new ListResult<>();
                list.setList(etalaseDomains);
                result.setData(list);
                return gson.toJson(result);
            }
        });
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
}
