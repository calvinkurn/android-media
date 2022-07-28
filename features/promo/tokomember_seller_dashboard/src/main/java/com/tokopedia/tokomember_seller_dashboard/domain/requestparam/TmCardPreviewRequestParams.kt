package com.tokopedia.tokomember_seller_dashboard.domain.requestparam

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TmCardPreviewRequestParams(

	@Expose
	@SerializedName("param")
	val param: Param? = null,

	@Expose
	@SerializedName("pageInfo")
	val pageInfo: PageInfo? = null
)

data class CardPreview(
	@Expose
	@SerializedName("status")
	val status: Int? = null
)

data class Shop(
	@Expose
	@SerializedName("id")
	val id: Int? = null
)

data class PageInfo(
	@Expose
	@SerializedName("pageSize")
	val pageSize: Int? = null,
	@Expose
	@SerializedName("page")
	val page: Int? = null
)

data class Param(
	@Expose
	@SerializedName("shop")
	val shop: Shop? = null,
	@Expose
	@SerializedName("card")
	val card: CardPreview? = null
)
