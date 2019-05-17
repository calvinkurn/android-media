package com.tokopedia.search.result.domain.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GuidedSearchModel {

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
