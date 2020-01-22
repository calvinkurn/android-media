package com.tokopedia.interest_pick_common.data

import com.google.gson.annotations.Expose
import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

data class FeedInterestUserUpdate(

	@SerializedName("success")
	@Expose
	val success: Boolean = false,

	@SerializedName("error")
	@Expose
	val error: String = ""
)