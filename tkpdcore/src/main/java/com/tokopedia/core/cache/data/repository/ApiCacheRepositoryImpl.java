package com.tokopedia.core.cache.data.repository;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.cache.data.source.ApiCacheDataSource;
import com.tokopedia.core.cache.data.source.db.CacheApiData;
import com.tokopedia.core.cache.data.source.db.CacheApiWhitelist;
import com.tokopedia.core.cache.di.qualifier.ApiCacheQualifier;
import com.tokopedia.core.cache.di.qualifier.VersionNameQualifier;
import com.tokopedia.core.cache.domain.ApiCacheRepository;
import com.tokopedia.core.cache.domain.mapper.CacheApiWhiteListMapper;
import com.tokopedia.core.cache.domain.model.CacheApiDataDomain;
import com.tokopedia.core.cache.domain.model.CacheApiWhiteListDomain;
import com.tokopedia.core.var.TkpdCache;

import org.w3c.dom.Text;

import java.util.Collection;

import javax.inject.Inject;

import okhttp3.Response;
import rx.Observable;
import rx.functions.Func1;

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

    protected static void deleteAllCacheData() {
        SQLite.delete(CacheApiData.class).execute();
    }

    @Override
    public Observable<Boolean> checkVersion() {
        if (localCacheHandler.getString(TkpdCache.Key.VERSION_NAME_IN_CACHE) == null) {// fresh install
            // update version name
            localCacheHandler.putString(TkpdCache.Key.VERSION_NAME_IN_CACHE, versionName);
            localCacheHandler.applyEditor();
            return Observable.just(false);
        } else if (localCacheHandler.getString(TkpdCache.Key.VERSION_NAME_IN_CACHE) != null && localCacheHandler.getString(TkpdCache.Key.VERSION_NAME_IN_CACHE).equals(versionName)) {
            // update version name
            localCacheHandler.putString(TkpdCache.Key.VERSION_NAME_IN_CACHE, versionName);
            localCacheHandler.applyEditor();
            return Observable.just(false);
        } else {
            return Observable.just(true);
        }
    }

    @Override
    public Observable<Boolean> bulkInsert(final Collection<CacheApiWhiteListDomain> cacheApiDatas) {
        return checkVersion().flatMap(new Func1<Boolean, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(Boolean aBoolean) {
                deleteAllCache();

                Observable.from(cacheApiDatas)
                        .flatMap(new Func1<CacheApiWhiteListDomain, Observable<CacheApiWhitelist>>() {
                            @Override
                            public Observable<CacheApiWhitelist> call(CacheApiWhiteListDomain cacheApiWhiteListDomain) {
                                CacheApiWhitelist from = CacheApiWhiteListMapper.from(cacheApiWhiteListDomain);
                                from.save();
                                return Observable.just(from);
                            }
                        }).toList().toBlocking().first();
                return Observable.just(aBoolean);
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
        return Observable.just(apiCacheDataSource.isInWhiteList(host, path));
    }

    @Override
    public Observable<CacheApiWhitelist> isInWhiteListRaw(String host, String path) {
        return Observable.just(apiCacheDataSource.getWhiteList(host, path));
    }

    @Override
    public Observable<Boolean> deleteAllCache() {
        deleteAllCacheData();
        return Observable.just(true);
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
        apiCacheDataSource.updateResponse(cacheApiData, cacheApiWhitelist, response);
        return Observable.just(true);
    }



}
