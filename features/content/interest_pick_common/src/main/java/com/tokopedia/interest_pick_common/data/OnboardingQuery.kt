package com.tokopedia.interest_pick_common.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class OnboardingQuery(

	@SerializedName("data")
	@Expose
	val onboardingData: OnboardingData = OnboardingData()
)