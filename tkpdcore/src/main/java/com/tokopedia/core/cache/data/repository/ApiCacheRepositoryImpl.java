package com.tokopedia.core.cache.data.repository;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.cache.data.source.ApiCacheDataSource;
import com.tokopedia.core.cache.data.source.db.CacheApiData;
import com.tokopedia.core.cache.data.source.db.CacheApiWhitelist;
import com.tokopedia.core.cache.di.qualifier.ApiCacheQualifier;
import com.tokopedia.core.cache.di.qualifier.VersionNameQualifier;
import com.tokopedia.core.cache.domain.ApiCacheRepository;
import com.tokopedia.core.cache.domain.model.CacheApiDataDomain;
import com.tokopedia.core.cache.domain.model.CacheApiWhiteListDomain;
import com.tokopedia.core.var.TkpdCache;

import java.util.Collection;

import javax.inject.Inject;

import okhttp3.Response;
import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * Created by normansyahputa on 8/14/17.
 */

public class ApiCacheRepositoryImpl implements ApiCacheRepository {

    private LocalCacheHandler localCacheHandler;
    private String versionName;
    private ApiCacheDataSource apiCacheDataSource;

    @Inject
    public ApiCacheRepositoryImpl(@ApiCacheQualifier LocalCacheHandler localCacheHandler, @VersionNameQualifier String versionName, ApiCacheDataSource apiCacheDataSource) {
        this.localCacheHandler = localCacheHandler;
        this.versionName = versionName;
        this.apiCacheDataSource = apiCacheDataSource;
    }

    @Override
    public Observable<Boolean> isNeedToUpdateWhiteList() {
        String storedVersionName = localCacheHandler.getString(TkpdCache.Key.VERSION_NAME_IN_CACHE);
        // Fresh install or different version
        if (TextUtils.isEmpty(storedVersionName) || !storedVersionName.equals(versionName)) {
            // Update version name
            localCacheHandler.putString(TkpdCache.Key.VERSION_NAME_IN_CACHE, versionName);
            localCacheHandler.applyEditor();
            return Observable.just(true);
        } else {
            return Observable.just(false);
        }
    }

    @Override
    public Observable<Boolean> bulkInsert(final Collection<CacheApiWhiteListDomain> cacheApiDatas) {
        return isNeedToUpdateWhiteList().flatMap(new Func1<Boolean, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(Boolean aBoolean) {
                if (!aBoolean) {
                    return Observable.just(false);
                }
                return Observable.zip(apiCacheDataSource.deleteAllCacheData(), apiCacheDataSource.deleteAllWhiteListData(), new Func2<Boolean, Boolean, Boolean>() {
                    @Override
                    public Boolean call(Boolean aBoolean, Boolean aBoolean2) {
                        return true;
                    }
                }).flatMap(new Func1<Boolean, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Boolean aBoolean) {
                        return apiCacheDataSource.insertWhiteList(cacheApiDatas);
                    }
                });
            }
        });
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