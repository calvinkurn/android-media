package com.tokopedia.sellerorder.list.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by fwidjaja on 2019-08-28.
 */

data class SomListOrderParam(
        @SerializedName("search")
        @Expose
        var search: String = "",

        @SerializedName("start_date")
        @Expose
        var startDate: String = "01/01/2017",

        @SerializedName("end_date")
        @Expose
        var endDate: String = "",

        @SerializedName("filter_status")
        @Expose
        var filterStatus: Int = 999,

        @SerializedName("status_list")
        @Expose
        var statusList: List<Int> = listOf(),

        @SerializedName("sort_by")
        @Expose
        var sortBy: Int = 2
)