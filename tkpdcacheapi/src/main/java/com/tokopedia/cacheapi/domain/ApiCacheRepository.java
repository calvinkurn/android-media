package com.tokopedia.cacheapi.domain;

import android.support.annotation.Nullable;

import com.tokopedia.cacheapi.data.source.db.CacheApiData;
import com.tokopedia.cacheapi.data.source.db.CacheApiWhitelist;
import com.tokopedia.cacheapi.domain.model.CacheApiDataDomain;
import com.tokopedia.cacheapi.domain.model.CacheApiWhiteListDomain;

import java.util.Collection;

import okhttp3.Response;
import rx.Observable;

/**
 * Created by normansyahputa on 8/14/17.
 */

public interface ApiCacheRepository {

    Observable<CacheApiWhitelist> getWhiteList(String host, String path);

    Observable<Boolean> isInWhiteList(String host, String path);

    /**
     * bulk insert
     * {"ws.tokopedia.com","/konyol/coba.pl", 10},{"lucu.female.com","towel/doeng.pl", 100}
     *
     * @param cacheApiWhiteListDomains
     * @return
     */
    Observable<Boolean> insertWhiteList(@Nullable Collection<CacheApiWhiteListDomain> cacheApiWhiteListDomains);

    /**
     * bulk delete
     * {"ws.tokopedia.com","/konyol/coba.pl", 10},{"lucu.female.com","towel/doeng.pl", 100}
     *
     * @return
     */
    Observable<Boolean> deleteWhiteList(String host, String path);

    Observable<Boolean> updateResponse(Response response, int expiredTime);

    /**
     * Delete cached data
     *
     * @return true if delete operation success, false if param is null or data isn't available
     */
    Observable<Boolean> deleteCachedData(String host, String path);

    Observable<Boolean> deleteAllCacheData();

    Observable<Boolean> deleteExpiredCachedData();

    Observable<String> getCachedResponse(String host, String path, String requestParam);
}
