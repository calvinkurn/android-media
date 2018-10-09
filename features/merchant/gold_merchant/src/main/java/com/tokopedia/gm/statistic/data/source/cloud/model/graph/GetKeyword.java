package com.tokopedia.gm.statistic.data.source.cloud.model.graph;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by normansyahputa on 7/20/17.
 */

public class GetKeyword {
    @SerializedName("search_keyword")
    @Expose
    private List<SearchKeyword> searchKeyword = new ArrayList<>();

    /**
     * @return The searchKeyword
     */
    public List<SearchKeyword> getSearchKeyword() {
        return searchKeyword;
    }

    /**
     * @param searchKeyword The SearchKeyword
     */
    public void setSearchKeyword(List<SearchKeyword> searchKeyword) {
        this.searchKeyword = searchKeyword;
    }

    public static class SearchKeyword {

        @SerializedName("keyword")
        @Expose
        private String keyword;
        @SerializedName("frequency")
        @Expose
        private int frequency;

        /**
         * @return The keyword
         */
        public String getKeyword() {
            return keyword;
        }

        /**
         * @param keyword The Keyword
         */
        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }

        /**
         * @return The frequency
         */
        public int getFrequency() {
            return frequency;
        }

        /**
         * @param frequency The Frequency
         */
        public void setFrequency(int frequency) {
            this.frequency = frequency;
        }

    }
}
