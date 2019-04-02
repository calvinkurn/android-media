package com.tokopedia.topads.dashboard.data.model

/**
 * Created by zulfikarrahman on 11/4/16.
 */
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DataStatistic (

        @SerializedName("summary")
        @Expose
        val summary: Summary = Summary(),

        @SerializedName("cells")
        @Expose
        val cells: List<Cell> = listOf()
)