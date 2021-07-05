package com.tokopedia.tokopoints.view.model.rewardtopsection

import com.google.gson.annotations.SerializedName

data class Counter(

	@SerializedName("counterStr")
	val counterStr: String? = null,

	@SerializedName("isShowCounter")
    var isShowCounter: Boolean? = null,

	@SerializedName("counter")
	val counter: Int? = null
)