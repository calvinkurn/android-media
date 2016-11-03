package com.tokopedia.tkpd.talkview.product.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.Html;
import android.text.Spanned;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.tkpd.talkview.model.TalkBaseModel;

public class CommentTalk extends TalkBaseModel implements Parcelable {

    @SerializedName("comment_user_name")
    @Expose
    private String commentUserName;
    @SerializedName("comment_user_label_id")
    @Expose
    private int commentUserLabelId;
    @SerializedName("comment_shop_image")
    @Expose
    private String commentShopImage;
    @SerializedName("comment_shop_name")
    @Expose
    private String commentShopName;
    @SerializedName("comment_user_image")
    @Expose
    private String commentUserImage;
    @SerializedName("comment_create_time")
    @Expose
    private String commentCreateTime;
    @SerializedName("comment_is_seller")
    @Expose
    private int commentIsSeller;
    @SerializedName("comment_is_moderator")
    @Expose
    private int commentIsModerator;
    @SerializedName("comment_talk_id")
    @Expose
    private String commentTalkId;
    @SerializedName("comment_shop_reputation")
    @Expose
    private CommentShopReputation commentShopReputation;
    @SerializedName("comment_create_time_fmt")
    @Expose
    private String commentCreateTimeFmt;
    @SerializedName("comment_user_reputation")
    @Expose
    private CommentUserReputation commentUserReputation;
    @SerializedName("comment_shop_id")
    @Expose
    private String commentShopId;
    @SerializedName("comment_user_label")
    @Expose
    private String commentUserLabel;
    @SerializedName("comment_id")
    @Expose
    private String commentId;
    @SerializedName("comment_message")
    @Expose
    private String commentMessage;
    @SerializedName("comment_user_id")
    @Expose
    private int commentUserId;
    @SerializedName("comment_user_gender")
    @Expose
    private String commentUserGender;
    @SerializedName("comment_is_owner")
    @Expose
    private int commentIsOwner;

    /**
     * 
     * @return
     *     The commentUserName
     */
    public String getCommentUserName() {
        return commentUserName;
    }

    /**
     * 
     * @param commentUserName
     *     The comment_user_name
     */
    public void setCommentUserName(String commentUserName) {
        this.commentUserName = commentUserName;
    }

    /**
     * 
     * @return
     *     The commentUserLabelId
     */
    public int getCommentUserLabelId() {
        return commentUserLabelId;
    }

    /**
     * 
     * @param commentUserLabelId
     *     The comment_user_label_id
     */
    public void setCommentUserLabelId(int commentUserLabelId) {
        this.commentUserLabelId = commentUserLabelId;
    }

    /**
     * 
     * @return
     *     The commentShopImage
     */
    public String getCommentShopImage() {
        return commentShopImage;
    }

    /**
     * 
     * @param commentShopImage
     *     The comment_shop_image
     */
    public void setCommentShopImage(String commentShopImage) {
        this.commentShopImage = commentShopImage;
    }

    /**
     * 
     * @return
     *     The commentShopName
     */
    public String getCommentShopName() {
        return commentShopName;
    }

    /**
     * 
     * @param commentShopName
     *     The comment_shop_name
     */
    public void setCommentShopName(String commentShopName) {
        this.commentShopName = commentShopName;
    }

    /**
     * 
     * @return
     *     The commentUserImage
     */
    public String getCommentUserImage() {
        return commentUserImage;
    }

    /**
     * 
     * @param commentUserImage
     *     The comment_user_image
     */
    public void setCommentUserImage(String commentUserImage) {
        this.commentUserImage = commentUserImage;
    }

    /**
     * 
     * @return
     *     The commentCreateTime
     */
    public String getCommentCreateTime() {
        return commentCreateTime;
    }

    /**
     * 
     * @param commentCreateTime
     *     The comment_create_time
     */
    public void setCommentCreateTime(String commentCreateTime) {
        this.commentCreateTime = commentCreateTime;
    }

    /**
     * 
     * @return
     *     The commentIsSeller
     */
    public int getCommentIsSeller() {
        return commentIsSeller;
    }

    /**
     * 
     * @param commentIsSeller
     *     The comment_is_seller
     */
    public void setCommentIsSeller(int commentIsSeller) {
        this.commentIsSeller = commentIsSeller;
    }

    /**
     * 
     * @return
     *     The commentIsModerator
     */
    public int getCommentIsModerator() {
        return commentIsModerator;
    }

    /**
     * 
     * @param commentIsModerator
     *     The comment_is_moderator
     */
    public void setCommentIsModerator(int commentIsModerator) {
        this.commentIsModerator = commentIsModerator;
    }

    /**
     * 
     * @return
     *     The commentTalkId
     */
    public String getCommentTalkId() {
        return commentTalkId;
    }

    /**
     * 
     * @param commentTalkId
     *     The comment_talk_id
     */
    public void setCommentTalkId(String commentTalkId) {
        this.commentTalkId = commentTalkId;
    }

    /**
     * 
     * @return
     *     The commentShopReputation
     */
    public CommentShopReputation getCommentShopReputation() {
        return commentShopReputation;
    }

