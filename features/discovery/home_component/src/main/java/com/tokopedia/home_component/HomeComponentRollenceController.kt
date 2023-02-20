package com.tokopedia.home_component

import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey

/**
 * Created by yfsx on 4/28/21.
 */
object HomeComponentRollenceController {
    private var rollenceHPBDurationValue: String = ""
    private var rollenceHPBDotsInfiniteValue: String = ""
    private var rollenceDynamicIcons: String = ""
    private const val HPB_DURATION_4S = 4000L
    private const val HPB_DURATION_5S = 5000L
    private const val HPB_DURATION_6S = 6000L

    fun fetchHomeComponentRollenceValue() {
        rollenceHPBDurationValue = RemoteConfigInstance.getInstance().abTestPlatform.getString(RollenceKey.HOME_COMPONENT_HPB_DURATION_EXP, RollenceKey.HOME_COMPONENT_HPB_DURATION_CONTROL)
        rollenceHPBDotsInfiniteValue = RemoteConfigInstance.getInstance().abTestPlatform.getString(RollenceKey.HOME_COMPONENT_HPB_DOTS_INFINITE_EXP, RollenceKey.HOME_COMPONENT_HPB_DOTS_INFINITE_CONTROL)
        rollenceDynamicIcons = RemoteConfigInstance.getInstance().abTestPlatform.getString(RollenceKey.HOME_COMPONENT_DYNAMIC_ICON_EXP, "")
    }

    private fun getRollenceValueHPBDotsInfinite(): String {
        return rollenceHPBDotsInfiniteValue.ifEmpty { RollenceKey.HOME_COMPONENT_HPB_DOTS_INFINITE_CONTROL }
    }

    private fun getHPBDurationRollenceValue(): String {
        return rollenceHPBDurationValue.ifEmpty { RollenceKey.HOME_COMPONENT_HPB_DURATION_CONTROL }
    }

    fun isHPBUsingDotsAndInfiniteScroll(): Boolean {
        return getRollenceValueHPBDotsInfinite() == RollenceKey.HOME_COMPONENT_HPB_DOTS_INFINITE_VARIANT
    }

    fun isHomeComponentDynamicIconsUsingRollenceVariant(): Boolean {
        return rollenceDynamicIcons == RollenceKey.HOME_COMPONENT_DYNAMIC_ICON_VARIANT
    }

    fun getHPBDuration(): Long {
        return when (getHPBDurationRollenceValue()) {
            RollenceKey.HOME_COMPONENT_HPB_DURATION_VARIANT_4S -> HPB_DURATION_4S
            RollenceKey.HOME_COMPONENT_HPB_DURATION_VARIANT_6S -> HPB_DURATION_6S
            else -> HPB_DURATION_5S
        }
    }
}
