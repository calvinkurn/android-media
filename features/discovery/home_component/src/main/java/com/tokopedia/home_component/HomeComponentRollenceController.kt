package com.tokopedia.home_component

import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey

/**
 * Created by yfsx on 4/28/21.
 */
object HomeComponentRollenceController {
    private var rollenceLego24BannerValue: String = ""
    private var rollenceHPBDurationValue: String = ""
    private var rollenceHPBDotsInfiniteValue: String = ""

    fun fetchHomeComponentRollenceValue() {
        rollenceLego24BannerValue = RemoteConfigInstance.getInstance().abTestPlatform.getString(RollenceKey.HOME_COMPONENT_LEGO24BANNER_EXP, RollenceKey.HOME_COMPONENT_LEGO24BANNER_OLD)
        rollenceHPBDurationValue = RemoteConfigInstance.getInstance().abTestPlatform.getString(RollenceKey.HOME_COMPONENT_HPB_DURATION_EXP, RollenceKey.HOME_COMPONENT_HPB_DURATION_CONTROL)
        rollenceHPBDotsInfiniteValue = RemoteConfigInstance.getInstance().abTestPlatform.getString(RollenceKey.HOME_COMPONENT_HPB_DOTS_INFINITE_EXP, RollenceKey.HOME_COMPONENT_HPB_DOTS_INFINITE_CONTROL)
    }

    private fun getRollenceValueLego24Banner(): String {
        return rollenceLego24BannerValue.ifEmpty { RollenceKey.HOME_COMPONENT_LEGO24BANNER_OLD }
    }

    private fun getRollenceValueHPBDotsInfinite(): String {
        return rollenceHPBDotsInfiniteValue.ifEmpty { RollenceKey.HOME_COMPONENT_HPB_DOTS_INFINITE_CONTROL }
    }

    fun isHomeComponentLego24BannerUsingRollenceVariant(): Boolean {
        return getRollenceValueLego24Banner() == RollenceKey.HOME_COMPONENT_LEGO24BANNER_VARIANT
    }

    fun getHPBDurationRollenceValue(): String {
        return rollenceHPBDurationValue.ifEmpty { RollenceKey.HOME_COMPONENT_HPB_DURATION_CONTROL }
    }

    fun isHPBUsingDotsAndInfiniteScroll(): Boolean {
        return getRollenceValueHPBDotsInfinite() == RollenceKey.HOME_COMPONENT_HPB_DOTS_INFINITE_CONTROL
    }
}
