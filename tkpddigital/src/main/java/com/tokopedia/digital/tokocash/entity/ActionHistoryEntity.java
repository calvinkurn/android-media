package com.tokopedia.digital.tokocash.entity;

import android.os.Parcel;

/**
 * Created by nabillasabbaha on 8/23/17.
 */

public class ActionHistoryEntity {

    private String title;

    private String method;

    private String url;

    private ParamsActionHistoryEntity params;

    private String name;

    private String type;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ParamsActionHistoryEntity getParams() {
        return params;
    }

    public void setParams(ParamsActionHistoryEntity params) {
        this.params = params;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
