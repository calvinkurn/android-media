package com.tokopedia.interest_pick_common.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FeedUserOnboardingInterests(

		@SerializedName("data")
	@Expose
	var data: List<DataItem> = listOf(),

		@SerializedName("meta")
	@Expose
	val meta: Meta = Meta()
)