
package com.tokopedia.core.inboxreputation.model.inboxreputation;

import android.text.Html;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@org.parceler.Parcel
public class InboxReputationItem {

    @SerializedName("reviewee_name")
    @Expose
    String revieweeName;
    @SerializedName("reviewee_picture")
    @Expose
    String revieweeImageUrl;
    @SerializedName("reviewee_role")
    @Expose
    int revieweeRole;
    @SerializedName("user_reputation")
    @Expose
    UserReputation userReputation;
    @SerializedName("reputation_score")
    @Expose
    String reputationScore;
    @SerializedName("shop_badge_level")
    @Expose
    ShopBadgeLevel shopBadgeLevel;
    @SerializedName("read_status")
    @Expose
    int isRead;
    @SerializedName("review_status_description")
    @Expose
    String reviewStatusDescription;
    @SerializedName("is_edited")
    @Expose
    int isEdited;
    @SerializedName("create_time_fmt_ws")
    @Expose
    String createTime;
    @SerializedName("reputation_days_left_fmt")
    @Expose
    String reputationDaysLeft;
    @SerializedName("shop_id")
    @Expose
    String shopId;
    @SerializedName("updated_reputation_review")
    @Expose
    int updatedReputationReview;
    @SerializedName("order_id")
    @Expose
    int orderId;
    @SerializedName("buyer_id")
    @Expose
    String buyerId;
    @SerializedName("role")
    @Expose
    int role;
    @SerializedName("reputation_inbox_id")
    @Expose
    String reputationInboxId;
    @SerializedName("reputation_id")
    @Expose
    String reputationId;
    @SerializedName("invoice_uri")
    @Expose
    String invoiceUri;
    @SerializedName("invoice_ref_num")
    @Expose
    String invoiceRefNum;
    @SerializedName("my_score_image")
    @Expose
    String reviewerScore;
    @SerializedName("their_score_image")
    @Expose
    String revieweeScore;
    @SerializedName("is_reviewee_score_edited")
    @Expose
    int isRevieweeScoreEdited;
    @SerializedName("is_reviewee_score_read")
    @Expose
    int isRevieweeScoreRead;
    @SerializedName("is_reviewer_score_edited")
    @Expose
    int isReviewerScoreEdited;
    @SerializedName("show_reputation_day")
    @Expose
    int canShowReputationDay;
    @SerializedName("notif_received_smiley")
    @Expose
    int isReviewerSmileyRead;
    @SerializedName("reputation_progress")
    @Expose
    String reputationProgress;
    @SerializedName("show_bookmark")
    @Expose
    int isShowBookmark;

    /**
     * @return The revieweeName
     */
    public String getRevieweeName() {
        return Html.fromHtml(revieweeName).toString();
    }

    /**
     * @param revieweeName The reviewee_name
     */
    public void setRevieweeName(String revieweeName) {
        this.revieweeName = revieweeName;
    }

    /**
     * @return The revieweeImageUrl
     */
    public String getRevieweeImageUrl() {
        return revieweeImageUrl;
    }

    /**
     * @param revieweeImageUrl The reviewee_image_url
     */
    public void setRevieweeImageUrl(String revieweeImageUrl) {
        this.revieweeImageUrl = revieweeImageUrl;
    }

    /**
     * @return The revieweeRole
     */
    public int getRevieweeRole() {
        return revieweeRole;
    }

    /**
     * @param revieweeRole The reviewee_role
     */
    public void setRevieweeRole(int revieweeRole) {
        this.revieweeRole = revieweeRole;
    }

    /**
     * @return The userReputation
     */
    public UserReputation getUserReputation() {
        return userReputation;
    }

    /**
     * @param userReputation The user_reputation
     */
    public void setUserReputation(UserReputation userReputation) {
        this.userReputation = userReputation;
    }

    /**
     * @return The reputationScore
     */
    public String getReputationScore() {
        return reputationScore;
    }

    /**
     * @param reputationScore The reputation_score
     */
    public void setReputationScore(String reputationScore) {
        this.reputationScore = reputationScore;
    }

    /**
     * @return The shopBadgeLevel
     */
    public ShopBadgeLevel getShopBadgeLevel() {
        return shopBadgeLevel;
    }

    /**
     * @param shopBadgeLevel The shop_badge_level
     */
    public void setShopBadgeLevel(ShopBadgeLevel shopBadgeLevel) {
        this.shopBadgeLevel = shopBadgeLevel;
    }

    /**
     * @return The isRead
     */
    public boolean getIsRead() {
        return isRead == 2;
    }

    /**
     * @param isRead The is_read
     */
    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    /**
     * @return The reviewStatusDescription
     */
    public String getReviewStatusDescription() {
        return Html.fromHtml(reviewStatusDescription).toString();
    }

    /**
     * @param reviewStatusDescription The review_status_description
     */
    public void setReviewStatusDescription(String reviewStatusDescription) {
        this.reviewStatusDescription = reviewStatusDescription;
    }

    /**
     * @return The reputationDaysLeft
     */
    public String getReputationDaysLeft() {
        return reputationDaysLeft;
    }

    /**
     * @param reputationDaysLeft The reputation_days_left
     */
    public void setReputationDaysLeft(String reputationDaysLeft) {
        this.reputationDaysLeft = reputationDaysLeft;
    }

    /**
     * @return The shopId
     */
    public String getShopId() {
        return shopId;
    }

    /**
     * @param shopId The shop_id
     */
    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    /**
     * @return The updatedReputationReview
     */
    public int getUpdatedReputationReview() {
        return updatedReputationReview;
    }

