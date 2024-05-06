package com.tokopedia.analytics.byteio.pdp

/**
 * REPURCHASE AND OCS are to be used on later project
 * */
enum class AtcBuyType(val code: Int) {
    OCC(0),
    ATC(1),
    REPURCHASE(2),
    OCS(3);

    val funnel: String
        get() = when (this) {
            ATC -> "regular"
            OCC -> "occ"
            OCS -> "ocs"
            REPURCHASE -> "repurchase" // not used in this phase
        }
}

object ThanksDataEventLabel {
    const val REGULAR = "regular checkout"
    const val OCC = "occ"
}
