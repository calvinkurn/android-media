package com.tokopedia.notifications.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author lalit.singh
 */
public class Media {

    @SerializedName("fallback_url")
    String fallbackUrl;

    @SerializedName("high_quality_url")
    String highQuality;

    @SerializedName("medium_quality_url")
    String mediumQuality;

    @SerializedName("low_quality_url")
    String lowQuality;

    @SerializedName("display_url")
    String displayUrl;

    public String getFallbackUrl() {
        return fallbackUrl;
    }

    public void setFallbackUrl(String fallbackUrl) {
        this.fallbackUrl = fallbackUrl;
    }

    public String getHighQuality() {
        return highQuality;
    }

    public void setHighQuality(String highQuality) {
        this.highQuality = highQuality;
    }

    public String getMediumQuality() {
        return mediumQuality;
    }

    public void setMediumQuality(String mediumQuality) {
        this.mediumQuality = mediumQuality;
    }

    public String getLowQuality() {
        return lowQuality;
    }

    public void setLowQuality(String lowQuality) {
        this.lowQuality = lowQuality;
    }

    public String getDisplayUrl() {
        return displayUrl;
    }

    public void setDisplayUrl(String displayUrl) {
        this.displayUrl = displayUrl;
    }
}
