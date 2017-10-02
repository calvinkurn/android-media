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
     * delete data stored at white list cache data
     *
     * @return true if delete operation success, false if param is null or data isn't available
     */
    Observable<Boolean> singleDataDelete(String host, String path);

    Observable<Boolean> isInWhiteList(String host, String path);

    Observable<Boolean> deleteAllCache();

    Observable<Boolean> clearTimeout();

    Observable<String> getCachedResponse(String host, String path, String requestParam);

    Observable<Boolean> updateResponse(CacheApiData cacheApiData, CacheApiWhitelist cacheApiWhitelist, Response response);

    Observable<CacheApiWhitelist> getWhiteList(String host, String path);
}
