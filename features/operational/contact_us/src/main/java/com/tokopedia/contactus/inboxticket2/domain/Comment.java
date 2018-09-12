package com.tokopedia.contactus.inboxticket2.domain;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

public class Comment{

	@SerializedName("create_by")
	private CreateBy createBy;

	@SerializedName("create_time")
	private String createTime;

	@SerializedName("attachment")
	private List<AttachmentItem> attachment;

	@SerializedName("message")
	private String message;

	public void setCreateBy(CreateBy createBy){
		this.createBy = createBy;
	}

	public CreateBy getCreateBy(){
		return createBy;
	}

	public void setCreateTime(String createTime){
		this.createTime = createTime;
	}

	public String getCreateTime(){
		return createTime;
	}

	public void setAttachment(List<AttachmentItem> attachment){
		this.attachment = attachment;
	}

	public List<AttachmentItem> getAttachment(){
		return attachment;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}
}