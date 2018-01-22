package com.tokopedia.tkpd.campaign.data.entity;

import com.google.gson.annotations.SerializedName;

public class CampaignRequestEntity {

	@SerializedName("message")
	private String message;

	@SerializedName("tkp_campaign_id")
	private int campaignId;

	@SerializedName("tkp_campaign_name")
	private String campaignName;

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setCampaignId(int campaignId){
		this.campaignId = campaignId;
	}

	public int getCampaignId(){
		return campaignId;
	}

	public void setCampaignName(String campaignUrl){
		this.campaignName = campaignUrl;
	}

	public String getCampaignName(){
		return campaignName;
	}

	@Override
 	public String toString(){
		return 
			"CampaignRequestEntity{" +
			"message = '" + message + '\'' + 
			",campaign_id = '" + campaignId + '\'' + 
			",campaign_url = '" + campaignName + '\'' +
			"}";
		}
}