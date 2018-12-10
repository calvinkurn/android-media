package com.tokopedia.tkpd.campaign.data.entity;

import com.google.gson.annotations.SerializedName;

public class CampaignResponseEntity {

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("campaign_type")
	private int campaignType;

	@SerializedName("name")
	private String name;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id")
	private int id;

	@SerializedName("title")
	private String title;

	@SerializedName("tkp_url")
	private String url;

    @SerializedName("vibrate")
    private int vibrate;

	@SerializedName("status")
	private int status;

	@SerializedName("message")
	private String message;
	@SerializedName("enable")
	private boolean enable;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setCampaignType(int campaignType){
		this.campaignType = campaignType;
	}

	public int getCampaignType(){
		return campaignType;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public void setUrl(String url){
		this.url = url;
	}

	public String getUrl(){
		return url;
	}

	public void setStatus(int status){
		this.status = status;
	}

	public int getStatus(){
		return status;
	}

    public int getVibrate() {
        return vibrate;
    }

    public void setVibrate(int vibrate) {
        this.vibrate = vibrate;
    }

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	@Override
 	public String toString(){
		return 
			"CampaignResponseEntity{" + 
			"updated_at = '" + updatedAt + '\'' + 
			",campaign_type = '" + campaignType + '\'' + 
			",name = '" + name + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",id = '" + id + '\'' + 
			",title = '" + title + '\'' + 
			",url = '" + url + '\'' +
                    ",status = '" + status + '\'' +
                    ",vibrate = '" + vibrate + '\'' +
                    ",enable = '" + enable + '\'' +

                    "}";
		}
}