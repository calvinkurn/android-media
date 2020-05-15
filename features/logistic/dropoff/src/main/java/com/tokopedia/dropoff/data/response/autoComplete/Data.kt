package com.tokopedia.dropoff.data.response.autoComplete

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class Data(

	@field:SerializedName("predictions")
	val predictions: List<PredictionsItem> = emptyList()
)