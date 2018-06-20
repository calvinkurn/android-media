package com.tokopedia.feedplus.domain.model.feed;

/**
 * @author by yfsx on 20/06/18.
 */
public class WhitelistDomain {
    private WhitelistContentDomain content;
    private String error;

    public WhitelistContentDomain getContent() {
        return content;
    }

    public void setContent(WhitelistContentDomain content) {
        this.content = content;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
