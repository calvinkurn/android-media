package com.tokopedia.tkpd.campaign.data.entity;

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

    @SerializedName("is_valid")
    private boolean isValid;

    @SerializedName("error_message_id")
    private String errorMessage;

    @SerializedName("campaign_distance")
    private Double distance;

}
