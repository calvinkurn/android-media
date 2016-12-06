package com.tokopedia.core.talk.inboxtalk.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.Html;
import android.text.Spanned;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.var.RecyclerViewItem;


public class InboxTalk extends RecyclerViewItem implements Parcelable {

    @SerializedName("talk_shop_id")
    @Expose
    private String talkShopId;
    @SerializedName("talk_user_image")
    @Expose
    private String talkUserImage;
    @SerializedName("talk_product_status")
    @Expose
    private String talkProductStatus;
    @SerializedName("talk_create_time")
    @Expose
    private String talkCreateTime;
    @SerializedName("talk_read_status")
    @Expose
    private int talkReadStatus;
    @SerializedName("talk_user_name")
    @Expose
    private String talkUserName;
    @SerializedName("talk_user_url")
    @Expose
    private String talkUserUrl;
    @SerializedName("talk_id")
    @Expose
    private String talkId;
    @SerializedName("talk_user_reputation")
    @Expose
    private TalkUserReputation talkUserReputation;
    @SerializedName("talk_inbox_id")
    @Expose
    private String talkInboxId;
    @SerializedName("talk_own")
    @Expose
    private int talkOwn;
    @SerializedName("talk_product_name")
    @Expose
    private String talkProductName;
    @SerializedName("talk_create_time_fmt")
    @Expose
    private String talkCreateTimeFmt;
    @SerializedName("talk_message")
    @Expose
    private String talkMessage;
    @SerializedName("talk_follow_status")
    @Expose
    private int talkFollowStatus;
    @SerializedName("talk_total_comment")
    @Expose
    private String talkTotalComment;
    @SerializedName("talk_user_label")
    @Expose
    private String talkUserLabel;
    @SerializedName("talk_user_label_id")
    @Expose
    private int talkUserLabelId;
    @SerializedName("talk_product_id")
    @Expose
    private String talkProductId;
    @SerializedName("talk_product_image")
    @Expose
    private String talkProductImage;
    @SerializedName("talk_user_id")
    @Expose
    private String talkUserId;

    /**
     *
     * @return
     *     The talkShopId
     */
    public String getTalkShopId() {
        return talkShopId;
    }

    /**
     *
     * @param talkShopId
     *     The talk_shop_id
     */
    public void setTalkShopId(String talkShopId) {
        this.talkShopId = talkShopId;
    }

    /**
     *
     * @return
     *     The talkUserImage
     */
    public String getTalkUserImage() {
        return talkUserImage;
    }

    /**
     *
     * @param talkUserImage
     *     The talk_user_image
     */
    public void setTalkUserImage(String talkUserImage) {
        this.talkUserImage = talkUserImage;
    }

    /**
     *
     * @return
     *     The talkProductStatus
     */
    public String getTalkProductStatus() {
        return talkProductStatus;
    }

    /**
     *
     * @param talkProductStatus
     *     The talk_product_status
     */
    public void setTalkProductStatus(String talkProductStatus) {
        this.talkProductStatus = talkProductStatus;
    }

    /**
     *
     * @return
     *     The talkCreateTime
     */
    public String getTalkCreateTime() {
        return talkCreateTime;
    }

    /**
     *
     * @param talkCreateTime
     *     The talk_create_time
     */
    public void setTalkCreateTime(String talkCreateTime) {
        this.talkCreateTime = talkCreateTime;
    }

    /**
     *
     * @return
     *     The talkReadStatus
     */
    public int getTalkReadStatus() {
        return talkReadStatus;
    }

    /**
     *
     * @param talkReadStatus
     *     The talk_read_status
     */
    public void setTalkReadStatus(int talkReadStatus) {
        this.talkReadStatus = talkReadStatus;
    }

    /**
     *
     * @return
     *     The talkUserName
     */
    public String getTalkUserName() {
        return talkUserName;
    }

    /**
     *
     * @param talkUserName
     *     The talk_user_name
     */
    public void setTalkUserName(String talkUserName) {
        this.talkUserName = talkUserName;
    }

    /**
     *
     * @return
     *     The talkUserUrl
     */
    public String getTalkUserUrl() {
        return talkUserUrl;
    }

    /**
     *
     * @param talkUserUrl
     *     The talk_user_url
     */
    public void setTalkUserUrl(String talkUserUrl) {
        this.talkUserUrl = talkUserUrl;
    }

    /**
     *
     * @return
     *     The talkId
     */
    public String getTalkId() {
        return talkId;
    }

    /**
     *
     * @param talkId
     *     The talk_id
     */
    public void setTalkId(String talkId) {
        this.talkId = talkId;
    }

    /**
     *
     * @return
     *     The talkUserReputation
     */
    public TalkUserReputation getTalkUserReputation() {
        return talkUserReputation;
    }

    /**
     *
     * @param talkUserReputation
     *     The talk_user_reputation
     */
    public void setTalkUserReputation(TalkUserReputation talkUserReputation) {
        this.talkUserReputation = talkUserReputation;
    }

    /**
     *
     * @return
     *     The talkInboxId
     */
    public String getTalkInboxId() {
        return talkInboxId;
    }

    /**
     *
     * @param talkInboxId
     *     The talk_inbox_id
     */
    public void setTalkInboxId(String talkInboxId) {
        this.talkInboxId = talkInboxId;
    }

    /**
     *
     * @return
     *     The talkOwn
     */
    public int getTalkOwn() {
        return talkOwn;
    }

    /**
     *
     * @param talkOwn
     *     The talk_own
     */
    public void setTalkOwn(int talkOwn) {
        this.talkOwn = talkOwn;
    }

