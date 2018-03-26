package com.tokopedia.posapp.react.datasource.cloud;

import com.google.gson.Gson;
import com.tokopedia.cacheapi.domain.model.CacheApiDataDomain;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.posapp.cache.data.repository.EtalaseRepository;
import com.tokopedia.posapp.cache.data.repository.EtalaseRepositoryImpl;
import com.tokopedia.posapp.react.datasource.ReactDataSource;
import com.tokopedia.posapp.react.datasource.model.CacheResult;
import com.tokopedia.posapp.react.datasource.model.ListResult;
import com.tokopedia.posapp.shop.domain.model.EtalaseDomain;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author okasurya on 3/26/18.
 */

public class ReactEtalaseCloudSource extends ReactDataSource {
    private EtalaseRepository etalaseRepository;
    private SessionHandler sessionHandler;

    @Inject
    protected ReactEtalaseCloudSource(Gson gson,
                                      EtalaseRepositoryImpl etalaseRepository,
                                      SessionHandler sessionHandler) {
        super(gson);
        this.etalaseRepository = etalaseRepository;
        this.sessionHandler = sessionHandler;
    }

    @Override
    protected Observable<String> getData(String id) {
        return null;
    }

    @Override
    protected Observable<String> getDataList(int offset, int limit) {
        return null;
    }

    @Override
    protected Observable<String> getDataAll() {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString("shop_id", sessionHandler.getShopID());
        return etalaseRepository.getEtalase(requestParams).map(mapEtalaseList()).map(mapToJson());
    }

    @Override
    protected Observable<String> deleteAll() {
        return null;
    }

    @Override
    protected Observable<String> deleteItem(String id) {
        return null;
    }

    @Override
    protected Observable<String> update(String data) {
        return null;
    }

    @Override
    protected Observable<String> insert(String data) {
        return null;
    }

    private Func1<List<EtalaseDomain>, CacheResult> mapEtalaseList() {
        return new Func1<List<EtalaseDomain>, CacheResult>() {
            @Override
            public CacheResult call(List<EtalaseDomain> etalaseDomains) {
                ListResult<EtalaseDomain> list = new ListResult<>();
                list.setList(etalaseDomains);
                CacheResult<ListResult> cacheResult = new CacheResult<>();
                cacheResult.setData(list);
                return cacheResult;
            }
        };
    }
}
