package com.tokopedia.topads.common.data.model

import com.google.gson.annotations.SerializedName


open class AdsItem(
		@SerializedName("ad")
	    var ad: Ad = Ad(),
		@SerializedName("productID")
		var productID: String = "",
		@SerializedName("source")
		var source: String = "dashboard_add_product",
)