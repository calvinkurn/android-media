package com.tokopedia.cacheapi.domain.model;

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
    private boolean dynamicUrl;

    public CacheApiWhiteListDomain(String fullPath, long expireTime) {
        this(fullPath, expireTime, false);
    }

    public CacheApiWhiteListDomain(String fullPath, long expireTime, boolean dynamicUrl) {
        this(fullPath, CacheApiUtils.getPath(fullPath), expireTime, dynamicUrl);
    }

    public CacheApiWhiteListDomain(String host, String path, long expireTime) {
        this(host, path, expireTime, false);
    }

    public CacheApiWhiteListDomain(String host, String path, long expireTime, boolean dynamicUrl) {
        this.host = CacheApiUtils.getHost(host);
        this.path = CacheApiUtils.generateCachePath(path);
        this.expireTime = expireTime;
        this.dynamicUrl = dynamicUrl;
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

    public boolean isDynamicUrl() {
        return dynamicUrl;
    }

    public void setDynamicUrl(boolean dynamicUrl) {
        this.dynamicUrl = dynamicUrl;
    }
}