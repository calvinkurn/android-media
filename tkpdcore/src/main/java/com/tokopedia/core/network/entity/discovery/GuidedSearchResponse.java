package com.tokopedia.core.network.entity.discovery;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by henrypriyono on 14/02/18.
 */

public class GuidedSearchResponse {
    @SerializedName("data")
    private List<GuidedSearchItem> data;

    public List<GuidedSearchItem> getData() {
        return data;
    }

    public void setData(List<GuidedSearchItem> data) {
        this.data = data;
    }

    public static class GuidedSearchItem {
        @SerializedName("keyword")
        private String keyword;
        @SerializedName("url")
        private String url;

        public String getKeyword() {
            return keyword;
        }

        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
