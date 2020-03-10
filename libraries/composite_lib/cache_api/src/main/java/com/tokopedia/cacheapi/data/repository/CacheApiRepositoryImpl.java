package com.tokopedia.cacheapi.data.repository;

import com.tokopedia.cacheapi.data.source.CacheApiDataSource;
import com.tokopedia.cacheapi.data.source.db.model.CacheApiWhitelist;
import com.tokopedia.cacheapi.domain.CacheApiRepository;
import com.tokopedia.cacheapi.domain.model.CacheApiWhiteListDomain;

import java.util.Collection;
import java.util.List;

import okhttp3.Response;
import rx.Observable;

/**
 * Created by normansyahputa on 8/14/17.
 */

public class CacheApiRepositoryImpl implements CacheApiRepository {

    private CacheApiDataSource cacheApiDataSource;

    public CacheApiRepositoryImpl(CacheApiDataSource cacheApiDataSource) {
        this.cacheApiDataSource = cacheApiDataSource;
    }

    @Override
    public Observable<Boolean> insertWhiteList(final List<CacheApiWhiteListDomain> domainList, final String versionName) {
        return cacheApiDataSource.bulkInsert(domainList, versionName);
    }

    @Override
    public Observable<Boolean> deleteCachedData(String host, String path) {
        return cacheApiDataSource.deleteCachedData(host, path);
    }

    @Override
    public Observable<Boolean> isInWhiteList(String host, String path) {
        return cacheApiDataSource.isInWhiteList(host, path);
    }

    @Override
    public Observable<Boolean> deleteAllCacheData() {
        return cacheApiDataSource.deleteAllCacheData();
    }

    @Override
    public Observable<Boolean> deleteExpiredCachedData() {
        return cacheApiDataSource.deleteExpiredCachedData();
    }

    @Override
    public Observable<String> getCachedResponse(String host, String path, String requestParam) {
        return cacheApiDataSource.getCachedResponse(host, path, requestParam);
    }

    @Override
    public Observable<Boolean> saveResponse(String host, String path, Response response) {
        return cacheApiDataSource.saveResponse(host, path, response);
    }
}