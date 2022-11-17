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
    private const val HPB_DURATION_4S = 4000L
    private const val HPB_DURATION_5S = 5000L
    private const val HPB_DURATION_6S = 6000L

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

    private fun getHPBDurationRollenceValue(): String {
        return rollenceHPBDurationValue.ifEmpty { RollenceKey.HOME_COMPONENT_HPB_DURATION_CONTROL }
    }

    fun isHomeComponentLego24BannerUsingRollenceVariant(): Boolean {
        return getRollenceValueLego24Banner() == RollenceKey.HOME_COMPONENT_LEGO24BANNER_VARIANT
    }

    fun isHPBUsingDotsAndInfiniteScroll(): Boolean {
        return getRollenceValueHPBDotsInfinite() == RollenceKey.HOME_COMPONENT_HPB_DOTS_INFINITE_VARIANT
    }

    fun getHPBDuration(): Long {
        return when(getHPBDurationRollenceValue()) {
            RollenceKey.HOME_COMPONENT_HPB_DURATION_VARIANT_4S -> HPB_DURATION_4S
            RollenceKey.HOME_COMPONENT_HPB_DURATION_VARIANT_6S -> HPB_DURATION_6S
            else -> HPB_DURATION_5S
        }
    }
}
