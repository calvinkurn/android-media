package com.tokopedia.tkpd.home.feed.domain.model;

/**
 * @author Kulomady on 12/8/16.
 */

public class Image {
    private String mUrl;
    private String sUrl;
    private String xsUrl;

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getsUrl() {
        return sUrl;
    }

    public void setsUrl(String sUrl) {
        this.sUrl = sUrl;
    }

    public String getXsUrl() {
        return xsUrl;
    }

    public void setXsUrl(String xsUrl) {
        this.xsUrl = xsUrl;
    }
}
