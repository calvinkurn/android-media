package com.tokopedia.digital.tokocash.model;

/**
 * Created by nabillasabbaha on 8/23/17.
 */

public class ActionHistory {

    private String title;

    private String method;

    private String url;

    private ParamsActionHistory params;

    private String name;

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

    public ParamsActionHistory getParams() {
        return params;
    }

    public void setParams(ParamsActionHistory params) {
        this.params = params;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
