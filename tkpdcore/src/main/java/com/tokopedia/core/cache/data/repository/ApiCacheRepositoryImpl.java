package com.tokopedia.core.cache.data.repository;

import com.tokopedia.core.cache.data.source.ApiCacheDataSource;
import com.tokopedia.core.cache.data.source.db.CacheApiData;
import com.tokopedia.core.cache.data.source.db.CacheApiWhitelist;
import com.tokopedia.core.cache.domain.ApiCacheRepository;
import com.tokopedia.core.cache.domain.model.CacheApiWhiteListDomain;

import java.util.Collection;

import javax.inject.Inject;

import okhttp3.Response;
import rx.Observable;

/**
 * Created by normansyahputa on 8/14/17.
 */

public class ApiCacheRepositoryImpl implements ApiCacheRepository {

    private ApiCacheDataSource apiCacheDataSource;

    @Inject
    public ApiCacheRepositoryImpl(ApiCacheDataSource apiCacheDataSource) {
        this.apiCacheDataSource = apiCacheDataSource;
    }

    @Override
    public Observable<Boolean> insertWhiteList(final Collection<CacheApiWhiteListDomain> cacheApiDatas) {
        return apiCacheDataSource.bulkInsert(cacheApiDatas);
    }

    @Override
    public Observable<Boolean> deleteWhiteList(String host, String path) {
        return apiCacheDataSource.deleteWhiteList(host, path);
    }

    @Override
    public Observable<Boolean> deleteCachedData(String host, String path) {
        return apiCacheDataSource.deleteCachedData(host, path);
    }

    @Override
    public Observable<Boolean> isInWhiteList(String host, String path) {
        return apiCacheDataSource.isInWhiteList(host, path);
    }

    @Override
    public Observable<CacheApiWhitelist> getWhiteList(String host, String path) {
        return apiCacheDataSource.getWhiteList(host, path);
    }

    @Override
    public Observable<Boolean> deleteAllCacheData() {
        return apiCacheDataSource.deleteAllCacheData();
    }

    @Override
    public Observable<Boolean> deleteExpiredCachedData() {
        return apiCacheDataSource.deleteExpiredCachedData();
    }

    @Override
    public Observable<String> getCachedResponse(String host, String path, String requestParam) {
        return apiCacheDataSource.getCachedResponse(host, path, requestParam);
    }

    @Override
    public Observable<Boolean> updateResponse(Response response, int expiredTime) {
        return apiCacheDataSource.updateResponse(response, expiredTime);
    }
}