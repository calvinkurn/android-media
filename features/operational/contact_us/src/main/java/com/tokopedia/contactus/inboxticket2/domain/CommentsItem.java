package com.tokopedia.contactus.inboxticket2.domain;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

public class CommentsItem {

    @SerializedName("note")
    private String note;

    @SerializedName("create_time")
    private String createTime;

    @SerializedName("attachment")
    private List<AttachmentItem> attachment;

    @SerializedName("message_plaintext")
    private String messagePlaintext;

    @SerializedName("rating")
    private String rating;

    @SerializedName("id")
    private String id;

    @SerializedName("message")
    private String message;

    @SerializedName("created_by")
    private CreatedBy createdBy;

    private String shortTime;

    private boolean isCollapsed = true;

    public boolean isCollapsed() {
        return isCollapsed;
    }

    public void setCollapsed(boolean collapsed) {
        isCollapsed = collapsed;
    }

    public String getShortTime() {
        return shortTime;
    }

    public void setShortTime(String shortTime) {
        this.shortTime = shortTime;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getNote() {
        return note;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setAttachment(List<AttachmentItem> attachment) {
        this.attachment = attachment;
    }

    public List<AttachmentItem> getAttachment() {
        return attachment;
    }

    public void setMessagePlaintext(String messagePlaintext) {
        this.messagePlaintext = messagePlaintext;
    }

    public String getMessagePlaintext() {
        return messagePlaintext;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getRating() {
        return rating;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setCreatedBy(CreatedBy createdBy) {
        this.createdBy = createdBy;
    }

    public CreatedBy getCreatedBy() {
        return createdBy;
    }
}