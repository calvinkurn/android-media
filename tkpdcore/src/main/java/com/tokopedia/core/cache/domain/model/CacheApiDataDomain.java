package com.tokopedia.core.cache.domain.model;

/**
 * Created by normansyahputa on 8/24/17.
 */

public class CacheApiDataDomain {
    private String host;
    private String path;
    private String param;

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

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }
}
