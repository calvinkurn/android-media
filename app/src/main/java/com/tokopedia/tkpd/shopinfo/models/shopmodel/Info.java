
package com.tokopedia.tkpd.shopinfo.models.shopmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.tkpd.var.ProductItem;

import java.util.ArrayList;
import java.util.List;

public class Info {

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
    @SerializedName("shop_open_since")
    @Expose
    public String shopOpenSince;
    @SerializedName("shop_min_badge_score")
    @Expose
    public int shopMinBadgeScore;
    @SerializedName("shop_location")
    @Expose
    public String shopLocation;
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

    @SerializedName("badges")
    @Expose
    public List<ProductItem.Badge> badges = new ArrayList<ProductItem.Badge>();

}
