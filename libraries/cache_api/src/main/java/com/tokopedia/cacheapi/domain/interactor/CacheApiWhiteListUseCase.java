package com.tokopedia.cacheapi.domain.interactor;

import com.tokopedia.cacheapi.constant.CacheApiConstant;
import com.tokopedia.cacheapi.domain.CacheApiRepository;
import com.tokopedia.cacheapi.domain.model.CacheApiWhiteListDomain;
import com.tokopedia.cacheapi.util.Injection;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.Collection;
import java.util.List;

import rx.Observable;

/**
 * Created by normansyahputa on 8/16/17.
 */

public class CacheApiWhiteListUseCase extends UseCase<Boolean> {

    private CacheApiRepository cacheApiRepository;

    public CacheApiWhiteListUseCase() {
        cacheApiRepository = Injection.provideCacheApiRepository();
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        List<CacheApiWhiteListDomain> cacheApiWhiteListDomainList = (List<CacheApiWhiteListDomain>) requestParams.getObject(CacheApiConstant.ADD_WHITELIST_COLLECTIONS);
        String versionName = requestParams.getString(CacheApiConstant.APP_VERSION_NAME, "");
        return cacheApiRepository.insertWhiteList(cacheApiWhiteListDomainList, versionName);
    }

    public static RequestParams createParams(List<CacheApiWhiteListDomain> cacheApiWhiteListDomainList, String versionName) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(CacheApiConstant.ADD_WHITELIST_COLLECTIONS, cacheApiWhiteListDomainList);
        requestParams.putObject(CacheApiConstant.APP_VERSION_NAME, versionName);
        return requestParams;
    }
}
