package com.tokopedia.logisticaddaddress.domain.model.autocomplete

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class PredictionsItem(

	@field:SerializedName("types")
	val types: List<String> = emptyList(),

	@field:SerializedName("matched_substrings")
	val matchedSubstrings: List<MatchedSubstringsItem> = emptyList(),

	@field:SerializedName("terms")
	val terms: List<TermsItem> = emptyList(),

	@field:SerializedName("structured_formatting")
	val structuredFormatting: StructuredFormatting = StructuredFormatting(),

	@field:SerializedName("description")
	val description: String = "",

	@field:SerializedName("place_id")
	val placeId: String = ""
)