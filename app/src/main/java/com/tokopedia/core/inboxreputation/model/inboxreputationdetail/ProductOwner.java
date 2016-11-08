
package com.tokopedia.core.inboxreputation.model.inboxreputationdetail;

import android.text.Html;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@org.parceler.Parcel
public class ProductOwner {

    @SerializedName("user_label_id")
    @Expose
    String userLabelId;
    @SerializedName("shop_id")
    @Expose
    String shopId;
    @SerializedName("user_url")
    @Expose
    String userUrl;
    @SerializedName("full_name")
    @Expose
    String fullName;
    @SerializedName("shop_name")
    @Expose
    String shopName;
    @SerializedName("shop_url")
    @Expose
    String shopUrl;
    @SerializedName("shop_img")
    @Expose
    String shopImg;
    @SerializedName("user_img")
    @Expose
    String userImg;
    @SerializedName("user_label")
    @Expose
    String userLabel;
    @SerializedName("user_id")
    @Expose
    String userId;
    @SerializedName("shop_reputation_badge")
    @Expose
    String shopReputationBadge;
    @SerializedName("shop_reputation_score")
    @Expose
    String shopReputationScore;

    /**
     * 
     * @return
     *     The userLabelId
     */
    public String getUserLabelId() {
        return userLabelId;
    }

    /**
     * 
     * @param userLabelId
     *     The user_label_id
     */
    public void setUserLabelId(String userLabelId) {
        this.userLabelId = userLabelId;
    }

    /**
     * 
     * @return
     *     The shopId
     */
    public String getShopId() {
        return shopId;
    }

    /**
     * 
     * @param shopId
     *     The shop_id
     */
    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    /**
     * 
     * @return
     *     The userUrl
     */
    public String getUserUrl() {
        return userUrl;
    }

    /**
     * 
     * @param userUrl
     *     The user_url
     */
    public void setUserUrl(String userUrl) {
        this.userUrl = userUrl;
    }

    /**
     * 
     * @return
     *     The fullName
     */
    public String getFullName() {
        return Html.fromHtml(fullName).toString();
    }

    /**
     * 
     * @param fullName
     *     The full_name
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * 
     * @return
     *     The shopName
     */
    public String getShopName() {
        return Html.fromHtml(shopName).toString();
    }

    /**
     * 
     * @param shopName
     *     The shop_name
     */
    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    /**
     * 
     * @return
     *     The shopUrl
     */
    public String getShopUrl() {
        return shopUrl;
    }

    /**
     * 
     * @param shopUrl
     *     The shop_url
     */
    public void setShopUrl(String shopUrl) {
        this.shopUrl = shopUrl;
    }

    /**
     * 
     * @return
     *     The shopImg
     */
    public String getShopImg() {
        return shopImg;
    }

    /**
     * 
     * @param shopImg
     *     The shop_img
     */
    public void setShopImg(String shopImg) {
        this.shopImg = shopImg;
    }

    /**
     * 
     * @return
     *     The userImg
     */
    public String getUserImg() {
        return userImg;
    }

    /**
     * 
     * @param userImg
     *     The user_img
     */
    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    /**
     * 
     * @return
     *     The userLabel
     */
    public String getUserLabel() {
        return userLabel;
    }

    /**
     * 
     * @param userLabel
     *     The user_label
     */
    public void setUserLabel(String userLabel) {
        this.userLabel = userLabel;
    }

    /**
     * 
     * @return
     *     The userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 
     * @param userId
     *     The user_id
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * 
     * @return
     *     The shopReputationBadge
     */
    public String getShopReputationBadge() {
        return shopReputationBadge;
    }

    /**
     * 
     * @param shopReputationBadge
     *     The shop_reputation_badge
     */
    public void setShopReputationBadge(String shopReputationBadge) {
        this.shopReputationBadge = shopReputationBadge;
    }

    /**
     * 
     * @return
     *     The shopReputationScore
     */
    public String getShopReputationScore() {
        return shopReputationScore;
    }

    /**
     * 
     * @param shopReputationScore
     *     The shop_reputation_score
     */
    public void setShopReputationScore(String shopReputationScore) {
        this.shopReputationScore = shopReputationScore;
    }

}
