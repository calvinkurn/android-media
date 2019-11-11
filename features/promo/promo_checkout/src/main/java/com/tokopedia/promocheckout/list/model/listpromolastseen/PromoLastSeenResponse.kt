package com.tokopedia.promocheckout.list.model.listpromolastseen

import com.google.gson.annotations.SerializedName

data class PromoLastSeenResponse(

	@SerializedName("GetPromoSuggestion")
	val getPromoSuggestion: GetPromoSuggestion? = null
)