package com.tokopedia.topads.dashboard.data.model.insightkey


import com.google.gson.annotations.SerializedName

data class Header(
    @SerializedName("bid")
    val bid: BidBase = BidBase(),
    @SerializedName("btn_action")
    val btnAction: BtnAction = BtnAction(),
    @SerializedName("keyword")
    val keyword: KeywordBase = KeywordBase(),
    @SerializedName("negative")
    val negative: NegativeBase= NegativeBase()
)

data class KeywordBase(
        @SerializedName("box")
        val box: Box = Box(),
        @SerializedName("id")
        val id: Int = 0,
        @SerializedName("table")
        val table: List<Table> = listOf()
)
data class NegativeBase(
        @SerializedName("box")
        val box: Box = Box(),
        @SerializedName("id")
        val id: Int = 0,
        @SerializedName("table")
        val table: List<Table> = listOf()
)
data class BidBase(
        @SerializedName("box")
        val box: Box = Box(),
        @SerializedName("id")
        val id: Int = 0,
        @SerializedName("table")
        val table: List<Table> = listOf()
)