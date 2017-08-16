package com.tokopedia.core.cache.domain;

import android.support.annotation.Nullable;

import com.tokopedia.core.cache.domain.model.CacheApiWhiteListDomain;

import java.util.Collection;

import rx.Observable;

/**
 * Created by normansyahputa on 8/14/17.
 */

public interface ApiCacheRepository {

    /**
     * check version of api cache
     *
     * @return true if version is increased, otherwise false
     */
    Observable<Boolean> checkVersion();

    /**
     * bulk insert
     * {"ws.tokopedia.com","/konyol/coba.pl", 10},{"lucu.female.com","towel/doeng.pl", 100}
     *
     * @param cacheApiWhiteListDomains
     * @return
     */
    Observable<Boolean> bulkInsert(@Nullable Collection<CacheApiWhiteListDomain> cacheApiWhiteListDomains);

    /**
     * bulk delete
     * {"ws.tokopedia.com","/konyol/coba.pl", 10},{"lucu.female.com","towel/doeng.pl", 100}
     *
     * @param cacheApiWhiteListDomains
     * @return
     */
    Observable<Boolean> bulkDelete(@Nullable Collection<CacheApiWhiteListDomain> cacheApiWhiteListDomains);
}
