package com.tokopedia.tkpd.campaign.data.entity;

import com.google.gson.annotations.SerializedName;

public class CampaignResponseEntity{

	@SerializedName("message")
	private String message;

	@SerializedName("campaign_id")
	private int campaignId;

	@SerializedName("campaign_url")
	private String campaignUrl;

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

	public void setCampaignUrl(String campaignUrl){
		this.campaignUrl = campaignUrl;
	}

	public String getCampaignUrl(){
		return campaignUrl;
	}

	@Override
 	public String toString(){
		return 
			"CampaignResponseEntity{" + 
			"message = '" + message + '\'' + 
			",campaign_id = '" + campaignId + '\'' + 
			",campaign_url = '" + campaignUrl + '\'' + 
			"}";
		}
}