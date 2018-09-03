package com.tokopedia.contactus.inboxticket2.domain;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("show_bad_reason")
	private int showBadReason;

	@SerializedName("bad_reason_code")
	private int badReasonCode;

	@SerializedName("rating")
	private String rating;

	@SerializedName("comment_id")
	private String commentId;

	public void setShowBadReason(int showBadReason){
		this.showBadReason = showBadReason;
	}

	public int getShowBadReason(){
		return showBadReason;
	}

	public void setBadReasonCode(int badReasonCode){
		this.badReasonCode = badReasonCode;
	}

	public int getBadReasonCode(){
		return badReasonCode;
	}

	public void setRating(String rating){
		this.rating = rating;
	}

	public String getRating(){
		return rating;
	}

	public void setCommentId(String commentId){
		this.commentId = commentId;
	}

	public String getCommentId(){
		return commentId;
	}
}