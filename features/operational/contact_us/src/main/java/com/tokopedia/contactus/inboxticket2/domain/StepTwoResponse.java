package com.tokopedia.contactus.inboxticket2.domain;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

public class StepTwoResponse{

	@SerializedName("comment")
	private Comment comment;

	@SerializedName("is_success")
	private int isSuccess;

	public void setComment(Comment comment){
		this.comment = comment;
	}

	public Comment getComment(){
		return comment;
	}

	public void setIsSuccess(int isSuccess){
		this.isSuccess = isSuccess;
	}

	public int getIsSuccess(){
		return isSuccess;
	}
}