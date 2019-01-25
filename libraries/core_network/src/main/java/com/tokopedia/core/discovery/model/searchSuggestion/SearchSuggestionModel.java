
/*
 * Created By Kulomady on 11/25/16 11:21 PM
 * Copyright (c) 2016. All rights reserved
 *
 * Last Modified 11/25/16 11:21 PM
 */

package com.tokopedia.core.discovery.model.searchSuggestion;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.discovery.model.ObjContainer;

public class SearchSuggestionModel {

    @SerializedName("domains")
    @Expose
    private Domains domains;

    /**
     * 
     * @return
     *     The domains
     */
    public Domains getDomains() {
        return domains;
    }

    /**
     * 
     * @param domains
     *     The domains
     */
    public void setDomains(Domains domains) {
        this.domains = domains;
    }

    /**
     * use this for listener
     */
    public static final class SearchSuggestionContainer implements ObjContainer<SearchSuggestionModel> {

        SearchSuggestionModel searchSuggestionModel;

        public SearchSuggestionContainer(SearchSuggestionModel searchSuggestionModel) {
            this.searchSuggestionModel = searchSuggestionModel;
        }

        @Override
        public SearchSuggestionModel body() {
            return searchSuggestionModel;
        }
    }

}
