package com.tokopedia.core.product.model.productdetail.discussion;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hangnadi on 8/21/17.
 */

public class Talk {

    @SerializedName("talk_message")
    @Expose
    private String talkMessage;
    @SerializedName("talk_inbox_id")
    @Expose
    private int talkInboxId;
    @SerializedName("talk_user_url")
    @Expose
    private String talkUserUrl;
    @SerializedName("talk_product_name")
    @Expose
    private String talkProductName;
    @SerializedName("talk_product_image")
    @Expose
    private String talkProductImage;
    @SerializedName("talk_total_comment")
    @Expose
    private int talkTotalComment;
    @SerializedName("talk_user_name")
    @Expose
    private String talkUserName;
    @SerializedName("talk_user_image")
    @Expose
    private String talkUserImage;
    @SerializedName("talk_follow_status")
    @Expose
    private int talkFollowStatus;
    @SerializedName("talk_user_label")
    @Expose
    private String talkUserLabel;
    @SerializedName("talk_product_id")
    @Expose
    private String talkProductId;
    @SerializedName("talk_read_status")
    @Expose
    private int talkReadStatus;
    @SerializedName("talk_user_id")
    @Expose
    private String talkUserId;
    @SerializedName("talk_create_time_list")
    @Expose
    private TalkCreateTimeList talkCreateTimeList;
    @SerializedName("talk_own")
    @Expose
    private int talkOwn;
    @SerializedName("talk_create_time_fmt")
    @Expose
    private String talkCreateTimeFmt;
    @SerializedName("talk_shop_id")
    @Expose
    private String talkShopId;
    @SerializedName("talk_user_label_id")
    @Expose
    private int talkUserLabelId;
    @SerializedName("talk_product_status")
    @Expose
    private int talkProductStatus;
    @SerializedName("talk_id")
    @Expose
    private String talkId;
    @SerializedName("talk_user_reputation")
    @Expose
    private TalkUserReputation talkUserReputation;

    public String getTalkMessage() {
        return talkMessage;
    }

    public void setTalkMessage(String talkMessage) {
        this.talkMessage = talkMessage;
    }

    public int getTalkInboxId() {
        return talkInboxId;
    }

    public void setTalkInboxId(int talkInboxId) {
        this.talkInboxId = talkInboxId;
    }

    public String getTalkUserUrl() {
        return talkUserUrl;
    }

    public void setTalkUserUrl(String talkUserUrl) {
        this.talkUserUrl = talkUserUrl;
    }

    public String getTalkProductName() {
        return talkProductName;
    }

    public void setTalkProductName(String talkProductName) {
        this.talkProductName = talkProductName;
    }

    public String getTalkProductImage() {
        return talkProductImage;
    }

    public void setTalkProductImage(String talkProductImage) {
        this.talkProductImage = talkProductImage;
    }

    public int getTalkTotalComment() {
        return talkTotalComment;
    }

    public void setTalkTotalComment(int talkTotalComment) {
        this.talkTotalComment = talkTotalComment;
    }

    public String getTalkUserName() {
        return talkUserName;
    }

    public void setTalkUserName(String talkUserName) {
        this.talkUserName = talkUserName;
    }

    public String getTalkUserImage() {
        return talkUserImage;
    }

    public void setTalkUserImage(String talkUserImage) {
        this.talkUserImage = talkUserImage;
    }

    public int getTalkFollowStatus() {
        return talkFollowStatus;
    }

    public void setTalkFollowStatus(int talkFollowStatus) {
        this.talkFollowStatus = talkFollowStatus;
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

    public int getTalkReadStatus() {
        return talkReadStatus;
    }

    public void setTalkReadStatus(int talkReadStatus) {
        this.talkReadStatus = talkReadStatus;
    }

    public String getTalkUserId() {
        return talkUserId;
    }

    public void setTalkUserId(String talkUserId) {
        this.talkUserId = talkUserId;
    }

    public int getTalkOwn() {
        return talkOwn;
    }

    public void setTalkOwn(int talkOwn) {
        this.talkOwn = talkOwn;
    }

    public String getTalkCreateTimeFmt() {
        return talkCreateTimeFmt;
    }

    public void setTalkCreateTimeFmt(String talkCreateTimeFmt) {
        this.talkCreateTimeFmt = talkCreateTimeFmt;
    }

    public String getTalkShopId() {
        return talkShopId;
    }

    public void setTalkShopId(String talkShopId) {
        this.talkShopId = talkShopId;
    }

    public int getTalkUserLabelId() {
        return talkUserLabelId;
    }

    public void setTalkUserLabelId(int talkUserLabelId) {
        this.talkUserLabelId = talkUserLabelId;
    }

    public int getTalkProductStatus() {
        return talkProductStatus;
    }

    public void setTalkProductStatus(int talkProductStatus) {
        this.talkProductStatus = talkProductStatus;
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

    public TalkCreateTimeList getTalkCreateTimeList() {
        return talkCreateTimeList;
    }

    public void setTalkCreateTimeList(TalkCreateTimeList talkCreateTimeList) {
        this.talkCreateTimeList = talkCreateTimeList;
    }
}
