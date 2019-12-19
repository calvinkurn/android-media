package com.tokopedia.promocheckout.list.model.listpromocatalog

import com.google.gson.annotations.SerializedName

data class TokopointsCatalogHighlight(

	@SerializedName("resultStatus")
	val resultStatus: ResultStatus? = null,

	@SerializedName("catalogList")
	val catalogList: List<CatalogListItem?>? = null,

	@SerializedName("subTitle")
	val subTitle: String? = null,

	@SerializedName("title")
	val title: String? = null
)