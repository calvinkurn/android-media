package com.tokopedia.feedplus.data.pojo.onboarding

import com.google.gson.annotations.Expose
import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

data class SubmitInterestResponse(

	@SerializedName("feed_interest_user_update")
	@Expose
	val feedInterestUserUpdate: FeedInterestUserUpdate = FeedInterestUserUpdate()
)