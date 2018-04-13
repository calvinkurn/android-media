
package com.tokopedia.core.shopinfo.models.talkmodel;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.Html;
import android.text.Spanned;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.util.MethodChecker;

public class ShopTalk implements Parcelable {

    public static final int IS_FOLLOWING = 1;

    @SerializedName("talk_create_time")
    @Expose
    private String talkCreateTime;
    @SerializedName("talk_create_time_fmt")
    @Expose
    private String talkCreateTimeFmt;
    @SerializedName("talk_follow_status")
    @Expose
    private int talkFollowStatus;
    @SerializedName("talk_id")
    @Expose
    private String talkId;
    @SerializedName("talk_inbox_id")
    @Expose
    private int talkInboxId;
    @SerializedName("talk_message")
    @Expose
    private String talkMessage;
    @SerializedName("talk_own")
    @Expose
    private int talkOwn;
    @SerializedName("talk_product_id")
    @Expose
    private int talkProductId;
    @SerializedName("talk_product_image")
    @Expose
    private String talkProductImage;
    @SerializedName("talk_product_name")
    @Expose
    private String talkProductName;
    @SerializedName("talk_product_status")
    @Expose
    private int talkProductStatus;
    @SerializedName("talk_read_status")
    @Expose
    private int talkReadStatus;
    @SerializedName("talk_shop_id")
    @Expose
    private String talkShopId;
    @SerializedName("talk_total_comment")
    @Expose
    private String talkTotalComment;
    @SerializedName("talk_user_id")
    @Expose
    private String talkUserId;
    @SerializedName("talk_user_image")
    @Expose
    private String talkUserImage;
    @SerializedName("talk_user_label")
    @Expose
    private String talkUserLabel;
    @SerializedName("talk_user_label_id")
    @Expose
    private int talkUserLabelId;
    @SerializedName("talk_user_name")
    @Expose
    private String talkUserName;
    @SerializedName("talk_user_reputation")
    @Expose
    private TalkUserReputation talkUserReputation;
    @SerializedName("talk_user_url")
    @Expose
    private String talkUserUrl;
    private int position;
    private String report;

    protected ShopTalk(Parcel in) {
        talkCreateTime = in.readString();
        talkCreateTimeFmt = in.readString();
        talkFollowStatus = in.readInt();
        talkId = in.readString();
        talkInboxId = in.readInt();
        talkMessage = in.readString();
        talkOwn = in.readInt();
        talkProductId = in.readInt();
        talkProductImage = in.readString();
        talkProductName = in.readString();
        talkProductStatus = in.readInt();
        talkReadStatus = in.readInt();
        talkShopId = in.readString();
        talkTotalComment = in.readString();
        talkUserId = in.readString();
        talkUserImage = in.readString();
        talkUserLabel = in.readString();
        talkUserLabelId = in.readInt();
        talkUserName = in.readString();
        talkUserReputation = in.readParcelable(TalkUserReputation.class.getClassLoader());
        talkUserUrl = in.readString();
        position = in.readInt();
        report = in.readString();
    }

    public ShopTalk(){}

    public static final Creator<ShopTalk> CREATOR = new Creator<ShopTalk>() {
        @Override
        public ShopTalk createFromParcel(Parcel in) {
            return new ShopTalk(in);
        }

        @Override
        public ShopTalk[] newArray(int size) {
            return new ShopTalk[size];
        }
    };

    /**
     * @return The talkCreateTime
     */
    public String getTalkCreateTime() {
        return talkCreateTime;
    }

    /**
     * @param talkCreateTime The talk_create_time
     */
    public void setTalkCreateTime(String talkCreateTime) {
        this.talkCreateTime = talkCreateTime;
    }

    /**
     * @return The talkCreateTimeFmt
     */
    public String getTalkCreateTimeFmt() {
        return talkCreateTimeFmt;
    }

    /**
     * @param talkCreateTimeFmt The talk_create_time_fmt
     */
    public void setTalkCreateTimeFmt(String talkCreateTimeFmt) {
        this.talkCreateTimeFmt = talkCreateTimeFmt;
    }

    /**
     * @return The talkFollowStatus
     */
    public int getTalkFollowStatus() {
        return talkFollowStatus;
    }

    /**
     * @param talkFollowStatus The talk_follow_status
     */
    public void setTalkFollowStatus(int talkFollowStatus) {
        this.talkFollowStatus = talkFollowStatus;
    }

    /**
     * @return The talkId
     */
    public String getTalkId() {
        return talkId;
    }

    /**
     * @param talkId The talk_id
     */
    public void setTalkId(String talkId) {
        this.talkId = talkId;
    }

    /**
     * @return The talkInboxId
     */
    public int getTalkInboxId() {
        return talkInboxId;
    }

    /**
     * @param talkInboxId The talk_inbox_id
     */
    public void setTalkInboxId(int talkInboxId) {
        this.talkInboxId = talkInboxId;
    }

    /**
     * @return The talkMessage
     */
    public String getTalkMessage() {
        return MethodChecker.fromHtml(talkMessage).toString();
    }

    /**
     * @param talkMessage The talk_message
     */
    public void setTalkMessage(String talkMessage) {
        this.talkMessage = talkMessage;
    }

    /**
     * @return The talkOwn
     */
    public int getTalkOwn() {
        return talkOwn;
    }