    /**
     * 
     * @param commentShopReputation
     *     The comment_shop_reputation
     */
    public void setCommentShopReputation(CommentShopReputation commentShopReputation) {
        this.commentShopReputation = commentShopReputation;
    }

    /**
     * 
     * @return
     *     The commentCreateTimeFmt
     */
    public String getCommentCreateTimeFmt() {
        return commentCreateTimeFmt;
    }

    /**
     * 
     * @param commentCreateTimeFmt
     *     The comment_create_time_fmt
     */
    public void setCommentCreateTimeFmt(String commentCreateTimeFmt) {
        this.commentCreateTimeFmt = commentCreateTimeFmt;
    }

    /**
     * 
     * @return
     *     The commentUserReputation
     */
    public CommentUserReputation getCommentUserReputation() {
        return commentUserReputation;
    }

    /**
     * 
     * @param commentUserReputation
     *     The comment_user_reputation
     */
    public void setCommentUserReputation(CommentUserReputation commentUserReputation) {
        this.commentUserReputation = commentUserReputation;
    }

    /**
     * 
     * @return
     *     The commentShopId
     */
    public String getCommentShopId() {
        return commentShopId;
    }

    /**
     * 
     * @param commentShopId
     *     The comment_shop_id
     */
    public void setCommentShopId(String commentShopId) {
        this.commentShopId = commentShopId;
    }

    /**
     * 
     * @return
     *     The commentUserLabel
     */
    public String getCommentUserLabel() {
        return commentUserLabel;
    }

    /**
     * 
     * @param commentUserLabel
     *     The comment_user_label
     */
    public void setCommentUserLabel(String commentUserLabel) {
        this.commentUserLabel = commentUserLabel;
    }

    /**
     * 
     * @return
     *     The commentId
     */
    public String getCommentId() {
        return commentId;
    }

    /**
     * 
     * @param commentId
     *     The comment_id
     */
    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    /**
     * 
     * @return
     *     The commentMessage
     */
    public String getCommentMessage() {
        return commentMessage;
    }

    public Spanned getCommentMessageSpanned() {
        return Html.fromHtml(commentMessage);
    }

    /**
     * 
     * @param commentMessage
     *     The comment_message
     */
    public void setCommentMessage(String commentMessage) {
        this.commentMessage = commentMessage;
    }

    /**
     * 
     * @return
     *     The commentUserId
     */
    public int getCommentUserId() {
        return commentUserId;
    }

    /**
     * 
     * @param commentUserId
     *     The comment_user_id
     */
    public void setCommentUserId(int commentUserId) {
        this.commentUserId = commentUserId;
    }

    /**
     * 
     * @return
     *     The commentUserGender
     */
    public String getCommentUserGender() {
        return commentUserGender;
    }

    /**
     * 
     * @param commentUserGender
     *     The comment_user_gender
     */
    public void setCommentUserGender(String commentUserGender) {
        this.commentUserGender = commentUserGender;
    }

    /**
     * 
     * @return
     *     The commentIsOwner
     */
    public int getCommentIsOwner() {
        return commentIsOwner;
    }

    /**
     * 
     * @param commentIsOwner
     *     The comment_is_owner
     */
    public void setCommentIsOwner(int commentIsOwner) {
        this.commentIsOwner = commentIsOwner;
    }

    public CommentTalk(){

    }

    protected CommentTalk(Parcel in) {
        commentUserName = in.readString();
        commentUserLabelId = in.readInt();
        commentShopImage = in.readString();
        commentShopName = in.readString();
        commentUserImage = in.readString();
        commentCreateTime = in.readString();
        commentIsSeller = in.readInt();
        commentIsModerator = in.readInt();
        commentTalkId = in.readString();
        commentShopReputation = (CommentShopReputation) in.readValue(CommentShopReputation.class.getClassLoader());
        commentCreateTimeFmt = in.readString();
        commentUserReputation = (CommentUserReputation) in.readValue(CommentUserReputation.class.getClassLoader());
        commentShopId = in.readString();
        commentUserLabel = in.readString();
        commentId = in.readString();
        commentMessage = in.readString();
        commentUserId = in.readInt();
        commentUserGender = in.readString();
        commentIsOwner = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }



    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(commentUserName);
        dest.writeInt(commentUserLabelId);
        dest.writeString(commentShopImage);
        dest.writeString(commentShopName);
        dest.writeString(commentUserImage);
        dest.writeString(commentCreateTime);
        dest.writeInt(commentIsSeller);
        dest.writeInt(commentIsModerator);
        dest.writeString(commentTalkId);
        dest.writeValue(commentShopReputation);
        dest.writeString(commentCreateTimeFmt);
        dest.writeValue(commentUserReputation);
        dest.writeString(commentShopId);
        dest.writeString(commentUserLabel);
        dest.writeString(commentId);
        dest.writeString(commentMessage);
        dest.writeInt(commentUserId);
        dest.writeString(commentUserGender);
        dest.writeInt(commentIsOwner);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CommentTalk> CREATOR = new Parcelable.Creator<CommentTalk>() {
        @Override
        public CommentTalk createFromParcel(Parcel in) {
            return new CommentTalk(in);
        }

        @Override
        public CommentTalk[] newArray(int size) {
            return new CommentTalk[size];
        }
    };
}