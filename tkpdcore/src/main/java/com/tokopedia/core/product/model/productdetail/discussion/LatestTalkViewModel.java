package com.tokopedia.core.product.model.productdetail.discussion;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hangnadi on 8/22/17.
 */

public class LatestTalkViewModel implements Parcelable {
    private String talkId;
    private String talkDate;
    private String talkUserID;
    private String talkUsername;
    private String talkUserAvatar;
    private String talkMessage;
    private String commentId;
    private String commentMessage;
    private String commentDate;
    private String commentUserId;
    private String commentUserName;
    private String commentUserLabel;
    private String commentUserAvatar;
    private int talkCounterComment;

    public void setTalkId(String talkId) {
        this.talkId = talkId;
    }

    public String getTalkId() {
        return talkId;
    }

    public void setTalkDate(String talkDate) {
        this.talkDate = talkDate;
    }

    public String getTalkDate() {
        return talkDate;
    }

    public void setTalkUserID(String talkUserID) {
        this.talkUserID = talkUserID;
    }

    public String getTalkUserID() {
        return talkUserID;
    }

    public void setTalkUsername(String talkUsername) {
        this.talkUsername = talkUsername;
    }

    public String getTalkUsername() {
        return talkUsername;
    }

    public void setTalkUserAvatar(String talkUserAvatar) {
        this.talkUserAvatar = talkUserAvatar;
    }

    public String getTalkUserAvatar() {
        return talkUserAvatar;
    }

    public void setTalkMessage(String talkMessage) {
        this.talkMessage = talkMessage;
    }

    public String getTalkMessage() {
        return talkMessage;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentMessage(String commentMessage) {
        this.commentMessage = commentMessage;
    }

    public String getCommentMessage() {
        return commentMessage;
    }

    public void setCommentDate(String commentDate) {
        this.commentDate = commentDate;
    }

    public String getCommentDate() {
        return commentDate;
    }

    public void setCommentUserId(String commentUserId) {
        this.commentUserId = commentUserId;
    }

    public String getCommentUserId() {
        return commentUserId;
    }

    public void setCommentUserName(String commentUserName) {
        this.commentUserName = commentUserName;
    }

    public String getCommentUserName() {
        return commentUserName;
    }

    public void setCommentUserLabel(String commentUserLabel) {
        this.commentUserLabel = commentUserLabel;
    }

    public String getCommentUserLabel() {
        return commentUserLabel;
    }

    public void setCommentUserAvatar(String commentUserAvatar) {
        this.commentUserAvatar = commentUserAvatar;
    }

    public String getCommentUserAvatar() {
        return commentUserAvatar;
    }

    public void setTalkCounterComment(int talkCounterComment) {
        this.talkCounterComment = talkCounterComment;
    }

    public int getTalkCounterComment() {
        return talkCounterComment;
    }

    public LatestTalkViewModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.talkId);
        dest.writeString(this.talkDate);
        dest.writeString(this.talkUserID);
        dest.writeString(this.talkUsername);
        dest.writeString(this.talkUserAvatar);
        dest.writeString(this.talkMessage);
        dest.writeString(this.commentId);
        dest.writeString(this.commentMessage);
        dest.writeString(this.commentDate);
        dest.writeString(this.commentUserId);
        dest.writeString(this.commentUserName);
        dest.writeString(this.commentUserLabel);
        dest.writeString(this.commentUserAvatar);
        dest.writeInt(this.talkCounterComment);
    }

    protected LatestTalkViewModel(Parcel in) {
        this.talkId = in.readString();
        this.talkDate = in.readString();
        this.talkUserID = in.readString();
        this.talkUsername = in.readString();
        this.talkUserAvatar = in.readString();
        this.talkMessage = in.readString();
        this.commentId = in.readString();
        this.commentMessage = in.readString();
        this.commentDate = in.readString();
        this.commentUserId = in.readString();
        this.commentUserName = in.readString();
        this.commentUserLabel = in.readString();
        this.commentUserAvatar = in.readString();
        this.talkCounterComment = in.readInt();
    }

    public static final Creator<LatestTalkViewModel> CREATOR = new Creator<LatestTalkViewModel>() {
        @Override
        public LatestTalkViewModel createFromParcel(Parcel source) {
            return new LatestTalkViewModel(source);
        }

        @Override
        public LatestTalkViewModel[] newArray(int size) {
            return new LatestTalkViewModel[size];
        }
    };
}
