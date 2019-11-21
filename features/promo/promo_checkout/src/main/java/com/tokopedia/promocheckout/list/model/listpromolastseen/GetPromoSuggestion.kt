package com.tokopedia.promocheckout.list.model.listpromolastseen

import com.google.gson.annotations.SerializedName

data class GetPromoSuggestion(

	@SerializedName("PromoHistory")
	val promoHistory: List<PromoHistoryItem?>? = null
)