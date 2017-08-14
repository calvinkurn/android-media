package com.tokopedia.core.cache.data.repository;

import android.support.annotation.Nullable;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.base.di.qualifier.ApiCacheQualifier;
import com.tokopedia.core.base.di.qualifier.VersionNameQualifier;
import com.tokopedia.core.cache.domain.ApiCacheRepository;
import com.tokopedia.core.cache.domain.model.CacheApiWhiteListDomain;
import com.tokopedia.core.var.TkpdCache;

import java.util.Collection;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by normansyahputa on 8/14/17.
 */

public class ApiCacheRepositoryImpl implements ApiCacheRepository {

    private LocalCacheHandler localCacheHandler;
    private String versionName;

    @Inject
    public ApiCacheRepositoryImpl(@ApiCacheQualifier LocalCacheHandler localCacheHandler, @VersionNameQualifier String versionName) {
        this.localCacheHandler = localCacheHandler;
        this.versionName = versionName;
    }

    @Override
    public Observable<Boolean> checkVersion() {
        if (localCacheHandler.getString(TkpdCache.Key.VERSION_NAME_IN_CACHE) != null && localCacheHandler.getString(TkpdCache.Key.VERSION_NAME_IN_CACHE).equals(versionName)) {
            return Observable.just(false);
        }
        return Observable.just(true);
    }

    @Override
    public Observable<Boolean> bulkInsert(Collection<CacheApiWhiteListDomain> cacheApiDatas) {
        return checkVersion().map(new Func1<Boolean, Boolean>() {
            @Override
            public Boolean call(Boolean aBoolean) {
                if (aBoolean) {

                }
                return aBoolean;
            }
        });
    }

    @Override
    public Observable<Boolean> bulkDelete(@Nullable Collection<CacheApiWhiteListDomain> cacheApiWhiteListDomains) {
        return null;
    }
}
