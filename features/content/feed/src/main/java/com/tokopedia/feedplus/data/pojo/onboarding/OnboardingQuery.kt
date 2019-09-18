package com.tokopedia.feedplus.data.pojo.onboarding

import com.google.gson.annotations.Expose
import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

data class OnboardingQuery(

	@SerializedName("data")
	@Expose
	val onboardingData: OnboardingData = OnboardingData()
)