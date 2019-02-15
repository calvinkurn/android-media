package com.tokopedia.contactus.inboxticket2.data.model;

import java.util.List;

public class Tickets{
	private List<BadCsatReasonListItem> badCsatReasonList;
	private boolean showRating;
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