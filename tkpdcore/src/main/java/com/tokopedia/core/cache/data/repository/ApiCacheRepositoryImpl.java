package com.tokopedia.core.cache.data.repository;

import android.support.annotation.Nullable;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.base.di.qualifier.ApiCacheQualifier;
import com.tokopedia.core.base.di.qualifier.VersionNameQualifier;
import com.tokopedia.core.cache.data.source.cache.CacheHelper;
import com.tokopedia.core.cache.data.source.db.CacheApiData;
import com.tokopedia.core.cache.data.source.db.CacheApiWhitelist;
import com.tokopedia.core.cache.domain.ApiCacheRepository;
import com.tokopedia.core.cache.domain.mapper.CacheApiWhiteListMapper;
import com.tokopedia.core.cache.domain.model.CacheApiWhiteListDomain;
import com.tokopedia.core.var.TkpdCache;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by normansyahputa on 8/14/17.
 */

public class ApiCacheRepositoryImpl implements ApiCacheRepository {

    private static final String TAG = "ApiCacheRepositoryImpl";
    private LocalCacheHandler localCacheHandler;
    private String versionName;
    private CacheHelper cacheHelper;

    @Inject
    public ApiCacheRepositoryImpl(@ApiCacheQualifier LocalCacheHandler localCacheHandler, @VersionNameQualifier String versionName, CacheHelper cacheHelper) {
        this.localCacheHandler = localCacheHandler;
        this.versionName = versionName;
        this.cacheHelper = cacheHelper;
    }

    public static void DeleteAllCache() {
        SQLite.delete(CacheApiData.class).execute();
        SQLite.delete(CacheApiWhitelist.class);
    }

    @Override
    public Observable<Boolean> checkVersion() {
        if (localCacheHandler.getString(TkpdCache.Key.VERSION_NAME_IN_CACHE) == null) {// fresh install
            // update version name
            localCacheHandler.putString(TkpdCache.Key.VERSION_NAME_IN_CACHE, versionName);
            return Observable.just(false);
        } else if (localCacheHandler.getString(TkpdCache.Key.VERSION_NAME_IN_CACHE) != null && localCacheHandler.getString(TkpdCache.Key.VERSION_NAME_IN_CACHE).equals(versionName)) {
            // update version name
            localCacheHandler.putString(TkpdCache.Key.VERSION_NAME_IN_CACHE, versionName);
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
                if (!aBoolean) {// if version is upgdated
                    Observable.from(cacheApiDatas)
                            .flatMap(new Func1<CacheApiWhiteListDomain, Observable<CacheApiWhitelist>>() {
                                @Override
                                public Observable<CacheApiWhitelist> call(CacheApiWhiteListDomain cacheApiWhiteListDomain) {
                                    CacheApiWhitelist from = CacheApiWhiteListMapper.from(cacheApiWhiteListDomain);
                                    from.save();
                                    return Observable.just(from);
                                }
                            }).toList().toBlocking().first();
                }
                return Observable.just(aBoolean);
            }
        });
    }

    @Override
    public Observable<Boolean> bulkDelete(@Nullable final Collection<CacheApiWhiteListDomain> cacheApiDatas) {
        return checkVersion().flatMap(new Func1<Boolean, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(Boolean aBoolean) {
                if (!aBoolean) {// if version is upgdated
                    Observable.from(cacheApiDatas)
                            .flatMap(new Func1<CacheApiWhiteListDomain, Observable<CacheApiWhitelist>>() {
                                @Override
                                public Observable<CacheApiWhitelist> call(CacheApiWhiteListDomain cacheApiWhiteListDomain) {
                                    CacheApiWhitelist cacheApiWhitelist = cacheHelper.queryFromRaw(cacheApiWhiteListDomain.getHost(), cacheApiWhiteListDomain.getPath());
                                    cacheApiWhitelist.delete();

                                    List<CacheApiData> cacheApiDatas1 = cacheHelper.queryDataFrom(cacheApiWhiteListDomain.getHost(), cacheApiWhiteListDomain.getPath());
                                    if (cacheApiDatas1 != null) {
                                        for (CacheApiData cacheApiData : cacheApiDatas1) {
                                            cacheApiData.delete();
                                        }
                                    }

                                    return Observable.just(cacheApiWhitelist);
                                }
                            }).toList().toBlocking().first();
                }
                return Observable.just(aBoolean);
            }
        });
    }

    @Override
    public Observable<Boolean> singleDelete(@Nullable CacheApiWhiteListDomain cacheApiWhiteListDomain) {
        return Observable.just(cacheApiWhiteListDomain).map(new Func1<CacheApiWhiteListDomain, Object>() {
            @Override
            public Object call(CacheApiWhiteListDomain cacheApiWhiteListDomain) {
                CacheApiWhitelist cacheApiWhitelist = cacheHelper.queryFromRaw(cacheApiWhiteListDomain.getHost(), cacheApiWhiteListDomain.getPath());
                cacheApiWhitelist.delete();
                return null;
            }
        }).map(new Func1<Object, Boolean>() {
            @Override
            public Boolean call(Object o) {
                return false;
            }
        });
    }

    @Override
    public void deleteAllCache() {
        SQLite.delete(CacheApiData.class).execute();
        SQLite.delete(CacheApiWhitelist.class);
    }

}
