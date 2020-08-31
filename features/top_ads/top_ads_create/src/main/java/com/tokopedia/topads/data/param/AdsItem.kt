package com.tokopedia.topads.data.param

import com.google.gson.annotations.SerializedName


open class AdsItem(
		@SerializedName("ad")
	    var ad: Ad = Ad(),
		@SerializedName("productID")
		var productID: String = "",
		@SerializedName("groupSchedule")
		var groupSchedule: String = "0",
		@SerializedName("source")
		var source: String = "dashboard_add_product",
		@SerializedName("stickerID")
		var stickerID: String = "3"
)