package com.tokopedia.play.widget.ui.mapper

import com.tokopedia.play.widget.data.PlayWidgetPromoLabel
import com.tokopedia.play.widget.ui.type.PlayWidgetPromoType
import javax.inject.Inject

/**
 * Created by jegul on 07/04/21
 */

class PlayWidgetPromoLabelMapper @Inject constructor() {

    fun mapWidgetPromoType(promoLabels: List<PlayWidgetPromoLabel>): PlayWidgetPromoType {
        val promoLabel = promoLabels.firstOrNull { it.type != GIVEAWAY } ?: return PlayWidgetPromoType.NoPromo
        return PlayWidgetPromoType.getByType(promoLabel.type, promoLabel.text)
    }

    fun mapWidgetHasGiveaway(promoLabels: List<PlayWidgetPromoLabel>): Boolean =
        promoLabels.firstOrNull { it.type == GIVEAWAY } != null

    companion object {
        private const val GIVEAWAY = "GIVEAWAY"
    }
}