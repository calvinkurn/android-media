package com.tokopedia.tkpd.home.feed.domain.model;

import android.net.Uri;

/**
 * @author Kulomady on 12/8/16.
 */

public class Paging {
    private String uriNext;
    private String uriPrevious;

    public Paging(String uriNext, String uriPrevious) {
        this.uriNext = uriNext;
        this.uriPrevious = uriPrevious;
    }

    public String getUriNext() {
        return uriNext;
    }

    public String getUriPrevious() {
        return uriPrevious;
    }

    public int getStartIndex() {
        if (uriNext != null && !uriNext.equals(""))
            return Integer.parseInt(getUrlParam("start"));
        else
            return -1;
    }

    private String getUrlParam(String key) {
        Uri uri = Uri.parse(uriNext);
        return uri.getQueryParameter(key);
    }

}
