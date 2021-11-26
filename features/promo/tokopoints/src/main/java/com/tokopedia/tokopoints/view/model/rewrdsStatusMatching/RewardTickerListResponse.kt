package com.tokopedia.tokopoints.view.model.rewrdsStatusMatching

import com.google.gson.annotations.SerializedName

data class RewardTickerListResponse(

    @SerializedName("rewardsTickerList")
    val rewardsTickerList: RewardsTickerList? = null
)

data class Image(

    @SerializedName("url")
    val url: String? = null
)

data class Link(

    @SerializedName("applink")
    val applink: String? = null,

    @SerializedName("url")
    val url: String? = null
)

data class ResultStatus(

    @SerializedName("code")
    val code: String? = null,

    @SerializedName("message")
    val message: List<String?>? = null,

    @SerializedName("status")
    val status: String? = null
)

data class Text(

    @SerializedName("color")
    val color: String? = null,

    @SerializedName("format")
    val format: String? = null,

    @SerializedName("content")
    val content: String? = null
)

data class RewardsTickerList(

    @SerializedName("resultStatus")
    val resultStatus: ResultStatus? = null,

    @SerializedName("tickerList")
    val tickerList: List<TickerListItem?>? = null
)

data class TickerListItem(

    @SerializedName("metadata")
    val metadata: List<MetadataItem?>? = null,

    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("type")
    val type: String? = null
)

data class MetadataItem(

    @SerializedName("image")
    val image: Image? = null,

    @SerializedName("link")
    val link: Link? = null,

    @SerializedName("backgroundImageURLDesktop")
    val backgroundImageURLDesktop: String? = null,

    @SerializedName("text")
    val text: Text? = null,

    @SerializedName("backgroundImageURL")
    val backgroundImageURL: String? = null,

    @SerializedName("timeRemainingSeconds")
    var timeRemainingSeconds: Long? = null,

    @SerializedName("isShowTime")
    val isShowTime: Boolean? = false
)
