package com.tokopedia.topads.sdk.base;

import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.topads.sdk.view.DisplayMode;

/**
 * Created by errysuprayogi on 3/27/17.
 */

public class Config {

    public static final String TOPADS_URL = "https://ta.tokopedia.com/promo/";
    public static final int ERROR_CODE_INVALID_RESPONSE = 911;
    public static final String DEFAULT_DEVICE = "android";
    public static final String DEFAULT_CLIENT_ID = "12";

    private String baseUrl;
    private String userId;
    private String sessionId;
    private String device;
    private String clientId;
    private Endpoint endpoint;
    private boolean withPreferedCategory;
    private boolean withMerlinCategory;
    private TopAdsParams topAdsParams;
    private DisplayMode displayMode;

    public Config(Builder builder) {
        this.baseUrl = builder.baseUrl;
        this.userId = builder.userId;
        this.sessionId = builder.sessionId;
        this.device = builder.device;
        this.clientId = builder.clientId;
        this.withPreferedCategory = builder.withPreferedCategory;
        this.withMerlinCategory = builder.withMerlinCategory;
        this.endpoint = builder.endpoint;
        this.topAdsParams = builder.topAdsParams;
        this.displayMode = builder.displayMode;
    }

    public void setWithPreferedCategory(boolean withPreferedCategory) {
        this.withPreferedCategory = withPreferedCategory;
    }

    public void setWithMerlinCategory(boolean withMerlinCategory) {
        this.withMerlinCategory = withMerlinCategory;
    }

    public boolean isWithPreferedCategory() {
        return withPreferedCategory;
    }

    public boolean isWithMerlinCategory() {
        return withMerlinCategory;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getUserId() {
        return userId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getDevice() {
        return device;
    }

    public Endpoint getEndpoint() {
        return endpoint;
    }

    public String getClientId() {
        return clientId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setEndpoint(Endpoint endpoint) {
        this.endpoint = endpoint;
    }

    public void setTopAdsParams(TopAdsParams topAdsParams) {
        this.topAdsParams = topAdsParams;
    }

    public DisplayMode getDisplayMode() {
        return displayMode;
    }

    public void setDisplayMode(DisplayMode displayMode) {
        this.displayMode = displayMode;
    }

    public TopAdsParams getTopAdsParams() {
        return topAdsParams;
    }

    public static class Builder {

        private String baseUrl;
        private String userId;
        private String sessionId;
        private String device;
        private String clientId;
        private Endpoint endpoint;
        private boolean withPreferedCategory;
        private boolean withMerlinCategory;
        private DisplayMode displayMode;
        private TopAdsParams topAdsParams = new TopAdsParams();

        public Builder() {
            baseUrl = TOPADS_URL;
            device = DEFAULT_DEVICE;
            clientId = DEFAULT_CLIENT_ID;
        }

        public Builder setUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder setSessionId(String sessionId) {
            this.sessionId = sessionId;
            return this;
        }

        public Builder setEndpoint(Endpoint endpoint) {
            this.endpoint = endpoint;
            return this;
        }

        public Builder withPreferedCategory() {
            this.withPreferedCategory = true;
            return this;
        }

        public Builder withMerlinCategory() {
            this.withMerlinCategory = true;
            return this;
        }

        public Builder displayMode(DisplayMode displayMode){
            this.displayMode = displayMode;
            return this;
        }

        public Builder topAdsParams(TopAdsParams topAdsParams){
            this.topAdsParams = topAdsParams;
            return this;
        }

        public Config build() {
            return new Config(this);
        }
    }

}
