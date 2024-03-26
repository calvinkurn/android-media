package com.tokopedia.home.util

import com.tokopedia.recommendation_widget_common.byteio.RefreshType

enum class HomeRefreshType {
    PULL_TO_REFRESH,
    RELOAD,
    AUTO_REFRESH,
    FIRST_OPEN,
    ADDRESS_CHANGED,
}

fun HomeRefreshType.toRecomRequestType(): RefreshType {
    return when(this) {
        HomeRefreshType.PULL_TO_REFRESH -> RefreshType.REFRESH
        else -> RefreshType.OPEN
    }
}
