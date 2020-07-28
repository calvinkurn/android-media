package com.tokopedia.buyerorder.detail.data.recommendationPojo

import com.google.gson.annotations.SerializedName

data class HomeWidget(

	@SerializedName("widget_grid")
	val widgetGrid: List<WidgetGridItem>?
)
