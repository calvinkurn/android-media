package com.tokopedia.checkout.data.model.response.shipmentaddressform

import com.google.gson.annotations.SerializedName

data class PopUp(
	@SerializedName("button")
	val button: Button = Button(),
	@SerializedName("description")
	val description: String = "",
	@SerializedName("title")
	val title: String = ""
)