    /**
     * @param talkOwn The talk_own
     */
    public void setTalkOwn(int talkOwn) {
        this.talkOwn = talkOwn;
    }

    /**
     * @return The talkProductId
     */
    public int getTalkProductId() {
        return talkProductId;
    }

    /**
     * @param talkProductId The talk_product_id
     */
    public void setTalkProductId(int talkProductId) {
        this.talkProductId = talkProductId;
    }

    /**
     * @return The talkProductImage
     */
    public String getTalkProductImage() {
        return talkProductImage;
    }

    /**
     * @param talkProductImage The talk_product_image
     */
    public void setTalkProductImage(String talkProductImage) {
        this.talkProductImage = talkProductImage;
    }

    /**
     * @return The talkProductName
     */
    public String getTalkProductName() {
        return MethodChecker.fromHtml(talkProductName).toString();
    }

    /**
     * @param talkProductName The talk_product_name
     */
    public void setTalkProductName(String talkProductName) {
        this.talkProductName = talkProductName;
    }

    /**
     * @return The talkProductStatus
     */
    public int getTalkProductStatus() {
        return talkProductStatus;
    }

    /**
     * @param talkProductStatus The talk_product_status
     */
    public void setTalkProductStatus(int talkProductStatus) {
        this.talkProductStatus = talkProductStatus;
    }

    /**
     * @return The talkReadStatus
     */
    public int getTalkReadStatus() {
        return talkReadStatus;
    }

    /**
     * @param talkReadStatus The talk_read_status
     */
    public void setTalkReadStatus(int talkReadStatus) {
        this.talkReadStatus = talkReadStatus;
    }

    /**
     * @return The talkShopId
     */
    public String getTalkShopId() {
        return talkShopId;
    }

    /**
     * @param talkShopId The talk_shop_id
     */
    public void setTalkShopId(String talkShopId) {
        this.talkShopId = talkShopId;
    }

    /**
     * @return The talkTotalComment
     */
    public String getTalkTotalComment() {
        return talkTotalComment;
    }

    /**
     * @param talkTotalComment The talk_total_comment
     */
    public void setTalkTotalComment(String talkTotalComment) {
        this.talkTotalComment = talkTotalComment;
    }

    /**
     * @return The talkUserId
     */
    public String getTalkUserId() {
        return talkUserId;
    }

    /**
     * @param talkUserId The talk_user_id
     */
    public void setTalkUserId(String talkUserId) {
        this.talkUserId = talkUserId;
    }

    /**
     * @return The talkUserImage
     */
    public String getTalkUserImage() {
        return talkUserImage;
    }

    /**
     * @param talkUserImage The talk_user_image
     */
    public void setTalkUserImage(String talkUserImage) {
        this.talkUserImage = talkUserImage;
    }

    /**
     * @return The talkUserLabel
     */
    public String getTalkUserLabel() {
        return talkUserLabel;
    }

    /**
     * @param talkUserLabel The talk_user_label
     */
    public void setTalkUserLabel(String talkUserLabel) {
        this.talkUserLabel = talkUserLabel;
    }

    /**
     * @return The talkUserLabelId
     */
    public int getTalkUserLabelId() {
        return talkUserLabelId;
    }

    /**
     * @param talkUserLabelId The talk_user_label_id
     */
    public void setTalkUserLabelId(int talkUserLabelId) {
        this.talkUserLabelId = talkUserLabelId;
    }

    /**
     * @return The talkUserName
     */
    public String getTalkUserName() {
        return MethodChecker.fromHtml(talkUserName).toString();
    }

    /**
     * @param talkUserName The talk_user_name
     */
    public void setTalkUserName(String talkUserName) {
        this.talkUserName = talkUserName;
    }

    /**
     * @return The talkUserReputation
     */
    public TalkUserReputation getTalkUserReputation() {
        return talkUserReputation;
    }

    /**
     * @param talkUserReputation The talk_user_reputation
     */
    public void setTalkUserReputation(TalkUserReputation talkUserReputation) {
        this.talkUserReputation = talkUserReputation;
    }

    /**
     * @return The talkUserUrl
     */
    public String getTalkUserUrl() {
        return talkUserUrl;
    }

    /**
     * @param talkUserUrl The talk_user_url
     */
    public void setTalkUserUrl(String talkUserUrl) {
        this.talkUserUrl = talkUserUrl;
    }

    public Spanned getTalkMessageSpanned() {
        return MethodChecker.fromHtml(talkMessage);
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public String getReport() {
        return report;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(talkCreateTime);
        dest.writeString(talkCreateTimeFmt);
        dest.writeInt(talkFollowStatus);
        dest.writeString(talkId);
        dest.writeInt(talkInboxId);
        dest.writeString(talkMessage);
        dest.writeInt(talkOwn);
        dest.writeInt(talkProductId);
        dest.writeString(talkProductImage);
        dest.writeString(talkProductName);
        dest.writeInt(talkProductStatus);
        dest.writeInt(talkReadStatus);
        dest.writeString(talkShopId);
        dest.writeString(talkTotalComment);
        dest.writeString(talkUserId);
        dest.writeString(talkUserImage);
        dest.writeString(talkUserLabel);
        dest.writeInt(talkUserLabelId);
        dest.writeString(talkUserName);
        dest.writeParcelable(talkUserReputation, flags);
        dest.writeString(talkUserUrl);
        dest.writeInt(position);
        dest.writeString(report);
    }
}
