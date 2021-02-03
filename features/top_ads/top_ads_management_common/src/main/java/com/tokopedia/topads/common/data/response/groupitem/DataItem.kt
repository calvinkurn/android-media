package com.tokopedia.topads.common.data.response.groupitem

import com.google.gson.annotations.SerializedName

data class DataItem(

	@field:SerializedName("total_item")
	val totalItem: Int = 0,

	@field:SerializedName("group_status")
	val groupStatus: Int = 0,

	@field:SerializedName("stat_total_conversion")
	val statTotalConversion: String = "",

	@field:SerializedName("total_keyword")
	val totalKeyword: Int = 0,

	@field:SerializedName("group_price_daily_bar")
	val groupPriceDailyBar: String = "",

	@field:SerializedName("group_price_daily_spent_fmt")
	val groupPriceDailySpentFmt: String = "",

	@field:SerializedName("stat_total_spent")
	val statTotalSpent: String = "",

	@field:SerializedName("group_status_desc")
	val groupStatusDesc: String = "",

	@field:SerializedName("stat_total_click")
	val statTotalClick: String = "",

	@field:SerializedName("group_name")
	val groupName: String = "",

	@field:SerializedName("group_price_bid")
	val groupPriceBid: Int = 0,

	@field:SerializedName("stat_total_impression")
	val statTotalImpression: String = "",

	@field:SerializedName("group_id")
	val groupId: Int = 0,

	@field:SerializedName("group_status_toogle")
	val groupStatusToogle: Int = 0,

	@field:SerializedName("stat_total_ctr")
	val statTotalCtr: String = "",

	@field:SerializedName("group_price_daily")
	val groupPriceDaily: Long = 0,

	@field:SerializedName("stat_total_sold")
	val statTotalSold: String = "",

	@field:SerializedName("stat_avg_click")
	val statAvgClick: String = "",

	@field:SerializedName("group_type")
	val groupType: String = "",

	@field:SerializedName("group_end_date")
	val groupEndDate: String = "",

	@field:SerializedName("stat_total_income")
	val groupTotalIncome: String = ""

)