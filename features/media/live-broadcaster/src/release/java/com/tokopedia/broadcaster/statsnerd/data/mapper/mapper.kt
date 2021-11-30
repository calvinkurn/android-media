package com.tokopedia.broadcaster.statsnerd.data.mapper

import com.tokopedia.broadcaster.statsnerd.data.entity.StatsNerdLog
import com.tokopedia.broadcaster.uimodel.LoggerUIModel

fun LoggerUIModel.mapToData(): StatsNerdLog {
    return StatsNerdLog()
}