package com.tokopedia.topads.data.response

import com.google.gson.annotations.SerializedName

open class AdsItem(

	var ad: Ad = Ad(),

	var productID: String = "",

	var groupSchedule: String = "0",

	var source: String = "dashboard_add_product",

	var stickerID: String = "3"
)