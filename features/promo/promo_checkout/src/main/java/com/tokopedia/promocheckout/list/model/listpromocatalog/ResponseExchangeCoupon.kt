package com.tokopedia.promocheckout.list.model.listpromocatalog

import com.google.gson.annotations.SerializedName

data class ResponseExchangeCoupon(

	@SerializedName("tokopointsCatalogHighlight")
	val tokopointsCatalogHighlight: TokopointsCatalogHighlight? = null
)