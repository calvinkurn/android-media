package com.tokopedia.home_component

import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey

/**
 * Created by yfsx on 4/28/21.
 */
object HomeComponentRollenceController {

    private var rollenceLego24BannerValue: String = ""

    fun fetchHomeComponentRollenceValue() {
        rollenceLego24BannerValue = RemoteConfigInstance.getInstance().abTestPlatform.getString(RollenceKey.HOME_COMPONENT_LEGO24BANNER_EXP, RollenceKey.HOME_COMPONENT_LEGO24BANNER_OLD)
    }

    private fun getRollenceValueLego24Banner(): String {
        return if (rollenceLego24BannerValue.isNotEmpty()) rollenceLego24BannerValue else RollenceKey.HOME_COMPONENT_LEGO24BANNER_OLD
    }

    fun isHomeComponentLego24BannerUsingRollenceVariant(): Boolean {
        return getRollenceValueLego24Banner() == RollenceKey.HOME_COMPONENT_LEGO24BANNER_VARIANT
    }
}