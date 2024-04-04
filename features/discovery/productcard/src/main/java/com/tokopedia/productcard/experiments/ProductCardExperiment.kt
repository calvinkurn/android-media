package com.tokopedia.productcard.experiments

import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey.REVERSE_PRODUCT_CARD
import com.tokopedia.remoteconfig.RollenceKey.REVERSE_PRODUCT_CARD_V4

object ProductCardExperiment {

    fun getValue(): String =
        try {
            RemoteConfigInstance.getInstance().abTestPlatform.getString(
                REVERSE_PRODUCT_CARD,
                "",
            )
        } catch (_: Throwable) {
            ""
        }

    fun isReimagine(): Boolean = getValue() != REVERSE_PRODUCT_CARD_V4
}
