package com.tokopedia.core.cache.domain;

import android.support.annotation.Nullable;

import com.tokopedia.core.cache.data.source.db.CacheApiData;
import com.tokopedia.core.cache.data.source.db.CacheApiWhitelist;
import com.tokopedia.core.cache.domain.model.CacheApiDataDomain;
import com.tokopedia.core.cache.domain.model.CacheApiWhiteListDomain;

import java.util.Collection;

import okhttp3.Response;
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
     * @param cacheApiWhiteListDomain
     * @return
     */
    Observable<Boolean> singleDelete(@Nullable CacheApiWhiteListDomain cacheApiWhiteListDomain);

    /**
     * delete data stored at whitelist cache data
     *
     * @param cacheApiDataDomain {@link CacheApiDataDomain} object
     * @return true if delete operation success, false if param is null or data isn't available
     */
    Observable<Boolean> singleDataDelete(@Nullable CacheApiDataDomain cacheApiDataDomain);

    boolean isInWhiteList(String url, String method);

    void deleteAllCache();

    void clearTimeout();

    CacheApiData queryDataFrom(String host, String path, String requestParam);

    void updateResponse(CacheApiData cacheApiData, CacheApiWhitelist cacheApiWhitelist, Response response);

    CacheApiWhitelist isInWhiteListRaw(final String host, final String path);
}
