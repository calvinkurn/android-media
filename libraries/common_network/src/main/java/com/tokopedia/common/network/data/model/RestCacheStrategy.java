package com.tokopedia.common.network.data.model;

import com.tokopedia.common.network.util.RestConstant;

/**
 * Caching strategy
 */
public class RestCacheStrategy {
    // Mandatory param if you are going to use this caching strategy
    private CacheType type;

    // optional
    private long expiryTime;

    // optional
    private boolean isSessionIncluded;

    private RestCacheStrategy(Builder builder) {
        this.type = builder.type;
        this.expiryTime = builder.expiryTime < 1 ? RestConstant.ExpiryTimes.MINUTE_30.val() : builder.expiryTime;
        this.isSessionIncluded = builder.isSessionIncluded;
    }

    public CacheType getType() {
        return type;
    }

    public long getExpiryTime() {
        return expiryTime;
    }

    public boolean isSessionIncluded() {
        return isSessionIncluded;
    }

    //Builder Class
    public static class Builder {

        private CacheType type;

        //optional parameters
        private long expiryTime;
        private boolean isSessionIncluded;

        public Builder(CacheType type) {
            this.type = type;
        }

        public Builder setExpiryTime(long expiryTime) {
            this.expiryTime = expiryTime;
            return this;
        }

        public Builder setSessionIncluded(boolean isSessionIncluded) {
            this.isSessionIncluded = isSessionIncluded;
            return this;
        }

        public RestCacheStrategy build() {
            return new RestCacheStrategy(this);
        }

    }

    @Override
    public String toString() {
        return "RestCacheStrategy{" +
                "type=" + type +
                ", expiryTime=" + expiryTime +
                ", isSessionIncluded=" + isSessionIncluded +
                '}';
    }
}
