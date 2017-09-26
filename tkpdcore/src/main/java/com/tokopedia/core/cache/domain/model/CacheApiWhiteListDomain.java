package com.tokopedia.core.cache.domain.model;

import android.text.TextUtils;

import com.tokopedia.core.cache.util.CacheApiUtils;

import java.net.URL;

/**
 * Created by normansyahputa on 8/14/17.
 */

public class CacheApiWhiteListDomain {
    private String host;
    private String path;

    /**
     * this field in seconds
     */
    private long expireTime;

    public CacheApiWhiteListDomain(String fullPath, long expireTime) {
        String host = CacheApiUtils.getDomain(fullPath);
        String path = CacheApiUtils.getPath(fullPath);
        if (!TextUtils.isEmpty(host) && !TextUtils.isEmpty(path)) {
            setHost(CacheApiUtils.generateCacheHost(host));
            setPath(CacheApiUtils.generateCachePath(path));
            setExpireTime(expireTime);
        }
    }

    public CacheApiWhiteListDomain(String host, String path, long expireTime) {
        setHost(CacheApiUtils.generateCacheHost(host));
        setPath(CacheApiUtils.generateCachePath(path));
        setExpireTime(expireTime);
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

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }
}
