package com.tokopedia.cacheapi.domain.model;

import android.text.TextUtils;

import com.tokopedia.cacheapi.util.CacheApiUtils;

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
        String host = CacheApiUtils.getHost(fullPath);
        String path = CacheApiUtils.getPath(fullPath);
        if (!TextUtils.isEmpty(host) && !TextUtils.isEmpty(path)) {
            this.host = host;
            this.path = path;
            this.expireTime = expireTime;
        }
    }

    public CacheApiWhiteListDomain(String host, String path, long expireTime) {
        this.host = CacheApiUtils.getHost(host);
        this.path = CacheApiUtils.generateCachePath(path);
        this.expireTime = expireTime;
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