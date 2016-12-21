package com.tokopedia.core.talkview.inbox.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.Html;
import android.text.Spanned;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.talkview.model.TalkBaseModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TalkDetail extends TalkBaseModel implements Parcelable {

    @SerializedName("comment_user_reputation")
    @Expose
    private CommentUserReputation commentUserReputation;
    @SerializedName("comment_create_time_fmt")
    @Expose
    private String commentCreateTimeFmt;
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
    private int commentUserId;
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
     *     The commentShopName
     */
    public String getCommentShopName() {
        return Html.fromHtml(commentShopName).toString();
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

    /**
     *
     * @return
     *     The commentUserName
     */
    public String getCommentUserName() {
        return Html.fromHtml(commentUserName).toString();
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
     *     The commentMessage
     */
    public String getCommentMessage() {
        return Html.fromHtml(commentMessage).toString();
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

    public TalkDetail(){

    }

    protected TalkDetail(Parcel in) {
        commentUserReputation = (CommentUserReputation) in.readValue(CommentUserReputation.class.getClassLoader());
        commentCreateTimeFmt = in.readString();
        commentCreateTime = in.readString();
        commentShopName = in.readString();
        commentId = in.readString();
        commentUserLabel = in.readString();
        commentIsOwner = in.readInt();
        commentUserName = in.readString();
        commentUserImage = in.readString();
        commentMessage = in.readString();
        commentShopReputation = (CommentShopReputation) in.readValue(CommentShopReputation.class.getClassLoader());
        commentUserGender = in.readString();
        commentShopId = in.readString();
        commentTalkId = in.readString();
        commentUserId = in.readInt();
        commentIsModerator = in.readInt();
        commentShopImage = in.readString();
        commentUserLabelId = in.readInt();
        commentIsSeller = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(commentUserReputation);
        dest.writeString(commentCreateTimeFmt);
        dest.writeString(commentCreateTime);
        dest.writeString(commentShopName);
        dest.writeString(commentId);
        dest.writeString(commentUserLabel);
        dest.writeInt(commentIsOwner);
        dest.writeString(commentUserName);
        dest.writeString(commentUserImage);
        dest.writeString(commentMessage);
        dest.writeValue(commentShopReputation);
        dest.writeString(commentUserGender);
        dest.writeString(commentShopId);
        dest.writeString(commentTalkId);
        dest.writeInt(commentUserId);
        dest.writeInt(commentIsModerator);
        dest.writeString(commentShopImage);
        dest.writeInt(commentUserLabelId);
        dest.writeInt(commentIsSeller);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<TalkDetail> CREATOR = new Parcelable.Creator<TalkDetail>() {
        @Override
        public TalkDetail createFromParcel(Parcel in) {
            return new TalkDetail(in);
        }

        @Override
        public TalkDetail[] newArray(int size) {
            return new TalkDetail[size];
        }
    };

    public String getCommentCreateDateFmt() {
        try {
            SimpleDateFormat sdf;
            Locale id;
            id = new Locale("in", "ID");
            sdf = new SimpleDateFormat("dd MMMM yyyy, HH:mm z", id);
            SimpleDateFormat newSdf = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
            Calendar calNow = Calendar.getInstance();
            calNow.setTime(new Date());
            Calendar calYesterday = Calendar.getInstance();
            calYesterday.setTime(new Date());
            calYesterday.add(Calendar.DATE,-1);
            Calendar cal = Calendar.getInstance();

            cal.setTime(sdf.parse(getCommentCreateTime()));
            if (cal.get(Calendar.DAY_OF_YEAR) == calNow.get(Calendar.DAY_OF_YEAR)
                    && cal.get(Calendar.YEAR) == calNow.get(Calendar.YEAR)) {
                return "Hari ini";
            } else if (cal.get(Calendar.DAY_OF_YEAR) == calYesterday.get(Calendar.DAY_OF_YEAR)
                    && cal.get(Calendar.YEAR) == calYesterday.get(Calendar.YEAR)){
                return "Kemarin";
            }
            else {
                return newSdf.format(sdf.parse(getCommentCreateTimeFmt()));
            }
        } catch (ParseException e) {
            return "";
        }
    }
}