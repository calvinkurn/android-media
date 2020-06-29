package com.tokopedia.dropoff.data.response.autoComplete

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class AutocompleteResponse(

	@field:SerializedName("kero_maps_autocomplete")
	val keroMapsAutocomplete: KeroMapsAutocomplete = KeroMapsAutocomplete()
)