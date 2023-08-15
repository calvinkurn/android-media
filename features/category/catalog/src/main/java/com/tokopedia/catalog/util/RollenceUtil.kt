package com.tokopedia.catalog.util

import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey

object RollenceUtil {
    fun useCatalogReimagine(): Boolean {
        val remoteConfigRollenceValue = RemoteConfigInstance.getInstance().abTestPlatform
            .getString(RollenceKey.USE_CATALOG_REIMAGINE, RollenceKey.USE_CATALOG_REIMAGINE_ENABLED_VALUE)
        return (remoteConfigRollenceValue == RollenceKey.USE_CATALOG_REIMAGINE_ENABLED_VALUE)
    }
}
