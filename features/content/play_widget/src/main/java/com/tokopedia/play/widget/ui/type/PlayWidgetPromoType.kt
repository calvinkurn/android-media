package com.tokopedia.play.widget.ui.type

import java.util.*

/**
 * Created by jegul on 06/04/21
 */
sealed class PlayWidgetPromoType {

    abstract val promoText: String

    data class Default(
            override val promoText: String,
            val isRilisanSpesial: Boolean
    ) : PlayWidgetPromoType()

    data class LiveOnly(
            override val promoText: String,
            val isRilisanSpesial: Boolean
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
            return when (type.uppercase(Locale.getDefault())) {
                "SPECIAL_CAMPAIGN_LIVE" -> LiveOnly(promoText, false)
                "SPECIAL_CAMPAIGN" -> Default(promoText, false)
                "RILISAN_SPESIAL_LIVE" -> LiveOnly(promoText, true)
                "RILISAN_SPESIAL" -> Default(promoText, true)
                "DEFAULT" -> Default(promoText, false)
                "ONLY_LIVE" -> LiveOnly(promoText, false)
                "" -> NoPromo
                else -> Unknown
            }
        }
    }
}