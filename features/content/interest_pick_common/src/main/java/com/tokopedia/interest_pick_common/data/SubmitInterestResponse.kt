package com.tokopedia.interest_pick_common.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SubmitInterestResponse(

	@SerializedName("feed_interest_user_update")
	@Expose
	val feedInterestUserUpdate: FeedInterestUserUpdate = FeedInterestUserUpdate()
)