package com.tokopedia.chatbot.data.TickerData

import com.google.gson.annotations.SerializedName

data class TickerData(

		@SerializedName("is_success")
		val isSuccess: Int = 0,

		@SerializedName("type")
		val type: String? = "",

		@SerializedName("items")
		val items: List<ItemsItem?>? = null
)