/*
 * Created By Kulomady on 11/25/16 11:21 PM
 * Copyright (c) 2016. All rights reserved
 *
 * Last Modified 11/25/16 11:21 PM
 */

package com.tokopedia.core.discovery.model.searchSuggestion;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.discovery.model.ObjContainer;

import java.util.List;

/**
 * Created by Tokopedia on 9/1/2016.
 */
public class SearchDataModel {

    /**
     * id : popular_search
     * name : POPULAR SEARCH
     * items : [{"keyword":"tupperware","url":"/search?q=tupperware&source=popular&st=product"},{"keyword":"sepatu pria","url":"/search?q=sepatu+pria&source=popular&st=product"},{"keyword":"sepatu nike","url":"/search?q=sepatu+nike&source=popular&st=product"},{"keyword":"xiaomi","url":"/search?q=xiaomi&source=popular&st=product"},{"keyword":"tas wanita","url":"/search?q=tas+wanita&source=popular&st=product"},{"keyword":"vapor","url":"/search?q=vapor&source=popular&st=product"}]
     */
    @SerializedName("data")
    @Expose
    private List<Data> data;

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public static class Data {
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("name")
        @Expose
        private String name;
        /**
         * keyword : tupperware
         * url : /search?q=tupperware&source=popular&st=product
         */

        private List<Items> items;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Items> getItems() {
            return items;
        }

        public void setItems(List<Items> items) {
            this.items = items;
        }

        public static class Items {
            @SerializedName("keyword")
            @Expose
            private String keyword;
            @SerializedName("url")
            @Expose
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

    /**
     * use this for listener
     */
    public static final class SearchSuggestionContainer implements ObjContainer<SearchDataModel> {

        SearchDataModel searchSuggestionModel;

        public SearchSuggestionContainer(SearchDataModel searchSuggestionModel) {
            this.searchSuggestionModel = searchSuggestionModel;
        }

        @Override
        public SearchDataModel body() {
            return searchSuggestionModel;
        }
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
