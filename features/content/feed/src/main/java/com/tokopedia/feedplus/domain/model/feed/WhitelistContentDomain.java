package com.tokopedia.feedplus.domain.model.feed;

/**
 * @author by yfsx on 20/06/18.
 */
public class WhitelistContentDomain {
    private boolean isWhitelist;
    private String url;

    public boolean isWhitelist() {
        return isWhitelist;
    }

    public void setWhitelist(boolean whitelist) {
        isWhitelist = whitelist;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
