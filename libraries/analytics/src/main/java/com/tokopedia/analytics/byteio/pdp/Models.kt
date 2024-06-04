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

    val buttonName: String
        get() = when (this) {
            OCS -> CartRedirButtonName.BUY_NOW.value
            // other cases not defined on this phase
            else -> ""
        }

    companion object {
        fun getBuyType(actionType: Int): AtcBuyType? {
            return AtcBuyType.values().find { it.code == actionType }
        }

        fun getBuyType(cartType: String): AtcBuyType? {
            return AtcBuyType.values().find { it.funnel == cartType }
        }
    }
}

object ThanksDataEventLabel {
    const val REGULAR = "regular checkout"
    const val OCC = "occ"
}

enum class CartRedirButtonName(val value: String) {
    BUY_NOW("buy now");
}
