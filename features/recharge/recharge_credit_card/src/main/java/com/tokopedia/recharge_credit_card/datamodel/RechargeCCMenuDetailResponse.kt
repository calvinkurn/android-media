package com.tokopedia.recharge_credit_card.datamodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

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
        val tickers: List<TickerCreditCard> = listOf()
)

class TickerCreditCard(
        @SerializedName("ID")
        @Expose
        val id: Int = 0,
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