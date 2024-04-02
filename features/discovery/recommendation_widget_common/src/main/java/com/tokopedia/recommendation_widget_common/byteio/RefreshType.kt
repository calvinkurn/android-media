package com.tokopedia.recommendation_widget_common.byteio

enum class RefreshType(val value: Int) {
    UNKNOWN(-1),
    OPEN(0),
    REFRESH(1),
    LOAD_MORE(2),
    PUSH(3),
}
