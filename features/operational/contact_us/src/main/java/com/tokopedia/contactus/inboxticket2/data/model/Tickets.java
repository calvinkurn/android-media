package com.tokopedia.contactus.inboxticket2.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Tickets{
	@SerializedName("badCsatReasonList")
	private List<BadCsatReasonListItem> badCsatReasonList;
	@SerializedName("showRating")
	private boolean showRating;
	@SerializedName("id")
	private String id;

	public void setBadCsatReasonList(List<BadCsatReasonListItem> badCsatReasonList){
		this.badCsatReasonList = badCsatReasonList;
	}

	public List<BadCsatReasonListItem> getBadCsatReasonList(){
		return badCsatReasonList;
	}

	public void setShowRating(boolean showRating){
		this.showRating = showRating;
	}

	public boolean isShowRating(){
		return showRating;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	@Override
 	public String toString(){
		return 
			"Tickets{" + 
			"badCsatReasonList = '" + badCsatReasonList + '\'' + 
			",showRating = '" + showRating + '\'' + 
			",id = '" + id + '\'' + 
			"}";
		}
}