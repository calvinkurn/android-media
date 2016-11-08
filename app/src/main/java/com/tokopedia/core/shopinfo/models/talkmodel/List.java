
package com.tokopedia.core.shopinfo.models.talkmodel;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.Html;
import android.text.Spanned;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.var.RecyclerViewItem;

public class List extends RecyclerViewItem implements Parcelable{

    @SerializedName("talk_shop_id")
    @Expose
    public String talkShopId;
    @SerializedName("talk_user_image")
    @Expose
    public String talkUserImage;
    @SerializedName("talk_product_status")
    @Expose
    public String talkProductStatus;
    @SerializedName("talk_create_time")
    @Expose
    public String talkCreateTime;
    @SerializedName("talk_read_status")
    @Expose
    public int talkReadStatus;
    @SerializedName("talk_user_name")
    @Expose
    public String talkUserName;
    @SerializedName("talk_user_url")
    @Expose
    public String talkUserUrl;
    @SerializedName("talk_id")
    @Expose
    public String talkId;
    @SerializedName("talk_user_reputation")
    @Expose
    public TalkUserReputation talkUserReputation;
    @SerializedName("talk_inbox_id")
    @Expose
    public int talkInboxId;
    @SerializedName("talk_own")
    @Expose
    public String talkOwn;
    @SerializedName("talk_product_name")
    @Expose
    public String talkProductName;
    @SerializedName("talk_message")
    @Expose
    public String talkMessage;
    @SerializedName("talk_follow_status")
    @Expose
    public int talkFollowStatus;
    @SerializedName("talk_total_comment")
    @Expose
    public String talkTotalComment;
    @SerializedName("talk_user_label")
    @Expose
    public String talkUserLabel;
    @SerializedName("talk_product_id")
    @Expose
    public String talkProductId;
    @SerializedName("talk_user_label_id")
    @Expose
    public int talkUserLabelId;
    @SerializedName("talk_product_image")
    @Expose
    public String talkProductImage;
    @SerializedName("talk_user_id")
    @Expose
    public String talkUserId;

    public String getTalkShopId() {
        return talkShopId;
    }

    public void setTalkShopId(String talkShopId) {
        this.talkShopId = talkShopId;
    }

    public String getTalkUserImage() {
        return talkUserImage;
    }

    public void setTalkUserImage(String talkUserImage) {
        this.talkUserImage = talkUserImage;
    }

    public String getTalkProductStatus() {
        return talkProductStatus;
    }

    public void setTalkProductStatus(String talkProductStatus) {
        this.talkProductStatus = talkProductStatus;
    }

    public String getTalkCreateTime() {
        return talkCreateTime;
    }

    public void setTalkCreateTime(String talkCreateTime) {
        this.talkCreateTime = talkCreateTime;
    }

    public int getTalkReadStatus() {
        return talkReadStatus;
    }

    public void setTalkReadStatus(int talkReadStatus) {
        this.talkReadStatus = talkReadStatus;
    }

    public String getTalkUserName() {
        return talkUserName;
    }

    public void setTalkUserName(String talkUserName) {
        this.talkUserName = talkUserName;
    }

    public String getTalkUserUrl() {
        return talkUserUrl;
    }

    public void setTalkUserUrl(String talkUserUrl) {
        this.talkUserUrl = talkUserUrl;
    }

    public String getTalkId() {
        return talkId;
    }

    public void setTalkId(String talkId) {
        this.talkId = talkId;
    }

    public TalkUserReputation getTalkUserReputation() {
        return talkUserReputation;
    }

    public void setTalkUserReputation(TalkUserReputation talkUserReputation) {
        this.talkUserReputation = talkUserReputation;
    }

    public int getTalkInboxId() {
        return talkInboxId;
    }

    public void setTalkInboxId(int talkInboxId) {
        this.talkInboxId = talkInboxId;
    }

    public String getTalkOwn() {
        return talkOwn;
    }

