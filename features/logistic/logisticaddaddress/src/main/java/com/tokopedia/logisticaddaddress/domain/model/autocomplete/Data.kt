package com.tokopedia.logisticaddaddress.domain.model.autocomplete

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class Data(

	@field:SerializedName("predictions")
	val predictions: List<PredictionsItem> = emptyList()
)