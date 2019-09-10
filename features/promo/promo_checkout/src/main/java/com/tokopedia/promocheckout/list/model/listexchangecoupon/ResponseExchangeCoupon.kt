package com.tokopedia.promocheckout.list.model.listexchangecoupon

import com.google.gson.annotations.SerializedName

data class ResponseExchangeCoupon(

	@field:SerializedName("tokopointsCatalogHighlight")
	val tokopointsCatalogHighlight: TokopointsCatalogHighlight? = null
)