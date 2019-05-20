package com.tokopedia.logisticaddaddress.domain.model.autofill

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class AutofillResponse(

	@field:SerializedName("kero_maps_autofill")
	val keroMapsAutofill: KeroMapsAutofill = KeroMapsAutofill()
)