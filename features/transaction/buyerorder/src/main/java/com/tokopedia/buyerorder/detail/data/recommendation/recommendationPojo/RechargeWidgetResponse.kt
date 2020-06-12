package com.tokopedia.buyerorder.detail.data.recommendationPojo

import com.google.gson.annotations.SerializedName

data class RechargeWidgetResponse(

	@SerializedName("home_widget")
	val homeWidget: HomeWidget?
)
