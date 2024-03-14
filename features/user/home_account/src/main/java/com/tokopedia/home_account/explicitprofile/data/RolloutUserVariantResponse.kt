package com.tokopedia.home_account.explicitprofile.data

import com.google.gson.annotations.SerializedName

data class RolloutUserVariantResponse(
	@SerializedName("RolloutUserVariant")
	val rolloutUserVariant: RolloutUserVariant = RolloutUserVariant()
)

data class RolloutUserVariant(
	@SerializedName("message")
	val message: String = "",

	@SerializedName("featureVariants")
	val featureVariants: List<FeatureVariantsItem> = listOf()
)

data class FeatureVariantsItem(
	@SerializedName("feature")
	val feature: String = "",

	@SerializedName("variant")
	val variant: String = ""
)
