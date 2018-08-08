package com.tokopedia.core.util;

import android.net.Uri;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by stevenfredian on 6/17/16.
 */
public class NewPagingHandler {
    /**
     * uri_next : https://www.tokopedia.com/?page=2
     * uri_previous : 0
     */

    private PagingBean paging;

    public PagingBean getPaging() {
        return paging;
    }

    public void setPaging(PagingBean paging) {
        this.paging = paging;
    }

    public static boolean CheckHasNext(PagingBean paging) {
        String uriNext = paging.getUri_next();
        if(uriNext!=null&&!uriNext.equals("0")){
            return true;
        }
        return false;
    }


    public static class PagingBean {

        @SerializedName("uri_next")
        @Expose
        private String uri_next;

        @SerializedName("uri_previous")
        @Expose
        private String uri_previous;
        private Uri uri;

        public String getUri_next() {
            return uri_next;
        }

        public void setUri_next(String uri_next) {
            this.uri_next = uri_next;
        }

        public String getUri_previous() {
            return uri_previous;
        }

        public void setUri_previous(String uri_previous) {
            this.uri_previous = uri_previous;
        }

        public String getNextPage() {
            if(uri_next==null) return "0";
            uri = Uri.parse(uri_next);
            return uri.getQueryParameter("page");
        }

        public String getPrevPage() {
            if(uri_previous==null) return "0";
            uri = Uri.parse(uri_previous);
            return uri.getQueryParameter("page");
        }
    }
}
