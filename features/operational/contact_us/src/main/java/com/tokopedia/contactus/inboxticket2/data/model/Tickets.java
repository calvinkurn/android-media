package com.tokopedia.contactus.inboxticket2.data.model;

import com.google.gson.annotations.SerializedName;
import com.tokopedia.contactus.inboxticket2.domain.AttachmentItem;
import com.tokopedia.contactus.inboxticket2.domain.CommentsItem;
import com.tokopedia.contactus.inboxticket2.domain.CreatedBy;

import java.util.List;

public class Tickets{
	@SerializedName("badCsatReasonList")
	private List<BadCsatReasonListItem> badCsatReasonList;
	@SerializedName("showRating")
	private boolean showRating;
	@SerializedName("id")
	private String id;
	@SerializedName("total_comments")
	private int totalComments;

	@SerializedName("comments")
	private List<CommentsItem> comments;

	@SerializedName("createTime")
	private String createTime;

	@SerializedName("subject")
	private String subject;

	@SerializedName("is_gandalf")
	private boolean isGandalf;

	@SerializedName("read_status")
	private String readStatus;

	@SerializedName("message")
	private String message;

	@SerializedName("createdBy")
	private CreatedBy createdBy;

	@SerializedName("solution_id")
	private int solutionId;

	@SerializedName("show_bad_CSAT_reason")
	private boolean showBadCSATReason;


	@SerializedName("number")
	private String number;

	@SerializedName("attachment")
	private List<AttachmentItem> attachment;

	@SerializedName("bad_rating_comment_id")
	private List<String> badRatingCommentId;

	@SerializedName("need_attachment")
	private boolean needAttachment;


	@SerializedName("invoice")
	private String invoice;

	@SerializedName("status")
	private String status;

	public void setTotalComments(int totalComments){
		this.totalComments = totalComments;
	}

	public int getTotalComments(){
		return totalComments;
	}

	public void setComments(List<CommentsItem> comments){
		this.comments = comments;
	}

	public List<CommentsItem> getComments(){
		return comments;
	}

	public void setCreateTime(String createTime){
		this.createTime = createTime;
	}

	public String getCreateTime(){
		return createTime;
	}

	public void setSubject(String subject){
		this.subject = subject;
	}

	public String getSubject(){
		return subject;
	}

	public void setIsGandalf(boolean isGandalf){
		this.isGandalf = isGandalf;
	}

	public boolean isIsGandalf(){
		return isGandalf;
	}

	public void setReadStatus(String readStatus){
		this.readStatus = readStatus;
	}

	public String getReadStatus(){
		return readStatus;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setCreatedBy(CreatedBy createdBy){
		this.createdBy = createdBy;
	}

	public CreatedBy getCreatedBy(){
		return createdBy;
	}

	public void setSolutionId(int solutionId){
		this.solutionId = solutionId;
	}

	public int getSolutionId(){
		return solutionId;
	}

	public void setShowBadCSATReason(boolean showBadCSATReason){
		this.showBadCSATReason = showBadCSATReason;
	}

	public boolean isShowBadCSATReason(){
		return showBadCSATReason;
	}

	public void setShowRating(boolean showRating){
		this.showRating = showRating;
	}

	public boolean isShowRating(){
		return showRating;
	}

	public void setNumber(String number){
		this.number = number;
	}

	public String getNumber(){
		return number;
	}

	public void setAttachment(List<AttachmentItem> attachment){
		this.attachment = attachment;
	}

	public List<AttachmentItem> getAttachment(){
		return attachment;
	}

	public void setBadRatingCommentId(List<String> badRatingCommentId){
		this.badRatingCommentId = badRatingCommentId;
	}

	public List<String> getBadRatingCommentId(){
		return badRatingCommentId;
	}

	public void setNeedAttachment(boolean needAttachment){
		this.needAttachment = needAttachment;
	}

	public boolean isNeedAttachment(){
		return needAttachment;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setInvoice(String invoice){
		this.invoice = invoice;
	}

	public String getInvoice(){
		return invoice;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	public void setBadCsatReasonList(List<BadCsatReasonListItem> badCsatReasonList){
		this.badCsatReasonList = badCsatReasonList;
	}

	public List<BadCsatReasonListItem> getBadCsatReasonList(){
		return badCsatReasonList;
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