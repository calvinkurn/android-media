package com.tokopedia.core.cache.data.source;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.cache.data.source.cache.CacheApiVersionCache;
import com.tokopedia.core.cache.data.source.db.CacheApiData;
import com.tokopedia.core.cache.data.source.db.CacheApiDataManager;
import com.tokopedia.core.cache.data.source.db.CacheApiWhitelist;
import com.tokopedia.core.cache.domain.model.CacheApiWhiteListDomain;

import java.util.Collection;

import javax.inject.Inject;

import okhttp3.Response;
import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * Created by normansyahputa on 8/9/17.
 */

public class ApiCacheDataSource {

    private CacheApiVersionCache cacheApiVersionCache;
    private CacheApiDataManager cacheApiDataManager;

    @Inject
    public ApiCacheDataSource(CacheApiVersionCache cacheApiVersionCache, CacheApiDataManager cacheApiDataManager) {
        this.cacheApiVersionCache = cacheApiVersionCache;
        this.cacheApiDataManager = cacheApiDataManager;
    }

    public Observable<Boolean> bulkInsert(final Collection<CacheApiWhiteListDomain> cacheApiDatas) {
        return cacheApiVersionCache.isWhiteListVersionUpdated().flatMap(new Func1<Boolean, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(Boolean aBoolean) {
                CommonUtils.dumper(String.format("Need to update white list: %b", aBoolean));
                if (!aBoolean) {
                    return Observable.just(false);
                }
                return Observable.zip(deleteAllCacheData(), cacheApiDataManager.deleteAllWhiteListData(), new Func2<Boolean, Boolean, Boolean>() {
                    @Override
                    public Boolean call(Boolean aBoolean, Boolean aBoolean2) {
                        CommonUtils.dumper(String.format("Delete white list and cache finished"));
                        return true;
                    }
                }).flatMap(new Func1<Boolean, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Boolean aBoolean) {
                        return cacheApiDataManager.insertWhiteList(cacheApiDatas).flatMap(new Func1<Boolean, Observable<Boolean>>() {
                            @Override
                            public Observable<Boolean> call(Boolean aBoolean) {
                                if (!aBoolean) {
                                    return Observable.just(false);
                                }
                                return cacheApiVersionCache.updateCacheWhiteListVersion();
                            }
                        });
                    }
                });
            }
        });
    }

    public Observable<CacheApiWhitelist> getWhiteList(String host, String path) {
        return cacheApiDataManager.getWhiteList(host, path);
    }

    public Observable<Boolean> isInWhiteList(String host, String path) {
        return cacheApiDataManager.isInWhiteList(host, path);
    }

    public Observable<String> getCachedResponse(String host, String path, String param) {
        return cacheApiDataManager.getCachedResponse(host, path, param);
    }

    public Observable<Boolean> deleteAllCacheData() {
        return cacheApiDataManager.deleteAllCacheData();
    }

    public Observable<Boolean> deleteExpiredCachedData() {
        return cacheApiDataManager.deleteExpiredCachedData();
    }

    public Observable<Boolean> deleteCachedData(String host, String path) {
        return cacheApiDataManager.deleteCachedData(host, path);
    }

    public Observable<Boolean> deleteWhiteList(String host, String path) {
        return cacheApiDataManager.deleteWhiteList(host, path);
    }

    public Observable<Boolean> updateResponse(Response response, int expiredTime) {
        return cacheApiDataManager.updateResponse(response, expiredTime);
    }
}