    public void setTalkOwn(String talkOwn) {
        this.talkOwn = talkOwn;
    }

    public String getTalkProductName() {
        return talkProductName;
    }

    public void setTalkProductName(String talkProductName) {
        this.talkProductName = talkProductName;
    }

    public String getTalkMessage() {
        return talkMessage;
    }

    public Spanned getTalkMessageSpanned() {
        return Html.fromHtml(talkMessage);
    }

    public void setTalkMessage(String talkMessage) {
        this.talkMessage = talkMessage;
    }

    public int getTalkFollowStatus() {
        return talkFollowStatus;
    }

    public void setTalkFollowStatus(int talkFollowStatus) {
        this.talkFollowStatus = talkFollowStatus;
    }

    public String getTalkTotalComment() {
        return talkTotalComment;
    }

    public void setTalkTotalComment(String talkTotalComment) {
        this.talkTotalComment = talkTotalComment;
    }

    public String getTalkUserLabel() {
        return talkUserLabel;
    }

    public void setTalkUserLabel(String talkUserLabel) {
        this.talkUserLabel = talkUserLabel;
    }

    public String getTalkProductId() {
        return talkProductId;
    }

    public void setTalkProductId(String talkProductId) {
        this.talkProductId = talkProductId;
    }

    public int getTalkUserLabelId() {
        return talkUserLabelId;
    }

    public void setTalkUserLabelId(int talkUserLabelId) {
        this.talkUserLabelId = talkUserLabelId;
    }

    public String getTalkProductImage() {
        return talkProductImage;
    }

    public void setTalkProductImage(String talkProductImage) {
        this.talkProductImage = talkProductImage;
    }

    public String getTalkUserId() {
        return talkUserId;
    }

    public void setTalkUserId(String talkUserId) {
        this.talkUserId = talkUserId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.talkShopId);
        dest.writeString(this.talkUserImage);
        dest.writeString(this.talkProductStatus);
        dest.writeString(this.talkCreateTime);
        dest.writeInt(this.talkReadStatus);
        dest.writeString(this.talkUserName);
        dest.writeString(this.talkUserUrl);
        dest.writeString(this.talkId);
        dest.writeParcelable(this.talkUserReputation, flags);
        dest.writeInt(this.talkInboxId);
        dest.writeString(this.talkOwn);
        dest.writeString(this.talkProductName);
        dest.writeString(this.talkMessage);
        dest.writeInt(this.talkFollowStatus);
        dest.writeString(this.talkTotalComment);
        dest.writeString(this.talkUserLabel);
        dest.writeString(this.talkProductId);
        dest.writeInt(this.talkUserLabelId);
        dest.writeString(this.talkProductImage);
        dest.writeString(this.talkUserId);
    }

    public List() {
    }

    protected List(Parcel in) {
        this.talkShopId = in.readString();
        this.talkUserImage = in.readString();
        this.talkProductStatus = in.readString();
        this.talkCreateTime = in.readString();
        this.talkReadStatus = in.readInt();
        this.talkUserName = in.readString();
        this.talkUserUrl = in.readString();
        this.talkId = in.readString();
        this.talkUserReputation = in.readParcelable(TalkUserReputation.class.getClassLoader());
        this.talkInboxId = in.readInt();
        this.talkOwn = in.readString();
        this.talkProductName = in.readString();
        this.talkMessage = in.readString();
        this.talkFollowStatus = in.readInt();
        this.talkTotalComment = in.readString();
        this.talkUserLabel = in.readString();
        this.talkProductId = in.readString();
        this.talkUserLabelId = in.readInt();
        this.talkProductImage = in.readString();
        this.talkUserId = in.readString();
    }

    public static final Creator<List> CREATOR = new Creator<List>() {
        @Override
        public List createFromParcel(Parcel source) {
            return new List(source);
        }

        @Override
        public List[] newArray(int size) {
            return new List[size];
        }
    };
}
