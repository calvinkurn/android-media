package com.tokopedia.tokopoints.view.model.rewrdsStatusMatching

import com.google.gson.annotations.SerializedName

data class RewardTickerResponse(

	@SerializedName("rewardsTicker")
	val rewardsTicker: RewardsTicker? = null
)

data class Ticker(

	@SerializedName("tickerList")
	val tickerList: List<TickerListItem?>? = null
)

data class RewardsTicker(

	@SerializedName("resultStatus")
	val resultStatus: ResultStatus? = null,

	@SerializedName("ticker")
	val ticker: Ticker? = null
)

data class TickerListItem(

	@SerializedName("metadata")
	val metadata: List<MetadataItem?>? = null,

	@SerializedName("id")
	val id: Int? = null,

	@SerializedName("type")
	val type: String? = null
)

data class ResultStatus(

	@SerializedName("code")
	val code: String? = null,

	@SerializedName("message")
	val message: List<String?>? = null
)

data class Text(

	@SerializedName("content")
	val content: String? = null
)

data class MetadataItem(

	@SerializedName("image")
	val image: Image? = null,

	@SerializedName("link")
	val link: Link? = null,

	@SerializedName("text")
	val text: Text? = null
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
