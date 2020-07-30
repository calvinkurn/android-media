package com.tokopedia.promocheckout.list.model.listpromolastseen

import com.google.gson.annotations.SerializedName

data class PromoHistoryItem(

	@SerializedName("PromoContent")
	val promoContent: PromoContent? = null,

	@SerializedName("PromoCode")
	val promoCode: String? = null,

	@SerializedName("ActiveTimeWindow")
	val activeTimeWindow: ActiveTimeWindow? = null
)