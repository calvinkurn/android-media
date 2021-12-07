package com.tokopedia.broadcaster.log.data.mapper

import com.tokopedia.broadcaster.log.data.entity.NetworkLog
import com.tokopedia.broadcaster.data.uimodel.LoggerUIModel

fun LoggerUIModel.mapToData(): NetworkLog {
    return NetworkLog()
}