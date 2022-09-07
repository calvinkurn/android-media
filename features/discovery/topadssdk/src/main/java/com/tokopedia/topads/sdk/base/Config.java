package com.tokopedia.topads.sdk.base;

import com.tokopedia.url.TokopediaUrl;
import com.tokopedia.topads.sdk.domain.TopAdsParams;

/**
 * Created by errysuprayogi on 3/27/17.
 */

public class Config {

    public static String TOPADS_BASE_URL = TokopediaUrl.Companion.getInstance().getTA();
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
        private TopAdsParams topAdsParams;

        public Builder() {
            baseUrl = TOPADS_BASE_URL;
            device = DEFAULT_DEVICE;
            clientId = DEFAULT_CLIENT_ID;
            topAdsParams = new TopAdsParams();
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

        public Builder topAdsParams(TopAdsParams topAdsParams) {
            this.topAdsParams.getParam().putAll(topAdsParams.getParam());
            return this;
        }

        public Config build() {
            return new Config(this);
        }
    }

}
