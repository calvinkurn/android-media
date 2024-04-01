package com.tokopedia.analytics.byteio.pdp


/**
 * REPURCHASE AND OCS are to be used on later project
 * */
enum class AtcBuyType(val code: Int) {
    INSTANT(0),
    ATC(1),
    REPURCHASE(2),
    OCS(3)
}
