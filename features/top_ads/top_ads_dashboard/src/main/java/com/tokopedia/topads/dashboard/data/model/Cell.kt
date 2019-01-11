package com.tokopedia.topads.dashboard.data.model

/**
 * Created by zulfikarrahman on 11/4/16.
 */

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date

data class Cell (

    @SerializedName("date.day")
    @Expose
    val dateDay: Int = 0,

    @SerializedName("date.month")
    @Expose
    val dateMonth: Int = 0,

    @SerializedName("date.year")
    @Expose
    val dateYear: Int = 0,

    @SerializedName("impression_sum")
    @Expose
    val impressionSum: Int = 0,

    @SerializedName("click_sum")
    @Expose
    val clickSum: Int = 0,

    @SerializedName("ctr_percentage")
    @Expose
    val ctrPercentage: Float = 0f,

    @SerializedName("conversion_sum")
    @Expose
    val conversionSum: Int = 0,

    @SerializedName("cost_avg")
    @Expose
    val costAvg: Float = 0f,

    @SerializedName("cost_sum")
    @Expose
    val costSum: Float = 0f,

    @SerializedName("all_gross_profit")
    @Expose
    val grossProfit: Float = 0f,

    @SerializedName("all_sold_sum")
    @Expose
    val soldSum: Float = 0f,

    @SerializedName("impression_sum_fmt")
    @Expose
    val impressionSumFmt: String = "",

    @SerializedName("click_sum_fmt")
    @Expose
    val clickSumFmt: String = "",

    @SerializedName("ctr_percentage_fmt")
    @Expose
    val ctrPercentageFmt: String = "",

    @SerializedName("conversion_sum_fmt")
    @Expose
    val conversionSumFmt: String = "",

    @SerializedName("cost_avg_fmt")
    @Expose
    val costAvgFmt: String = "",

    @SerializedName("cost_sum_fmt")
    @Expose
    val costSumFmt: String = "",

    @SerializedName("all_gross_profit_fmt")
    @Expose
    val grossProfitFmt: String = "",

    @SerializedName("all_sold_sum_fmt")
    @Expose
    val soldSumFmt: String = ""){

    val date: Date
        get() {
            val formatter = SimpleDateFormat("dd/MM/yyyy")
            val dateText = dateDay.toString() + "/" + dateMonth + "/" + dateYear
            var date = Date()
            try {
                date = formatter.parse(dateText)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return date
        }

}
