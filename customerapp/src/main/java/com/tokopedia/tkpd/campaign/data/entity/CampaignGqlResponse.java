package com.tokopedia.tkpd.campaign.data.entity;

import com.google.gson.annotations.SerializedName;

/**
 * @author by nisie on 06/02/19.
 */
public class CampaignGqlResponse {

    @SerializedName("triggerVerifyShake")
    private CampaignResponseEntity campaignResponseEntity;

    public CampaignResponseEntity getCampaignResponseEntity() {
        return campaignResponseEntity;
    }
}