    /**
     *
     * @return
     *     The talkProductName
     */
    public String getTalkProductName() {
        return talkProductName;
    }

    /**
     *
     * @param talkProductName
     *     The talk_product_name
     */
    public void setTalkProductName(String talkProductName) {
        this.talkProductName = talkProductName;
    }

    /**
     *
     * @return
     *     The talkCreateTimeFmt
     */
    public String getTalkCreateTimeFmt() {
        return talkCreateTimeFmt;
    }

    /**
     *
     * @param talkCreateTimeFmt
     *     The talk_create_time_fmt
     */
    public void setTalkCreateTimeFmt(String talkCreateTimeFmt) {
        this.talkCreateTimeFmt = talkCreateTimeFmt;
    }

    /**
     *
     * @return
     *     The talkMessage
     */
    public String getTalkMessage() {
        return talkMessage;
    }

    public Spanned getTalkMessageSpanned() {
        return Html.fromHtml(talkMessage);
    }

    /**
     *
     * @param talkMessage
     *     The talk_message
     */
    public void setTalkMessage(String talkMessage) {
        this.talkMessage = talkMessage;
    }

    /**
     *
     * @return
     *     The talkFollowStatus
     */
    public int getTalkFollowStatus() {
        return talkFollowStatus;
    }

    /**
     *
     * @param talkFollowStatus
     *     The talk_follow_status
     */
    public void setTalkFollowStatus(int talkFollowStatus) {
        this.talkFollowStatus = talkFollowStatus;
    }

    /**
     *
     * @return
     *     The talkTotalComment
     */
    public String getTalkTotalComment() {
        return talkTotalComment;
    }

    /**
     *
     * @param talkTotalComment
     *     The talk_total_comment
     */
    public void setTalkTotalComment(String talkTotalComment) {
        this.talkTotalComment = talkTotalComment;
    }

    /**
     *
     * @return
     *     The talkUserLabel
     */
    public String getTalkUserLabel() {
        return talkUserLabel;
    }

    /**
     *
     * @param talkUserLabel
     *     The talk_user_label
     */
    public void setTalkUserLabel(String talkUserLabel) {
        this.talkUserLabel = talkUserLabel;
    }

    /**
     *
     * @return
     *     The talkUserLabelId
     */
    public int getTalkUserLabelId() {
        return talkUserLabelId;
    }

    /**
     *
     * @param talkUserLabelId
     *     The talk_user_label_id
     */
    public void setTalkUserLabelId(int talkUserLabelId) {
        this.talkUserLabelId = talkUserLabelId;
    }

    /**
     *
     * @return
     *     The talkProductId
     */
    public String getTalkProductId() {
        return talkProductId;
    }

    /**
     *
     * @param talkProductId
     *     The talk_product_id
     */
    public void setTalkProductId(String talkProductId) {
        this.talkProductId = talkProductId;
    }

    /**
     *
     * @return
     *     The talkProductImage
     */
    public String getTalkProductImage() {
        return talkProductImage;
    }

    /**
     *
     * @param talkProductImage
     *     The talk_product_image
     */
    public void setTalkProductImage(String talkProductImage) {
        this.talkProductImage = talkProductImage;
    }

    /**
     *
     * @return
     *     The talkUserId
     */
    public String getTalkUserId() {
        return talkUserId;
    }

    /**
     *
     * @param talkUserId
     *     The talk_user_id
     */
    public void setTalkUserId(String talkUserId) {
        this.talkUserId = talkUserId;
    }


    protected InboxTalk(Parcel in) {
        talkShopId = in.readString();
        talkUserImage = in.readString();
        talkProductStatus = in.readString();
        talkCreateTime = in.readString();
        talkReadStatus = in.readInt();
        talkUserName = in.readString();
        talkUserUrl = in.readString();
        talkId = in.readString();
        talkUserReputation = (TalkUserReputation) in.readValue(TalkUserReputation.class.getClassLoader());
        talkInboxId = in.readString();
        talkOwn = in.readInt();
        talkProductName = in.readString();
        talkCreateTimeFmt = in.readString();
        talkMessage = in.readString();
        talkFollowStatus = in.readInt();
        talkTotalComment = in.readString();
        talkUserLabel = in.readString();
        talkUserLabelId = in.readInt();
        talkProductId = in.readString();
        talkProductImage = in.readString();
        talkUserId = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(talkShopId);
        dest.writeString(talkUserImage);
        dest.writeString(talkProductStatus);
        dest.writeString(talkCreateTime);
        dest.writeInt(talkReadStatus);
        dest.writeString(talkUserName);
        dest.writeString(talkUserUrl);
        dest.writeString(talkId);
        dest.writeValue(talkUserReputation);
        dest.writeString(talkInboxId);
        dest.writeInt(talkOwn);
        dest.writeString(talkProductName);
        dest.writeString(talkCreateTimeFmt);
        dest.writeString(talkMessage);
        dest.writeInt(talkFollowStatus);
        dest.writeString(talkTotalComment);
        dest.writeString(talkUserLabel);
        dest.writeInt(talkUserLabelId);
        dest.writeString(talkProductId);
        dest.writeString(talkProductImage);
        dest.writeString(talkUserId);
    }

    @SuppressWarnings("unused")
    public static final Creator<InboxTalk> CREATOR = new Creator<InboxTalk>() {
        @Override
        public InboxTalk createFromParcel(Parcel in) {
            return new InboxTalk(in);
        }

        @Override
        public InboxTalk[] newArray(int size) {
            return new InboxTalk[size];
        }
    };
}