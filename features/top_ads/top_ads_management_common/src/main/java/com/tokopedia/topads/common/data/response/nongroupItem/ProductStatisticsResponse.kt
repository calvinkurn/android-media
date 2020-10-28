package com.tokopedia.topads.common.data.response.nongroupItem

import com.google.gson.annotations.SerializedName

data class ProductStatisticsResponse(

	@field:SerializedName("GetDashboardProductStatistics")
	val getDashboardProductStatistics: GetDashboardProductStatistics = GetDashboardProductStatistics()
)
data class GetDashboardProductStatistics(

		@field:SerializedName("data")
		val data: List<WithoutGroupDataItem> = listOf()
)