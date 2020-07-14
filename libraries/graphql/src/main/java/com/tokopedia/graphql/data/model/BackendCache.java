package com.tokopedia.graphql.data.model;

public class BackendCache {
    /*Query content md5*/
    private String digest;

    /*Expiry time in seconds*/
    private int maxAge;

    /*Caching tpe enums*/
    private int cacheType;

    /*Purge frequency in minutes*/
    private int autoPurgeTime;

    /*To check for public and private cache*/
    private boolean isGuest;

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public int getCacheType() {
        return cacheType;
    }

    public void setCacheType(int cacheType) {
        this.cacheType = cacheType;
    }

    public int getAutoPurgeTime() {
        return autoPurgeTime;
    }

    public void setAutoPurgeTime(int autoPurgeTime) {
        this.autoPurgeTime = autoPurgeTime;
    }

    public boolean isGuest() {
        return isGuest;
    }

    public void setGuest(boolean guest) {
        isGuest = guest;
    }

    @Override
    public String toString() {
        return "BackendCache{" +
                "digest='" + digest + '\'' +
                ", maxAge=" + maxAge +
                ", cacheType=" + cacheType +
                ", autoPurge=" + autoPurgeTime +
                ", isGuest=" + isGuest +
                '}';
    }
}
