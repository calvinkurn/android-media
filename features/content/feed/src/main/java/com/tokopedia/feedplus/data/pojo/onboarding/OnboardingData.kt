package com.tokopedia.feedplus.data.pojo.onboarding

import com.google.gson.annotations.Expose
import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

data class OnboardingData(

	@SerializedName("feedUserOnboardingInterests")
	@Expose
	val feedUserOnboardingInterests: FeedUserOnboardingInterests = FeedUserOnboardingInterests()
)