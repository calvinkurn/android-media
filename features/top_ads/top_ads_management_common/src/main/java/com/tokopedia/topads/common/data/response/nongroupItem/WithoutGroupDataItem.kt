package com.tokopedia.topads.common.data.response.nongroupItem

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WithoutGroupDataItem(

	@field:SerializedName("stat_total_conversion")
	val statTotalConversion: String = "",

	@field:SerializedName("product_image_uri")
	val productImageUri: String = "",

	@field:SerializedName("ad_price_bid_fmt")
	val adPriceBidFmt: String = "",

	@field:SerializedName("stat_total_spent")
	val statTotalSpent: String = "",

	@field:SerializedName("ad_price_daily_bar")
	val adPriceDailyBar: String = "",

	@field:SerializedName("stat_total_click")
	val statTotalClick: String = "",

	@field:SerializedName("ad_price_daily_fmt")
	val adPriceDailyFmt: String = "",

	@field:SerializedName("ad_status_desc")
	val adStatusDesc: String = "",

	@field:SerializedName("ad_price_bid")
	val adPriceBid: Int = 0,

	@field:SerializedName("item_id")
	val itemId: Int = 0,

	@field:SerializedName("stat_avg_position")
	val statAvgPosition: Int = 0,

	@field:SerializedName("ad_price_daily")
	val adPriceDaily: Int = 0,

	@field:SerializedName("ad_price_daily_spent_fmt")
	val adPriceDailySpentFmt: String = "",

	@field:SerializedName("product_name")
	val productName: String = "",

	@field:SerializedName("ad_status")
	val adStatus: Int = 0,

	@field:SerializedName("ad_status_toogle")
	val adStatusToogle: Int = 0,

	@field:SerializedName("stat_total_gross_profit")
	val statTotalGrossProfit: String = "",

	@field:SerializedName("stat_total_impression")
	val statTotalImpression: String = "",

	@field:SerializedName("ad_id")
	val adId: Int = 0,

	@field:SerializedName("group_id")
	val groupId: Int = 0,

	@field:SerializedName("group_name")
	val groupName: String = "",

	@field:SerializedName("stat_total_ctr")
	val statTotalCtr: String = "",

	@field:SerializedName("stat_total_sold")
	val statTotalSold: String = "",

	@field:SerializedName("stat_avg_click")
	val statAvgClick: String = ""
) : Parcelable