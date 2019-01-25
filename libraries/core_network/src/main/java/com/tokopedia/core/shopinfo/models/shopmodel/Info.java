
package com.tokopedia.core.shopinfo.models.shopmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.base.utils.StringUtils;
import com.tokopedia.core.var.Badge;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class Info {

    private static final int SHOP_GOLD_MERCHANT_VALUE = 1;
    public static final int SHOP_OFFICIAL_VALUE = 1;

    @SerializedName("shop_status_title")
    @Expose
    public String shopStatusTitle;
    @SerializedName("shop_is_closed_reason")
    @Expose
    public int shopIsClosedReason;
    @SerializedName("shop_lucky")
    @Expose
    public String shopLucky;
    @SerializedName("shop_id")
    @Expose
    public String shopId;
    @SerializedName("shop_owner_last_login")
    @Expose
    public String shopOwnerLastLogin;
    @SerializedName("shop_tagline")
    @Expose
    public String shopTagline;
    @SerializedName("shop_url")
    @Expose
    public String shopUrl;
    @SerializedName("shop_status_message")
    @Expose
    public String shopStatusMessage;
    @SerializedName("shop_description")
    @Expose
    public String shopDescription;
    @SerializedName("shop_cover")
    @Expose
    public String shopCover;
    @SerializedName("shop_has_terms")
    @Expose
    public int shopHasTerms;
    @SerializedName("shop_is_gold")
    @Expose
    public int shopIsGold;
    @SerializedName("shop_is_gold_badge")
    @Expose
    public boolean shopIsGoldBadge;
    @SerializedName("shop_open_since")
    @Expose
    public String shopOpenSince;
    @SerializedName("shop_min_badge_score")
    @Expose
    public int shopMinBadgeScore;
    @SerializedName("shop_location")
    @Expose
    public String shopLocation;
    @SerializedName("shop_official_top")
    @Expose
    public String shopOfficialTop;
    @SerializedName("shop_is_closed_until")
    @Expose
    public int shopIsClosedUntil;
    @SerializedName("shop_name")
    @Expose
    public String shopName;
    @SerializedName("shop_reputation")
    @Expose
    public String shopReputation;
    @SerializedName("shop_is_official")
    @Expose
    public int shopIsOfficial;
    @SerializedName("shop_owner_id")
    @Expose
    public int shopOwnerId;
    @SerializedName("shop_already_favorited")
    @Expose
    public int shopAlreadyFavorited;
    @SerializedName("shop_is_owner")
    @Expose
    public int shopIsOwner;
    @SerializedName("shop_status")
    @Expose
    public int shopStatus;
    @SerializedName("shop_is_closed_note")
    @Expose
    public int shopIsClosedNote;
    @SerializedName("shop_reputation_badge")
    @Expose
    public String shopReputationBadge;
    @SerializedName("shop_avatar")
    @Expose
    public String shopAvatar;
    @SerializedName("shop_total_favorit")
    @Expose
    public int shopTotalFavorit;
    @SerializedName("shop_domain")
    @Expose
    public String shopDomain;
    @SerializedName("shop_is_free_returns")
    @Expose
    public String shopIsFreeReturns;

    @SerializedName("badges")
    @Expose
    public List<Badge> badges = new ArrayList<>();

    public String getShopStatusTitle() {
        return shopStatusTitle;
    }

    public int getShopIsClosedReason() {
        return shopIsClosedReason;
    }

    public String getShopLucky() {
        return shopLucky;
    }

    public String getShopId() {
        return shopId;
    }

    public String getShopOwnerLastLogin() {
        return shopOwnerLastLogin;
    }

    public String getShopTagline() {
        return shopTagline;
    }

    public String getShopUrl() {
        return shopUrl;
    }

    public String getShopStatusMessage() {
        return shopStatusMessage;
    }

    public String getShopDescription() {
        return shopDescription;
    }

    public String getShopCover() {
        return shopCover;
    }

    public int getShopHasTerms() {
        return shopHasTerms;
    }

    public boolean isGoldMerchant() {
        return shopIsGold == SHOP_GOLD_MERCHANT_VALUE;
    }

    public boolean isShopOfficialStore() {
        return shopIsOfficial == SHOP_OFFICIAL_VALUE;
    }

    public boolean isShopIsGoldBadge() {
        return shopIsGoldBadge;
    }

    public String getShopOpenSince() {
        return shopOpenSince;
    }

    public int getShopMinBadgeScore() {
        return shopMinBadgeScore;
    }

    public String getShopLocation() {
        return shopLocation;
    }

    public String getShopOfficialTop() {
        return shopOfficialTop;
    }

    public int getShopIsClosedUntil() {
        return shopIsClosedUntil;
    }

    public String getShopName() {
        return shopName;
    }

    public String getShopReputation() {
        return shopReputation;
    }

    public int getShopIsOfficial() {
        return shopIsOfficial;
    }

    public int getShopOwnerId() {
        return shopOwnerId;
    }

    public int getShopAlreadyFavorited() {
        return shopAlreadyFavorited;
    }

    public int getShopIsOwner() {
        return shopIsOwner;
    }

    public int getShopStatus() {
        return shopStatus;
    }

    public int getShopIsClosedNote() {
        return shopIsClosedNote;
    }

    public String getShopReputationBadge() {
        return shopReputationBadge;
    }

    public String getShopAvatar() {
        return shopAvatar;
    }

    public int getShopTotalFavorit() {
        return shopTotalFavorit;
    }

    public String getShopDomain() {
        return shopDomain;
    }

    public String getShopIsFreeReturns() {
        return shopIsFreeReturns;
    }

    public List<Badge> getBadges() {
        return badges;
    }

    /**
     *
     * @return
     */
    public boolean isFreeReturns(){
        if(StringUtils.isNotBlank(shopIsFreeReturns) && Double.parseDouble(
                StringUtils.omitPunctuationAndDoubleSpace( shopIsFreeReturns)) > 0){
            return true;
        }
        return false;
    }

    public boolean isOfficialStore(){
        if(shopIsOfficial > 0){
            return true;
        }
        return false;
    }

}
