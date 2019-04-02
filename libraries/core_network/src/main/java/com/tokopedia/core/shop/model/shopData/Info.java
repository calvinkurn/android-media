
package com.tokopedia.core.shop.model.shopData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Deprecated
@Parcel
public class Info {

    @SerializedName("shop_reputation_badge")
    @Expose
    String shopReputationBadge;
    @SerializedName("shop_is_owner")
    @Expose
    Integer shopIsOwner;
    @SerializedName("shop_reputation")
    @Expose
    String shopReputation;
    @SerializedName("shop_url")
    @Expose
    String shopUrl;
    @SerializedName("shop_has_terms")
    @Expose
    Integer shopHasTerms;
    @SerializedName("shop_is_allow_manage")
    @Expose
    Integer shopIsAllowManage;
    @SerializedName("shop_is_closed_note")
    @Expose
    Integer shopIsClosedNote;
    @SerializedName("shop_domain")
    @Expose
    String shopDomain;
    @SerializedName("shop_cover")
    @Expose
    String shopCover;
    @SerializedName("shop_id")
    @Expose
    String shopId;
    @SerializedName("shop_lucky")
    @Expose
    String shopLucky;
    @SerializedName("shop_location")
    @Expose
    String shopLocation;
    @SerializedName("shop_total_favorit")
    @Expose
    Integer shopTotalFavorit;
    @SerializedName("shop_status_title")
    @Expose
    String shopStatusTitle;
    @SerializedName("shop_is_closed_reason")
    @Expose
    String shopIsClosedReason;
    @SerializedName("shop_avatar")
    @Expose
    String shopAvatar;
    @SerializedName("shop_name")
    @Expose
    String shopName;
    @SerializedName("shop_status_message")
    @Expose
    String shopStatusMessage;
    @SerializedName("shop_is_gold")
    @Expose
    Integer shopIsGold;
    @SerializedName("shop_min_badge_score")
    @Expose
    Integer shopMinBadgeScore;
    @SerializedName("shop_is_closed_until")
    @Expose
    String shopIsClosedUntil;
    @SerializedName("shop_already_favorited")
    @Expose
    Integer shopAlreadyFavorited;
    @SerializedName("shop_open_since")
    @Expose
    String shopOpenSince;
    @SerializedName("shop_description")
    @Expose
    String shopDescription;
    @SerializedName("shop_tagline")
    @Expose
    String shopTagline;
    @SerializedName("shop_owner_id")
    @Expose
    Integer shopOwnerId;
    @SerializedName("shop_owner_last_login")
    @Expose
    String shopOwnerLastLogin;
    @SerializedName("shop_status")
    @Expose
    Integer shopStatus;
    @SerializedName("shop_gold_expired_time")
    @Expose
    String shopGoldExpiredTime;


    public String getShopGoldExpiredTime() {
        return shopGoldExpiredTime;
    }

