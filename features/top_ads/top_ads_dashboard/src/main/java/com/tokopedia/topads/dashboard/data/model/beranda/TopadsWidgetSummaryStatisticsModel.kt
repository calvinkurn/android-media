package com.tokopedia.topads.dashboard.data.model.beranda


import com.google.gson.annotations.SerializedName

data class TopadsWidgetSummaryStatisticsModel(
    @SerializedName("topadsWidgetSummaryStatistics")
    val topadsWidgetSummaryStatistics: TopadsWidgetSummaryStatistics
) {
    data class TopadsWidgetSummaryStatistics(
        @SerializedName("data")
        val `data`: WidgetSummaryStatistics,
        @SerializedName("header")
        val header: Header
    ) {
        data class WidgetSummaryStatistics(
            @SerializedName("cells")
            val cells: List<Cell>,
            @SerializedName("summary")
            val summary: Summary
        ) {
            data class Cell(
                @SerializedName("click")
                val click: Int,
                @SerializedName("click_fmt")
                val clickFmt: String,
                @SerializedName("cost")
                val cost: Int,
                @SerializedName("cost_fmt")
                val costFmt: String,
                @SerializedName("date")
                val date: String,
                @SerializedName("day")
                val day: Int,
                @SerializedName("impression")
                val impression: Int,
                @SerializedName("impression_fmt")
                val impressionFmt: String,
                @SerializedName("income")
                val income: Int,
                @SerializedName("income_fmt")
                val incomeFmt: String,
                @SerializedName("month")
                val month: Int,
                @SerializedName("roas")
                val roas: Int,
                @SerializedName("roas_fmt")
                val roasFmt: String,
                @SerializedName("sold")
                val sold: Int,
                @SerializedName("sold_fmt")
                val soldFmt: String,
                @SerializedName("year")
                val year: Int
            )

            data class Summary(
                @SerializedName("click_percent")
                var clickPercent: Int,
                @SerializedName("click_sum")
                val clickSum: Int,
                @SerializedName("impression_percent")
                val impressionPercent: Int,
                @SerializedName("impression_sum")
                val impressionSum: Int,
                @SerializedName("income_percent")
                val incomePercent: Int,
                @SerializedName("income_sum")
                val incomeSum: Int,
                @SerializedName("last_update")
                val lastUpdate: String,
                @SerializedName("roas")
                val roasSum: Int,
                @SerializedName("roas_percent")
                val roasPercent: Int,
                @SerializedName("spending_percent")
                val spendingPercent: Int,
                @SerializedName("spending_sum")
                val spendingSum: Int,
                @SerializedName("total_sold_percent")
                val totalSoldPercent: Int,
                @SerializedName("total_sold_sum")
                val totalSoldSum: Int
            )
        }

        data class Header(
            @SerializedName("process_time")
            val processTime: Double
        )
    }
}