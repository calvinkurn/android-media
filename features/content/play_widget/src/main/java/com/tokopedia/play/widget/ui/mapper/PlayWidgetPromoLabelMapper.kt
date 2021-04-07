package com.tokopedia.play.widget.ui.mapper

import com.tokopedia.play.widget.data.PlayWidgetPromoLabel
import com.tokopedia.play.widget.ui.type.PlayWidgetPromoType
import javax.inject.Inject

/**
 * Created by jegul on 07/04/21
 */
class PlayWidgetPromoLabelMapper @Inject constructor() {

    fun mapWidgetPromoType(promoLabels: List<PlayWidgetPromoLabel>): PlayWidgetPromoType {
        val promoLabel = promoLabels.firstOrNull() ?: return PlayWidgetPromoType.NoPromo
        return PlayWidgetPromoType.getByType(promoLabel.type, promoLabel.text)
    }
}