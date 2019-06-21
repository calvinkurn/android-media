package com.tokopedia.topads.dashboard.domain.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Author errysuprayogi on 25,March,2019
 */
public class MinimumBidDomain {

    @SerializedName("topadsBidInfo")
    private TopadsBidInfo topadsBidInfo;

    public TopadsBidInfo getTopadsBidInfo() {
        return topadsBidInfo;
    }

    public void setTopadsBidInfo(TopadsBidInfo topadsBidInfo) {
        this.topadsBidInfo = topadsBidInfo;
    }

    public static class TopadsBidInfo {

        @SerializedName("request_type")
        private String requestType;
        @SerializedName("data")
        private List<Data> data;

        public String getRequestType() {
            return requestType;
        }

        public void setRequestType(String requestType) {
            this.requestType = requestType;
        }

        public List<Data> getData() {
            return data;
        }

        public void setData(List<Data> data) {
            this.data = data;
        }

        public static class Data {

            @SerializedName("id")
            private int id;
            @SerializedName("suggestion_bid")
            private int suggestionBid;
            @SerializedName("suggestion_bid_fmt")
            private String suggestionBidFmt;
            @SerializedName("min_bid")
            private int minBid;
            @SerializedName("min_bid_fmt")
            private String minBidFmt;
            @SerializedName("max_bid")
            private int maxBid;
            @SerializedName("max_bid_fmt")
            private String maxBidFmt;
            @SerializedName("multiplier")
            private int multiplier;
            @SerializedName("min_daily_budget")
            private int minDailyBudget;
            @SerializedName("min_daily_budget_fmt")
            private String minDailyBudgetFmt;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getSuggestionBid() {
                return suggestionBid;
            }

            public void setSuggestionBid(int suggestionBid) {
                this.suggestionBid = suggestionBid;
            }

            public String getSuggestionBidFmt() {
                return suggestionBidFmt;
            }

            public void setSuggestionBidFmt(String suggestionBidFmt) {
                this.suggestionBidFmt = suggestionBidFmt;
            }

            public int getMinBid() {
                return minBid;
            }

            public void setMinBid(int minBid) {
                this.minBid = minBid;
            }

            public String getMinBidFmt() {
                return minBidFmt;
            }

            public void setMinBidFmt(String minBidFmt) {
                this.minBidFmt = minBidFmt;
            }

            public int getMaxBid() {
                return maxBid;
            }

            public void setMaxBid(int maxBid) {
                this.maxBid = maxBid;
            }

            public String getMaxBidFmt() {
                return maxBidFmt;
            }

            public void setMaxBidFmt(String maxBidFmt) {
                this.maxBidFmt = maxBidFmt;
            }

            public int getMultiplier() {
                return multiplier;
            }

            public void setMultiplier(int multiplier) {
                this.multiplier = multiplier;
            }

            public int getMinDailyBudget() {
                return minDailyBudget;
            }

            public void setMinDailyBudget(int minDailyBudget) {
                this.minDailyBudget = minDailyBudget;
            }

            public String getMinDailyBudgetFmt() {
                return minDailyBudgetFmt;
            }

            public void setMinDailyBudgetFmt(String minDailyBudgetFmt) {
                this.minDailyBudgetFmt = minDailyBudgetFmt;
            }
        }
    }
}
