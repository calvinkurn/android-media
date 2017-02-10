package com.tokopedia.tkpd.home.feed.domain.model;

/**
 * @author Kulomady on 12/8/16.
 */

public class Image {
    private String mediumUrl;
    private String smallUrl;
    private String xsUrl;

    public String getUrl() {
        return mediumUrl;
    }

    public void setUrl(String url) {
        mediumUrl = url;
    }

    public String getSmallUrl() {
        return smallUrl;
    }

    public void setSmallUrl(String smallUrl) {
        this.smallUrl = smallUrl;
    }

    public String getXsUrl() {
        return xsUrl;
    }

    public void setXsUrl(String xsUrl) {
        this.xsUrl = xsUrl;
    }
}
