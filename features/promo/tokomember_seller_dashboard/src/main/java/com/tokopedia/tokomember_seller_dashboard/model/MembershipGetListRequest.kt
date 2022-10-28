package com.tokopedia.tokomember_seller_dashboard.model

import com.google.gson.annotations.SerializedName

data class MembershipGetListRequest(

	@SerializedName("param")
	val param: Param? = null,

	@SerializedName("pageInfo")
	val pageInfo: PageInfo? = null
)

data class PageInfo(

	@SerializedName("pageSize")
	val pageSize: Int? = null,

	@SerializedName("page")
	val page: Int? = null
)

data class Param(

	@SerializedName("shop")
	val shop: Shop? = null,

	@SerializedName("program")
	val program: Program? = null,

	@SerializedName("card")
	val card: ListCard? = null
)

data class Shop(

	@SerializedName("id")
	val id: Int? = null
)

data class Program(

	@SerializedName("status")
	val status: Int? = null
)

data class ListCard(

	@SerializedName("id")
	val id: Int? = null
)
