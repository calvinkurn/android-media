package com.tokopedia.topads.dashboard.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by zulfikarrahman on 11/4/16.
 */

data class Summary (

    @SerializedName("click_sum")
    @Expose
    val clickSum: Int = 0,

    @SerializedName("click_sum_fmt")
    @Expose
    val clickSumFmt: String = "",

    @SerializedName("cost_sum")
    @Expose
    val costSum: Double = 0.0,

    @SerializedName("cost_sum_fmt")
    @Expose
    val costSumFmt: String = "",

    @SerializedName("impression_sum")
    @Expose
    val impressionSum: Int = 0,

    @SerializedName("impression_sum_fmt")
    @Expose
    val impressionSumFmt: String = "",

    @SerializedName("ctr_percentage")
    @Expose
    val ctrPercentage: Double = 0.0,

    @SerializedName("ctr_percentage_fmt")
    @Expose
    val ctrPercentageFmt: String = "",

    @SerializedName("conversion_sum")
    @Expose
    val conversionSum: Int = 0,

    @SerializedName("conversion_sum_fmt")
    val conversionSumFmt: String = "",

    @SerializedName("cost_avg")
    @Expose
    val costAvg: Double = 0.0,

    @SerializedName("cost_avg_fmt")
    @Expose
    val costAvgFmt: String = "",

    @SerializedName("all_gross_profit")
    @Expose
    val grossProfit: Float = 0f,

    @SerializedName("all_sold_sum")
    @Expose
    val soldSum: Float = 0f,

    @SerializedName("all_gross_profit_fmt")
    @Expose
    val grossProfitFmt: String = "",

    @SerializedName("all_sold_sum_fmt")
    @Expose
    val soldSumFmt: String = ""
    )