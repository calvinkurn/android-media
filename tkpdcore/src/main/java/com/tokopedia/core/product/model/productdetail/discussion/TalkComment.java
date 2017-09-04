package com.tokopedia.core.product.model.productdetail.discussion;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hangnadi on 8/22/17.
 */

public class TalkComment {

    @SerializedName("comment_user_reputation")
    @Expose
    private CommentUserReputation commentUserReputation;
    @SerializedName("comment_create_time_list")
    @Expose
    private CommentCreateTimeList commentCreateTimeList;
    @SerializedName("comment_create_time")
    @Expose
    private String commentCreateTime;
    @SerializedName("comment_shop_name")
    @Expose
    private String commentShopName;
    @SerializedName("comment_id")
    @Expose
    private String commentId;
    @SerializedName("comment_user_label")
    @Expose
    private String commentUserLabel;
    @SerializedName("comment_is_owner")
    @Expose
    private int commentIsOwner;
    @SerializedName("comment_user_name")
    @Expose
    private String commentUserName;
    @SerializedName("comment_user_image")
    @Expose
    private String commentUserImage;
    @SerializedName("comment_message")
    @Expose
    private String commentMessage;
    @SerializedName("comment_shop_reputation")
    @Expose
    private CommentShopReputation commentShopReputation;
    @SerializedName("comment_user_gender")
    @Expose
    private String commentUserGender;
    @SerializedName("comment_shop_id")
    @Expose
    private String commentShopId;
    @SerializedName("comment_talk_id")
    @Expose
    private String commentTalkId;
    @SerializedName("comment_user_id")
    @Expose
    private String commentUserId;
    @SerializedName("comment_is_moderator")
    @Expose
    private int commentIsModerator;
    @SerializedName("comment_shop_image")
    @Expose
    private String commentShopImage;
    @SerializedName("comment_user_label_id")
    @Expose
    private int commentUserLabelId;
    @SerializedName("comment_is_seller")
    @Expose
    private int commentIsSeller;

    public CommentUserReputation getCommentUserReputation() {
        return commentUserReputation;
    }

    public void setCommentUserReputation(CommentUserReputation commentUserReputation) {
        this.commentUserReputation = commentUserReputation;
    }

    public CommentCreateTimeList getCommentCreateTimeList() {
        return commentCreateTimeList;
    }

    public void setCommentCreateTimeList(CommentCreateTimeList commentCreateTimeList) {
        this.commentCreateTimeList = commentCreateTimeList;
    }

    public String getCommentCreateTime() {
        return commentCreateTime;
    }

    public void setCommentCreateTime(String commentCreateTime) {
        this.commentCreateTime = commentCreateTime;
    }

    public String getCommentShopName() {
        return commentShopName;
    }

    public void setCommentShopName(String commentShopName) {
        this.commentShopName = commentShopName;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getCommentUserLabel() {
        return commentUserLabel;
    }

    public void setCommentUserLabel(String commentUserLabel) {
        this.commentUserLabel = commentUserLabel;
    }

    public int getCommentIsOwner() {
        return commentIsOwner;
    }

    public void setCommentIsOwner(int commentIsOwner) {
        this.commentIsOwner = commentIsOwner;
    }

    public String getCommentUserName() {
        return commentUserName;
    }

    public void setCommentUserName(String commentUserName) {
        this.commentUserName = commentUserName;
    }

    public String getCommentUserImage() {
        return commentUserImage;
    }

    public void setCommentUserImage(String commentUserImage) {
        this.commentUserImage = commentUserImage;
    }

    public String getCommentMessage() {
        return commentMessage;
    }

    public void setCommentMessage(String commentMessage) {
        this.commentMessage = commentMessage;
    }

    public CommentShopReputation getCommentShopReputation() {
        return commentShopReputation;
    }

    public void setCommentShopReputation(CommentShopReputation commentShopReputation) {
        this.commentShopReputation = commentShopReputation;
    }

    public String getCommentUserGender() {
        return commentUserGender;
    }

    public void setCommentUserGender(String commentUserGender) {
        this.commentUserGender = commentUserGender;
    }

    public String getCommentShopId() {
        return commentShopId;
    }

    public void setCommentShopId(String commentShopId) {
        this.commentShopId = commentShopId;
    }

    public String getCommentTalkId() {
        return commentTalkId;
    }

    public void setCommentTalkId(String commentTalkId) {
        this.commentTalkId = commentTalkId;
    }

    public String getCommentUserId() {
        return commentUserId;
    }

    public void setCommentUserId(String commentUserId) {
        this.commentUserId = commentUserId;
    }

    public int getCommentIsModerator() {
        return commentIsModerator;
    }

    public void setCommentIsModerator(int commentIsModerator) {
        this.commentIsModerator = commentIsModerator;
    }

    public String getCommentShopImage() {
        return commentShopImage;
    }

    public void setCommentShopImage(String commentShopImage) {
        this.commentShopImage = commentShopImage;
    }

    public int getCommentUserLabelId() {
        return commentUserLabelId;
    }

    public void setCommentUserLabelId(int commentUserLabelId) {
        this.commentUserLabelId = commentUserLabelId;
    }

    public int getCommentIsSeller() {
        return commentIsSeller;
    }

    public void setCommentIsSeller(int commentIsSeller) {
        this.commentIsSeller = commentIsSeller;
    }
}
