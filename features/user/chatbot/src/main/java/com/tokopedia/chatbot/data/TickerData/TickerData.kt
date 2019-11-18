package com.tokopedia.chatbot.data.TickerData

import com.google.gson.annotations.SerializedName

data class TickerData(

		@SerializedName("is_success")
		val isSuccess: Int? = null,

		@SerializedName("type")
		val type: String? = null,

		@SerializedName("items")
		val items: List<ItemsItem?>? = null
)