package com.tokopedia.core.cache.data.repository;

import android.support.annotation.Nullable;

import com.tokopedia.core.cache.data.source.ApiCacheDataSource;
import com.tokopedia.core.cache.data.source.db.CacheApiData;
import com.tokopedia.core.cache.data.source.db.CacheApiWhitelist;
import com.tokopedia.core.cache.domain.ApiCacheRepository;
import com.tokopedia.core.cache.domain.model.CacheApiDataDomain;
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
    public Observable<Boolean> bulkInsert(final Collection<CacheApiWhiteListDomain> cacheApiDatas) {
        return apiCacheDataSource.bulkInsert(cacheApiDatas);
    }

    @Override
    public Observable<Boolean> singleDelete(@Nullable CacheApiWhiteListDomain cacheApiWhiteListDomain) {
        return apiCacheDataSource.deleteWhiteList(cacheApiWhiteListDomain);
    }

    @Override
    public Observable<Boolean> singleDataDelete(@Nullable CacheApiDataDomain cacheApiDataDomain) {
        return apiCacheDataSource.deleteCachedData(cacheApiDataDomain);
    }

    @Override
    public Observable<Boolean> isInWhiteList(String host, String path) {
        return apiCacheDataSource.isInWhiteList(host, path);
    }

    @Override
    public Observable<CacheApiWhitelist> isInWhiteListRaw(String host, String path) {
        return apiCacheDataSource.getWhiteList(host, path);
    }

    @Override
    public Observable<Boolean> deleteAllCache() {
        return apiCacheDataSource.deleteAllCacheData();
    }

    @Override
    public Observable<Boolean> clearTimeout() {
        return apiCacheDataSource.clearTimeout();
    }

    @Override
    public Observable<CacheApiData> queryDataFrom(String host, String path, String requestParam) {
        return apiCacheDataSource.getCachedData(host, path, requestParam);
    }

    @Override
    public Observable<Boolean> updateResponse(CacheApiData cacheApiData, CacheApiWhitelist cacheApiWhitelist, Response response) {
        return apiCacheDataSource.updateResponse(cacheApiData, cacheApiWhitelist, response);
    }
}