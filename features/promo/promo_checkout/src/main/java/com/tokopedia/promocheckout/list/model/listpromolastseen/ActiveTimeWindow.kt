package com.tokopedia.promocheckout.list.model.listpromolastseen

import com.google.gson.annotations.SerializedName

data class ActiveTimeWindow(

	@SerializedName("StartDate")
	val startDate: Int? = null,

	@SerializedName("EndTime")
	val endTime: Int? = null,

	@SerializedName("StartTime")
	val startTime: Int? = null,

	@SerializedName("ID")
	val iD: Int? = null,

	@SerializedName("EndDate")
	val endDate: Int? = null
)