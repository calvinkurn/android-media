package com.tokopedia.campaign.shake.landing.data.entity;

import android.annotation.SuppressLint;

import com.google.gson.annotations.SerializedName;

/**
 * @author by nisie on 06/02/19.
 */
public class ValidCampaignPojo {

    @SerializedName("id")
    private String id;

    @SerializedName("tkp_url")
    private String url;

    @SerializedName("vibrate")
    private int vibrate;

    @SerializedName("success_message")
    private String successMessage;

    @SerializedName("title")
    private String title;

    /**
     * Need to suppress this false alarm,
     * due is_valid need a boolean data type
     * instead of Long or String (as suggestions).
     */
    @SuppressLint("Invalid Data Type")
    @SerializedName("is_valid")
    private boolean isValid;

    @SerializedName("error_message_id")
    private String errorMessage;

    @SerializedName("campaign_distance")
    private Double distance;

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public int getVibrate() {
        return vibrate;
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public String getTitle() {
        return title;
    }

    public boolean isValid() {
        return isValid;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public Double getDistance() {
        return distance;
    }
}