    public void setShopGoldExpiredTime(String shopGoldExpiredTime) {
        this.shopGoldExpiredTime = shopGoldExpiredTime;
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
     *     The shopIsOwner
     */
    public Integer getShopIsOwner() {
        return shopIsOwner;
    }

    /**
     * 
     * @param shopIsOwner
     *     The shop_is_owner
     */
    public void setShopIsOwner(Integer shopIsOwner) {
        this.shopIsOwner = shopIsOwner;
    }

    /**
     * 
     * @return
     *     The shopReputation
     */
    public String getShopReputation() {
        return shopReputation;
    }

    /**
     * 
     * @param shopReputation
     *     The shop_reputation
     */
    public void setShopReputation(String shopReputation) {
        this.shopReputation = shopReputation;
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
     *     The shopHasTerms
     */
    public Integer getShopHasTerms() {
        return shopHasTerms;
    }

    /**
     * 
     * @param shopHasTerms
     *     The shop_has_terms
     */
    public void setShopHasTerms(Integer shopHasTerms) {
        this.shopHasTerms = shopHasTerms;
    }

    /**
     * 
     * @return
     *     The shopIsAllowManage
     */
    public Integer getShopIsAllowManage() {
        return shopIsAllowManage;
    }

    /**
     * 
     * @param shopIsAllowManage
     *     The shop_is_allow_manage
     */
    public void setShopIsAllowManage(Integer shopIsAllowManage) {
        this.shopIsAllowManage = shopIsAllowManage;
    }

    /**
     * 
     * @return
     *     The shopIsClosedNote
     */
    public Integer getShopIsClosedNote() {
        return shopIsClosedNote;
    }

    /**
     * 
     * @param shopIsClosedNote
     *     The shop_is_closed_note
     */
    public void setShopIsClosedNote(Integer shopIsClosedNote) {
        this.shopIsClosedNote = shopIsClosedNote;
    }

    /**
     * 
     * @return
     *     The shopDomain
     */
    public String getShopDomain() {
        return shopDomain;
    }

    /**
     * 
     * @param shopDomain
     *     The shop_domain
     */
    public void setShopDomain(String shopDomain) {
        this.shopDomain = shopDomain;
    }

    /**
     * 
     * @return
     *     The shopCover
     */
    public String getShopCover() {
        return shopCover;
    }

    /**
     * 
     * @param shopCover
     *     The shop_cover
     */
    public void setShopCover(String shopCover) {
        this.shopCover = shopCover;
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
     *     The shopLucky
     */
    public String getShopLucky() {
        return shopLucky;
    }

    /**
     * 
     * @param shopLucky
     *     The shop_lucky
     */
    public void setShopLucky(String shopLucky) {
        this.shopLucky = shopLucky;
    }

    /**
     * 
     * @return
     *     The shopLocation
     */
    public String getShopLocation() {
        return shopLocation;
    }

    /**
     * 
     * @param shopLocation
     *     The shop_location
     */
    public void setShopLocation(String shopLocation) {
        this.shopLocation = shopLocation;
    }

    /**
     * 
     * @return
     *     The shopTotalFavorit
     */
    public Integer getShopTotalFavorit() {
        return shopTotalFavorit;
    }

    /**
     * 
     * @param shopTotalFavorit
     *     The shop_total_favorit
     */
    public void setShopTotalFavorit(Integer shopTotalFavorit) {
        this.shopTotalFavorit = shopTotalFavorit;
    }

    /**
     * 
     * @return
     *     The shopStatusTitle
     */
    public String getShopStatusTitle() {
        return shopStatusTitle;
    }

    /**
     * 
     * @param shopStatusTitle
     *     The shop_status_title
     */
    public void setShopStatusTitle(String shopStatusTitle) {
        this.shopStatusTitle = shopStatusTitle;
    }

    /**
     * 
     * @return
     *     The shopIsClosedReason
     */
    public String getShopIsClosedReason() {
        return shopIsClosedReason;
    }

    /**
     * 
     * @param shopIsClosedReason
     *     The shop_is_closed_reason
     */
    public void setShopIsClosedReason(String shopIsClosedReason) {
        this.shopIsClosedReason = shopIsClosedReason;
    }

    /**
     * 
     * @return
     *     The shopAvatar
     */
    public String getShopAvatar() {
        return shopAvatar;
    }

    /**
     * 
     * @param shopAvatar
     *     The shop_avatar
     */
    public void setShopAvatar(String shopAvatar) {
        this.shopAvatar = shopAvatar;
    }

    /**
     * 
     * @return
     *     The shopName
     */
    public String getShopName() {
        return shopName;
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
     *     The shopStatusMessage
     */
    public String getShopStatusMessage() {
        return shopStatusMessage;
    }

    /**
     * 
     * @param shopStatusMessage
     *     The shop_status_message
     */
    public void setShopStatusMessage(String shopStatusMessage) {
        this.shopStatusMessage = shopStatusMessage;
    }

    /**
     * 
     * @return
     *     The shopIsGold
     */
    public Integer getShopIsGold() {
        return shopIsGold;
    }

    /**
     * 
     * @param shopIsGold
     *     The shop_is_gold
     */
    public void setShopIsGold(Integer shopIsGold) {
        this.shopIsGold = shopIsGold;
    }

    /**
     * 
     * @return
     *     The shopMinBadgeScore
     */
    public Integer getShopMinBadgeScore() {
        return shopMinBadgeScore;
    }

    /**
     * 
     * @param shopMinBadgeScore
     *     The shop_min_badge_score
     */
    public void setShopMinBadgeScore(Integer shopMinBadgeScore) {
        this.shopMinBadgeScore = shopMinBadgeScore;
    }

    /**
     * 
     * @return
     *     The shopIsClosedUntil
     */
    public String getShopIsClosedUntil() {
        return shopIsClosedUntil;
    }

    /**
     * 
     * @param shopIsClosedUntil
     *     The shop_is_closed_until
     */
    public void setShopIsClosedUntil(String shopIsClosedUntil) {
        this.shopIsClosedUntil = shopIsClosedUntil;
    }

    /**
     * 
     * @return
     *     The shopAlreadyFavorited
     */
    public Integer getShopAlreadyFavorited() {
        return shopAlreadyFavorited;
    }

    /**
     * 
     * @param shopAlreadyFavorited
     *     The shop_already_favorited
     */
    public void setShopAlreadyFavorited(Integer shopAlreadyFavorited) {
        this.shopAlreadyFavorited = shopAlreadyFavorited;
    }

    /**
     * 
     * @return
     *     The shopOpenSince
     */
    public String getShopOpenSince() {
        return shopOpenSince;
    }

    /**
     * 
     * @param shopOpenSince
     *     The shop_open_since
     */
    public void setShopOpenSince(String shopOpenSince) {
        this.shopOpenSince = shopOpenSince;
    }

    /**
     * 
     * @return
     *     The shopDescription
     */
    public String getShopDescription() {
        return shopDescription;
    }

    /**
     * 
     * @param shopDescription
     *     The shop_description
     */
    public void setShopDescription(String shopDescription) {
        this.shopDescription = shopDescription;
    }

    /**
     * 
     * @return
     *     The shopTagline
     */
    public String getShopTagline() {
        return shopTagline;
    }

    /**
     * 
     * @param shopTagline
     *     The shop_tagline
     */
    public void setShopTagline(String shopTagline) {
        this.shopTagline = shopTagline;
    }

    /**
     * 
     * @return
     *     The shopOwnerId
     */
    public Integer getShopOwnerId() {
        return shopOwnerId;
    }

    /**
     * 
     * @param shopOwnerId
     *     The shop_owner_id
     */
    public void setShopOwnerId(Integer shopOwnerId) {
        this.shopOwnerId = shopOwnerId;
    }

    /**
     * 
     * @return
     *     The shopOwnerLastLogin
     */
    public String getShopOwnerLastLogin() {
        return shopOwnerLastLogin;
    }

    /**
     * 
     * @param shopOwnerLastLogin
     *     The shop_owner_last_login
     */
    public void setShopOwnerLastLogin(String shopOwnerLastLogin) {
        this.shopOwnerLastLogin = shopOwnerLastLogin;
    }

    /**
     * 
     * @return
     *     The shopStatus
     */
    public Integer getShopStatus() {
        return shopStatus;
    }

    /**
     * 
     * @param shopStatus
     *     The shop_status
     */
    public void setShopStatus(Integer shopStatus) {
        this.shopStatus = shopStatus;
    }

}
