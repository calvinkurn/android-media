package com.tokopedia.shop.common.data.source.cloud.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.shop.common.constant.ShopStatusDef

class ShopInfoDetail {
    @SerializedName("badges")
    @Expose
    var badges: List<Any>? = null

    @SerializedName("date_shop_created")
    @Expose
    var dateShopCreated: String? = null

    @SerializedName("epoch_shop_created")
    @Expose
    var epochShopCreated: Long = 0

    @SerializedName("shop_already_favorited")
    @Expose
    var shopAlreadyFavorited: String? = null

    @SerializedName("shop_avatar")
    @Expose
    var shopAvatar: String? = null

    @SerializedName("shop_cover")
    @Expose
    var shopCover: String? = null

    @SerializedName("shop_description")
    @Expose
    private var shopDescription: String? = null

    @SerializedName("shop_domain")
    @Expose
    var shopDomain: String? = null

    @SerializedName("shop_gold_expired_time")
    @Expose
    var shopGoldExpiredTime: Long = 0

    @SerializedName("shop_has_terms")
    @Expose
    var shopHasTerms: Long = 0

    @SerializedName("shop_id")
    @Expose
    var shopId: String? = null

    @SerializedName("shop_is_allow_manage")
    @Expose
    var shopIsAllowManage: Long = 0

    @SerializedName("shop_is_closed_note")
    @Expose
    var shopIsClosedNote: Long = 0

    @SerializedName("shop_is_closed_reason")
    @Expose
    var shopIsClosedReason: Long = 0

    @SerializedName("shop_is_closed_until")
    @Expose
    var shopIsClosedUntil: Long = 0

    @SerializedName("shop_is_free_returns")
    @Expose
    var shopIsFreeReturns: String? = null

    @SerializedName("shop_is_gold")
    @Expose
    var shopIsGold: String? = null

    @SerializedName("shop_is_gold_badge")
    @Expose
    var isShopIsGoldBadge = false

    @SerializedName("shop_is_official")
    @Expose
    var shopIsOfficial: String? = null

    @SerializedName("shop_is_owner")
    @Expose
    var shopIsOwner: Long = 0

    @SerializedName("shop_location")
    @Expose
    var shopLocation: String? = null

    @SerializedName("shop_lucky")
    @Expose
    var shopLucky: String? = null

    @SerializedName("shop_min_badge_score")
    @Expose
    var shopMinBadgeScore: Long = 0

    @SerializedName("shop_name")
    @Expose
    var shopName: String? = null

    @SerializedName("shop_official_bot")
    @Expose
    var shopOfficialBot: String? = null

    @SerializedName("shop_official_top")
    @Expose
    var shopOfficialTop: String? = null

    @SerializedName("shop_open_since")
    @Expose
    var shopOpenSince: String? = null

    @SerializedName("shop_owner_last_login")
    @Expose
    var shopOwnerLastLogin: String? = null

    @SerializedName("shop_reputation")
    @Expose
    var shopReputation: String? = null

    @SerializedName("shop_reputation_badge")
    @Expose
    var shopReputationBadge: String? = null

    @SerializedName("shop_score")
    @Expose
    var shopScore: Long = 0

    @SerializedName("shop_status")
    @Expose
    var shopStatus = 0

    @SerializedName("shop_status_message")
    @Expose
    var shopStatusMessage: String? = null

    @SerializedName("shop_status_title")
    @Expose
    var shopStatusTitle: String? = null

    @SerializedName("shop_tagline")
    @Expose
    private var shopTagline: String? = null

    @SerializedName("shop_total_favorit")
    @Expose
    var shopTotalFavorit: Long = 0

    @SerializedName("shop_url")
    @Expose
    var shopUrl: String? = null

    @SerializedName("total_active_product")
    @Expose
    var totalActiveProduct: Long = 0
    fun getShopDescription(): String {
        return if (shopDescription == null) "" else {
            if (shopDescription.equals("0", ignoreCase = true)) "" else shopDescription
        } ?: ""
    }

    val isOpen: Boolean
        get() = shopStatus == ShopStatusDef.OPEN

    fun setShopDescription(shopDescription: String?) {
        this.shopDescription = shopDescription
    }

    val isGoldMerchant: Boolean
        get() = GOLD_MERCHANT_VALUE == shopIsGold
    val isShopOfficial: Boolean
        get() = IS_OFFICIAL_TYPE.equals(shopIsOfficial, ignoreCase = true)

    fun getShopTagline(): String {
        return if (shopTagline == null) "" else {
            if (shopTagline.equals("0", ignoreCase = true)) "" else shopTagline
        } ?: ""
    }

    fun setShopTagline(shopTagline: String?) {
        this.shopTagline = shopTagline
    }

    companion object {
        const val IS_OFFICIAL_TYPE = "1"
        const val GOLD_MERCHANT_VALUE = "1"
    }
}