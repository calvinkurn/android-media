package com.tokopedia.play.widget.analytic.const

import com.tokopedia.config.GlobalConfig
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType
import com.tokopedia.play.widget.ui.type.PlayWidgetPromoType
import com.tokopedia.track.TrackApp

/**
 * Created by kenny.hadisaputra on 07/06/22
 */
internal const val KEY_EVENT = "event"
internal const val KEY_EVENT_ACTION = "eventAction"
internal const val KEY_EVENT_CATEGORY = "eventCategory"
internal const val KEY_EVENT_LABEL = "eventLabel"
internal const val KEY_CURRENT_SITE = "currentSite"
internal const val KEY_USER_ID = "userId"
internal const val KEY_BUSINESS_UNIT = "businessUnit"
internal const val KEY_SESSION_IRIS = "sessionIris"

internal const val PROMO_VIEW = "promoView"
internal const val PROMO_CLICK = "promoClick"

internal const val EVENT_VIEW = "viewContentIris"
internal const val EVENT_CLICK = "clickContent"

internal const val VAL_BUSINESS_UNIT = "play"
internal val VAL_CURRENT_SITE =
    if (GlobalConfig.isSellerApp()) "tokopediaseller" else "tokopediamarketplace"

val irisSessionId: String
    get() = TrackApp.getInstance().gtm.irisSessionId

fun PlayWidgetChannelType.toTrackingType() = when (this) {
    PlayWidgetChannelType.Live -> "live"
    PlayWidgetChannelType.Vod -> "vod"
    PlayWidgetChannelType.Upcoming -> "upcoming"
    else -> "null"
}

internal fun PlayWidgetPromoType.toTrackingString(): String {
    return promoText.ifBlank { "no promo" }
}

internal val PlayWidgetPromoType.isRilisanSpesial: Boolean
    get() = when (this) {
        is PlayWidgetPromoType.Default -> isRilisanSpesial
        is PlayWidgetPromoType.LiveOnly -> isRilisanSpesial
        else -> false
    }

fun trackerMultiFields(vararg fields: Any?): String {
    return fields.joinToString(separator = " - ")
}
