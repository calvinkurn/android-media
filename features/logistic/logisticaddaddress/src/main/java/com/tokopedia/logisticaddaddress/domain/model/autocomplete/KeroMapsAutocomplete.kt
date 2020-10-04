package com.tokopedia.logisticaddaddress.domain.model.autocomplete

import com.google.gson.annotations.SerializedName
import javax.annotation.Generated

@Generated("com.robohorse.robopojogenerator")
data class KeroMapsAutocomplete(

	@field:SerializedName("data")
	val data: Data = Data(),
	@field:SerializedName("error_code")
	val errorCode: Int = 0
)