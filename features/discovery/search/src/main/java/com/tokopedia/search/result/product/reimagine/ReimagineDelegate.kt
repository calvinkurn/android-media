package com.tokopedia.search.result.product.reimagine

import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.search.di.scope.SearchScope
import javax.inject.Inject
import javax.inject.Named

@SearchScope
class ReimagineDelegate @Inject constructor(
    @Named(SearchConstant.AB_TEST_REMOTE_CONFIG)
    private val abTestRemoteConfig: RemoteConfig,
): Reimagine {

    override fun isReimagine(): Boolean =
        true



    companion object {
        // TODO:: TEMPORARY ROLLENCE CHECK
        const val TEMP_REIMAGINE_EXP = "reimagine_exp"
        const val TEMP_REIMAGINE_VARIANT_FILTER = "reimagine_filter"
    }
}
