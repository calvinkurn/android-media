package com.tokopedia.productcard.experiments

import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey.PRODUCT_CARD_SRE_2024
import com.tokopedia.remoteconfig.RollenceKey.REVERSE_PRODUCT_CARD
import com.tokopedia.remoteconfig.RollenceKey.REVERSE_PRODUCT_CARD_V4

object ProductCardExperiment {

    fun getValue(): String =
        try {
            RemoteConfigInstance.getInstance().abTestPlatform.getString(
                PRODUCT_CARD_SRE_2024,
                "",
            )
        } catch (_: Throwable) {
            ""
        }

    fun isReimagine(): Boolean = getValue() == PRODUCT_CARD_SRE_2024
}
