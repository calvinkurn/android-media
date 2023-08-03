package com.tokopedia.play.widget.analytic.const

import com.tokopedia.config.GlobalConfig
import com.tokopedia.content.analytic.CurrentSite
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType
import com.tokopedia.play.widget.ui.type.PlayWidgetPromoType
import com.tokopedia.track.TrackApp

/**
 * Created by kenny.hadisaputra on 07/06/22
 */
internal val VAL_CURRENT_SITE =
    if (GlobalConfig.isSellerApp()) CurrentSite.tokopediaSeller else CurrentSite.tokopediaMarketplace

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
