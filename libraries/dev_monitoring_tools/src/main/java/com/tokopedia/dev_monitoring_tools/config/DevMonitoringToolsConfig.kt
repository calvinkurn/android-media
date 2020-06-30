package com.tokopedia.dev_monitoring_tools.config

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DevMonitoringToolsConfig(

    @SerializedName("too_large_tool_min_size_log")
    @Expose
    val tooLargeToolMinSizeLog: Int = 200000,
    @SerializedName("user_journey_size")
    @Expose
    val userJourneySize: Int = 5,
    @SerializedName("anr_ignore_list")
    @Expose
    val anrIgnoreList: List<String> = arrayListOf("BlockCanary")
)