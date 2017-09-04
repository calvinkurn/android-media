package com.tokopedia.core.cache.domain.model;

/**
 * Created by normansyahputa on 8/14/17.
 */

public class CacheApiWhiteListDomain {
    private String host;
    private String path;

    /**
     * this field in minute
     */
    private long expireTime;

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
