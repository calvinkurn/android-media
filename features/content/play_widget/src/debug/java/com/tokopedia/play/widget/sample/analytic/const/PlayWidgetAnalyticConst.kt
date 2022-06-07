package com.tokopedia.play.widget.sample.analytic.const

import com.tokopedia.config.GlobalConfig
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType
import com.tokopedia.play.widget.ui.type.PlayWidgetPromoType

/**
 * Created by kenny.hadisaputra on 07/06/22
 */
internal const val PROMO_VIEW = "promoView"
internal const val PROMO_CLICK = "promoClick"

internal const val EVENT_VIEW = "viewContentIris"
internal const val EVENT_CLICK = "clickContent"

internal const val BUSINESS_UNIT = "play"
internal val CURRENT_SITE =
    if (GlobalConfig.isSellerApp()) "tokopediaseller" else "tokopediamarketplace"

internal fun PlayWidgetChannelType.toTrackingType() = when (this) {
    PlayWidgetChannelType.Live -> "live"
    PlayWidgetChannelType.Vod -> "vod"
    PlayWidgetChannelType.Upcoming -> "upcoming"
    else -> "null"
}

internal fun PlayWidgetPromoType.toTrackingString(): String {
    return promoText.ifBlank { "no promo" }
}

internal fun eventLabel(vararg label: Any): String {
    return label.joinToString(separator = " - ")
}