    /**
     * @param updatedReputationReview The updated_reputation_review
     */
    public void setUpdatedReputationReview(int updatedReputationReview) {
        this.updatedReputationReview = updatedReputationReview;
    }

    /**
     * @return The orderId
     */
    public int getOrderId() {
        return orderId;
    }

    /**
     * @param orderId The order_id
     */
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    /**
     * @return The buyerId
     */
    public String getBuyerId() {
        return buyerId;
    }

    /**
     * @param buyerId The buyer_id
     */
    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    /**
     * @return The role
     */
    public int getRole() {
        return role;
    }

    /**
     * @param role The role
     */
    public void setRole(int role) {
        this.role = role;
    }

    /**
     * @return The reputationInboxId
     */
    public String getReputationInboxId() {
        return reputationInboxId;
    }

    /**
     * @param reputationInboxId The reputation_inbox_id
     */
    public void setReputationInboxId(String reputationInboxId) {
        this.reputationInboxId = reputationInboxId;
    }


    /**
     * @return The reputationId
     */
    public String getReputationId() {
        return reputationId;
    }

    /**
     * @param reputationId The reputation_id
     */
    public void setReputationId(String reputationId) {
        this.reputationId = reputationId;
    }

    /**
     * @return The invoiceUri
     */
    public String getInvoiceUri() {
        return invoiceUri;
    }

    /**
     * @param invoiceUri The invoice_uri
     */
    public void setInvoiceUri(String invoiceUri) {
        this.invoiceUri = invoiceUri;
    }

    /**
     * @return The invoiceRefNum
     */
    public String getInvoiceRefNum() {
        return invoiceRefNum;
    }

    /**
     * @param invoiceRefNum The invoice_ref_num
     */
    public void setInvoiceRefNum(String invoiceRefNum) {
        this.invoiceRefNum = invoiceRefNum;
    }

    /**
     * @return The reviewerScore
     */
    public String getReviewerScore() {
        return reviewerScore;
    }

    /**
     * @param reviewerScore The reviewer_score
     */
    public void setReviewerScore(String reviewerScore) {
        this.reviewerScore = reviewerScore;
    }

    /**
     * @return The revieweeScore
     */
    public String getRevieweeScore() {
        return revieweeScore;
    }

    /**
     * @param revieweeScore The reviewee_score
     */
    public void setRevieweeScore(String revieweeScore) {
        this.revieweeScore = revieweeScore;
    }

    /**
     * @return The isRevieweeScoreEdited
     */
    public boolean getIsRevieweeScoreEdited() {
        return isRevieweeScoreEdited == 1;
    }

    /**
     * @param isRevieweeScoreEdited The is_reviewee_score_edited
     */
    public void setIsRevieweeScoreEdited(int isRevieweeScoreEdited) {
        this.isRevieweeScoreEdited = isRevieweeScoreEdited;
    }

    /**
     * @return The isRevieweeScoreRead
     */
    public boolean getIsRevieweeScoreRead() {
        return isRevieweeScoreRead == 1;
    }

    /**
     * @param isRevieweeScoreRead The is_reviewee_score_read
     */
    public void setIsRevieweeScoreRead(int isRevieweeScoreRead) {
        this.isRevieweeScoreRead = isRevieweeScoreRead;
    }

    /**
     * @return The isReviewerScoreEdited
     */
    public boolean getIsReviewerScoreEdited() {
        return isReviewerScoreEdited == 1;
    }

    /**
     * @param isReviewerScoreEdited The is_reviewer_score_edited
     */
    public void setIsReviewerScoreEdited(int isReviewerScoreEdited) {
        this.isReviewerScoreEdited = isReviewerScoreEdited;
    }

    /**
     * @return The isReviewerSmileyRead
     */
    public Boolean isSmileyReviewerRead() {
        return isReviewerSmileyRead == 0;
    }

    /**
     * @param isReviewerSmileyRead The is_smiley_reviewer_read
     */
    public void setIsReviewerSmileyRead(int isReviewerSmileyRead) {
        this.isReviewerSmileyRead = isReviewerSmileyRead;
    }

    /**
     * @param isEdited The is_edited
     */
    public void setIsEdited(int isEdited) {
        this.isEdited = isEdited;
    }

    /**
     * @return The isEdited
     */
    public boolean getIsEdited() {
        return isEdited == 1;
    }

    /**
     * @param createTime The create_time_fmt_ws
     */
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    /**
     * @return The createTime
     */
    public String getCreateTime() {
        return createTime;
    }

    /**
     * @param canShowReputationDay The show_reputation_day
     */
    public void setCanShowReputationDay(int canShowReputationDay) {
        this.canShowReputationDay = canShowReputationDay;
    }

    /**
     * @return The canShowReputationDay
     */
    public boolean getCanShowReputationDay() {
        return canShowReputationDay == 1;
    }


    /**
     * @return The orderId
     */
    public int getReputationProgress() {
        return reputationProgress.equals("") ? 0 : Integer.valueOf(reputationProgress);
    }

    /**
     * @param reputationProgress The reputation_progress
     */
    public void setReputationProgress(String reputationProgress) {
        this.reputationProgress = reputationProgress;
    }

    public String getLabel() {
        return generateLabel(getRevieweeRole());
    }

    String generateLabel(int args) {
        if (args == 1) {
            return "Pembeli";
        } else {
            return "Penjual";
        }
    }

    public boolean isShowBookmark() {
        return isShowBookmark == 1;
    }
}
