package com.tokopedia.tkpd.flashsale.domain.entity.enums


private const val TAB_POSITION_UPCOMING = 0
private const val TAB_POSITION_REGISTERED = 1
private const val TAB_POSITION_ONGOING = 2
private const val TAB_POSITION_FINISHED = 3

enum class FlashSaleListPageTab(val position: Int) {
    UPCOMING(TAB_POSITION_UPCOMING),
    REGISTERED(TAB_POSITION_REGISTERED),
    ONGOING(TAB_POSITION_ONGOING),
    FINISHED(TAB_POSITION_FINISHED)
}
