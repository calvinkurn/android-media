package com.tokopedia.interest_pick_common.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class OnboardingData(

	@SerializedName("feedUserOnboardingInterests")
	@Expose
	val feedUserOnboardingInterests: FeedUserOnboardingInterests = FeedUserOnboardingInterests()
)