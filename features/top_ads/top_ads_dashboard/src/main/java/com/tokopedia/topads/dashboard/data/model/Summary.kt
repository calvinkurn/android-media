package com.tokopedia.topads.dashboard.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by zulfikarrahman on 11/4/16.
 */

data class Summary (

    @SerializedName("ads_click_sum")
    @Expose
    val clickSum: Int = 0,

    @SerializedName("ads_click_sum_fmt")
    @Expose
    val clickSumFmt: String = "",

    @SerializedName("ads_cost_sum")
    @Expose
    val costSum: Double = 0.0,

    @SerializedName("ads_cost_sum_fmt")
    @Expose
    val costSumFmt: String = "",

    @SerializedName("ads_impression_sum")
    @Expose
    val impressionSum: Int = 0,

    @SerializedName("ads_impression_sum_fmt")
    @Expose
    val impressionSumFmt: String = "",

    @SerializedName("ads_ctr_percentage")
    @Expose
    val ctrPercentage: Double = 0.0,

    @SerializedName("ads_ctr_percentage_fmt")
    @Expose
    val ctrPercentageFmt: String = "",

    @SerializedName("ads_conversion_sum")
    @Expose
    val conversionSum: Int = 0,

    @SerializedName("ads_conversion_sum_fmt")
    val conversionSumFmt: String = "",

    @SerializedName("ads_cost_avg")
    @Expose
    val costAvg: Double = 0.0,

    @SerializedName("ads_cost_avg_fmt")
    @Expose
    val costAvgFmt: String = "",

    @SerializedName("ads_all_gross_profit")
    @Expose
    val grossProfit: Float = 0f,

    @SerializedName("ads_all_sold_sum")
    @Expose
    val soldSum: Float = 0f,

    @SerializedName("ads_all_gross_profit_fmt")
    @Expose
    val grossProfitFmt: String = "",

    @SerializedName("ads_all_sold_sum_fmt")
    @Expose
    val soldSumFmt: String = ""
    )