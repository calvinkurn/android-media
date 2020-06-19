package com.tokopedia.topads.dashboard.data.model.nongroupItem

import com.google.gson.annotations.SerializedName

data class NonGroupResponse(

	@field:SerializedName("topadsDashboardGroupProducts")
	val topadsDashboardGroupProducts: TopadsDashboardGroupProducts = TopadsDashboardGroupProducts()
)
{
	data class TopadsDashboardGroupProducts(

			@field:SerializedName("separate_statistic")
			val separateStatistic: String = "",

			@field:SerializedName("data")
			val data: List<WithoutGroupDataItem> = listOf()
	)
}