package com.tokopedia.cacheapi.domain.mapper;

import com.tokopedia.cacheapi.data.source.db.model.CacheApiWhitelist;
import com.tokopedia.cacheapi.domain.model.CacheApiWhiteListDomain;

/**
 * Created by normansyahputa on 8/14/17.
 */

public class CacheApiWhiteListMapper {
    public static CacheApiWhitelist from(CacheApiWhiteListDomain cacheApiWhiteListDomain) {
        CacheApiWhitelist cacheApiWhitelist = new CacheApiWhitelist();
        cacheApiWhitelist.setHost(cacheApiWhiteListDomain.getHost());
        cacheApiWhitelist.setPath(cacheApiWhiteListDomain.getPath());
        cacheApiWhitelist.setExpiredTime(cacheApiWhiteListDomain.getExpireTime());
        return cacheApiWhitelist;
    }
}
