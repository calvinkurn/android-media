package com.tokopedia.core.cache.domain.model;

import com.tokopedia.core.cache.util.CacheApiUtils;

/**
 * Created by normansyahputa on 8/24/17.
 */

public class CacheApiDataDomain {
    private String host;
    private String path;

    public CacheApiDataDomain(String host, String path) {
        setHost(CacheApiUtils.generateCacheHost(host));
        setPath(CacheApiUtils.generateCachePath(path));
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
