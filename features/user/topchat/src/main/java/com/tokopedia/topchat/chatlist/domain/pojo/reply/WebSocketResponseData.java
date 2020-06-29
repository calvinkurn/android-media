
package com.tokopedia.topchat.chatlist.domain.pojo.reply;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class  WebSocketResponseData {

    @SerializedName("msg_id")
    @Expose
    private int msgId;
    @SerializedName("from_uid")
    @Expose
    private int fromUid;
    @SerializedName("from")
    @Expose
    private String from;
    @SerializedName("from_role")
    @Expose
    private String fromRole;
    @SerializedName("to_uid")
    @Expose
    private int toUid;
    @SerializedName("message")
    @Expose
    private Message message;
    @SerializedName("start_time")
    @Expose
    private String startTime;
    @SerializedName("thumbnail")
    @Expose
    private String imageUri;
    @SerializedName("attachment")
    @Expose
    private Attachment attachment;
    @SerializedName("show_rating")
    @Expose
    private boolean showRating;
    @SerializedName("rating_status")
    @Expose
    private int ratingStatus;
    @SerializedName("is_bot")
    @Expose
    private boolean isBot;
    @SerializedName("is_opposite")
    @Expose
    private boolean isOpposite;
    @SerializedName("is_auto_reply")
    @Expose
    private boolean isAutoReply;

    public boolean isAutoReply() {
        return isAutoReply;
    }

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public int getFromUid() {
        return fromUid;
    }

    public void setFromUid(int fromUid) {
        this.fromUid = fromUid;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public int getToUid() {
        return toUid;
    }

    public void setToUid(int toUid) {
        this.toUid = toUid;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getFromRole() {
        return fromRole;
    }

    public void setFromRole(String fromRole) {
        this.fromRole = fromRole;
    }

    public Attachment getAttachment() {
        return attachment;
    }

    public void setAttachment(Attachment attachment) {
        this.attachment = attachment;
    }

    public boolean isShowRating() {
        return showRating;
    }

    public void setShowRating(boolean showRating) {
        this.showRating = showRating;
    }

    public int getRatingStatus() {
        return ratingStatus;
    }

    public void setRatingStatus(int ratingStatus) {
        this.ratingStatus = ratingStatus;
    }

    public boolean isBot() {
        return isBot;
    }

    public boolean getIsOpposite() {
        return isOpposite;
    }

    public void setIsOpposite(boolean isOpposite) {
        this.isOpposite = isOpposite;
    }
}
