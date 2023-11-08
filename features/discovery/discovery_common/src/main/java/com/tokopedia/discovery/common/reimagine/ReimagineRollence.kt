package com.tokopedia.discovery.common.reimagine

import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey.SEARCH_1_INST_AUTO
import com.tokopedia.remoteconfig.RollenceKey.SEARCH_2_COMPONENT
import com.tokopedia.remoteconfig.RollenceKey.SEARCH_3_PRODUCT_CARD
import javax.inject.Inject

class ReimagineRollence @Inject constructor() {

    private fun getABTestPlatform(): RemoteConfig? =
        try { RemoteConfigInstance.getInstance().abTestPlatform } catch (_: Exception) { null }

    private fun getVariant(experiment: String) =
        getABTestPlatform()?.getString(experiment, "") ?: ""

    fun search1InstAuto(): Search1InstAuto =
        Search1InstAuto.fromVariant(getVariant(SEARCH_1_INST_AUTO))

    fun search2Component(): Search2Component =
        Search2Component.fromVariant(getVariant(SEARCH_2_COMPONENT))

    fun search3ProductCard(): Search3ProductCard =
        Search3ProductCard.fromVariant(getVariant(SEARCH_3_PRODUCT_CARD))
}
