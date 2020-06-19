package com.tokopedia.topads.dashboard.data.model.groupitem

import com.google.gson.annotations.SerializedName

data class GroupItemResponse(

	@field:SerializedName("GetTopadsDashboardGroups")
	val getTopadsDashboardGroups: GetTopadsDashboardGroups = GetTopadsDashboardGroups()
){
	data class GetTopadsDashboardGroups(

			@field:SerializedName("separate_statistic")
			val separateStatistic: String = "",

			@field:SerializedName("data")
			val data: List<DataItem> = listOf()
	)
}