package com.tokopedia.discovery.common.reimagine

import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey
import com.tokopedia.remoteconfig.RollenceKey.SEARCH_1_INST_AUTO
import javax.inject.Inject

class ReimagineRollence @Inject constructor() {

    private fun getABTestPlatform(): RemoteConfig? =
        try { RemoteConfigInstance.getInstance().abTestPlatform } catch (_: Exception) { null }

    private fun getVariant(experiment: String) =
        getABTestPlatform()?.getString(experiment, "") ?: ""

    fun search1InstAuto(): Search1InstAuto =
        Search1InstAuto.fromVariant(getVariant(SEARCH_1_INST_AUTO))

    fun search2Component(): Search2Component =
        Search2Component.fromVariant(getVariant(RollenceKey.PRODUCT_CARD_SRE_2024))

    fun search3ProductCard(): Search3ProductCard =
        Search3ProductCard.fromVariant(getVariant(RollenceKey.PRODUCT_CARD_SRE_2024))
}
