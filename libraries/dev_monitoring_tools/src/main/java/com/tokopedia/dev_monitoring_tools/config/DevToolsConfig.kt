package com.tokopedia.dev_monitoring_tools.config

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DevToolsConfig(

    @SerializedName("too_large_tool_min_size_log")
    @Expose
    val tooLargeToolMinSizeLog: Long = 20000
)