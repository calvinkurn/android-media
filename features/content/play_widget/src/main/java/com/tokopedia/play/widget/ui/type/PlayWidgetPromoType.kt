package com.tokopedia.play.widget.ui.type

import java.util.*

/**
 * Created by jegul on 06/04/21
 */
sealed class PlayWidgetPromoType {

    abstract val promoText: String

    data class Default(
            override val promoText: String
    ) : PlayWidgetPromoType()

    data class LiveOnly(
            override val promoText: String
    ) : PlayWidgetPromoType()

    object Unknown : PlayWidgetPromoType() {

        override val promoText: String
            get() = ""
    }

    object NoPromo : PlayWidgetPromoType() {

        override val promoText: String
            get() = ""
    }

    companion object {

        fun getByType(type: String, promoText: String): PlayWidgetPromoType {
            return when (type.toUpperCase(Locale.getDefault())) {
                "DEFAULT" -> Default(promoText)
                "LIVE_ONLY" -> LiveOnly(promoText)
                "NO_PROMO" -> NoPromo
                else -> Unknown
            }
        }
    }
}