package com.tokopedia.recharge_credit_card.datamodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.common.topupbills.data.TopupBillsUserPerso

class RechargeCCMenuDetailResponse(
        @SerializedName("rechargeCatalogMenuDetail")
        @Expose
        val menuDetail: RechargeCCMenuDetail = RechargeCCMenuDetail()
)

class RechargeCCMenuDetail(
        @SerializedName("menu_name")
        @Expose
        val menuName: String = "",
        @SerializedName("menu_label")
        @Expose
        val menuLabel: String = "",
        @SerializedName("category_ids")
        @Expose
        val categoryIds: List<Int> = listOf(),
        @SerializedName("tickers")
        @Expose
        val tickers: List<TickerCreditCard> = listOf(),
        @SerializedName("user_perso")
        @Expose
        val userPerso: RechargeCCUserPerso = RechargeCCUserPerso(),
        @SerializedName("recommendations")
        @Expose
        val recommendations: List<RechargeCCRecommendation> = listOf(),
        @SerializedName("promos")
        @Expose
        val promos: List<RechargeCCPromo> = listOf(),
)

class TickerCreditCard(
        @SerializedName("ID")
        @Expose
        val id: String = "0",
        @SerializedName("Name")
        @Expose
        val name: String = "",
        @SerializedName("Content")
        @Expose
        val content: String = "",
        @SerializedName("Type")
        @Expose
        val type: String = ""
)

class RechargeCCUserPerso(
        @SerializedName("loyalty_status")
        @Expose
        val loyaltyStatus: String = "",
)

class RechargeCCRecommendation(
        @SerializedName("iconUrl")
        @Expose
        val iconUrl: String = "",
        @SerializedName("title")
        @Expose
        val title: String = "",
        @SerializedName("clientNumber")
        @Expose
        val clientNumber: String = "",
        @SerializedName("appLink")
        @Expose
        val applink: String = "",
        @SerializedName("webLink")
        @Expose
        val weblink: String = "",
        @SerializedName("productPrice")
        @Expose
        val productPrice: Int = 0,
        @SerializedName("type")
        @Expose
        val type: String = "",
        @SerializedName("categoryId")
        @Expose
        val categoryId: String = "0",
        @SerializedName("productId")
        @Expose
        val productId: String = "0",
        @SerializedName("isATC")
        @Expose
        val isAtc: Boolean = false,
        @SerializedName("operatorID")
        @Expose
        val operatorId: String = "0",
        @SerializedName("description")
        @Expose
        val description: String = "",
        @SerializedName("label")
        @Expose
        val label: String = "",
        @SerializedName("token")
        @Expose
        val token: String = "",
        var position: Int = 0
)

class RechargeCCPromo(
        @SerializedName("id")
        @Expose
        val id: String = "0",
        @SerializedName("img_url")
        @Expose
        val urlBannerPromo: String = "",
        @SerializedName("title")
        @Expose
        val title: String = "",
        @SerializedName("subtitle")
        @Expose
        val subtitle: String = "",
        @SerializedName("promo_code")
        @Expose
        val promoCode: String = "",
        var voucherCodeCopied: Boolean = false
)
