package com.tokopedia.affiliate.feature.dashboard.data.pojo.commission

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AffiliatedProductTxList(

	@SerializedName("next")
	@Expose
	val next: String = "",

	@SerializedName("has_next")
	@Expose
	val hasNext: Boolean = false,

	@SerializedName("history")
	@Expose
	val history: MutableList<AffiliateHistoryPojo> = ArrayList()
)