package com.tokopedia.core.cache.domain.mapper;

import com.tokopedia.core.cache.data.source.db.CacheApiWhitelist;
import com.tokopedia.core.cache.domain.model.CacheApiWhiteListDomain;

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
