package com.tokopedia.chatbot.data.TickerData

import com.google.gson.annotations.SerializedName


data class TickerDataResponse(

		@SerializedName("chipGetActiveTickerV4")
		val chipGetActiveTickerV4: ChipGetActiveTickerV4? = null
)