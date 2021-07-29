package com.tokopedia.tokopoints.view.model.rewrdsStatusMatching

import com.google.gson.annotations.SerializedName

data class RewardTickerListResponse(

	@field:SerializedName("rewardsTickerList")
	val rewardsTickerList: RewardsTickerList? = null
)

data class Image(

	@field:SerializedName("url")
	val url: String? = null
)

data class Link(

	@field:SerializedName("applink")
	val applink: String? = null,

	@field:SerializedName("url")
	val url: String? = null
)

data class ResultStatus(

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("message")
	val message: List<String?>? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class Text(

	@field:SerializedName("color")
	val color: String? = null,

	@field:SerializedName("format")
	val format: String? = null,

	@field:SerializedName("content")
	val content: String? = null
)

data class RewardsTickerList(

	@field:SerializedName("resultStatus")
	val resultStatus: ResultStatus? = null,

	@field:SerializedName("tickerList")
	val tickerList: List<TickerListItem?>? = null
)

data class TickerListItem(

	@field:SerializedName("metadata")
	val metadata: List<MetadataItem?>? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("type")
	val type: String? = null
)

data class MetadataItem(

	@field:SerializedName("image")
	val image: Image? = null,

	@field:SerializedName("link")
	val link: Link? = null,

	@field:SerializedName("backgroundImageURLDesktop")
	val backgroundImageURLDesktop: String? = null,

	@field:SerializedName("text")
	val text: Text? = null,

	@field:SerializedName("backgroundImageURL")
	val backgroundImageURL: String? = null
)
