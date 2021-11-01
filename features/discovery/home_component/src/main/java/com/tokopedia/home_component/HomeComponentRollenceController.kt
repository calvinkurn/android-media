package com.tokopedia.home_component

import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey

/**
 * Created by yfsx on 4/28/21.
 */
object HomeComponentRollenceController {

    private var rollenceLego4BannerValue: String = ""
    private var rollenceLego2BannerValue: String = ""

    fun fetchHomeComponentRollenceValue() {
        rollenceLego4BannerValue = RemoteConfigInstance.getInstance().abTestPlatform.getString(RollenceKey.HOME_COMPONENT_LEGO4BANNER_EXP, RollenceKey.HOME_COMPONENT_LEGO4BANNER_OLD)
        rollenceLego2BannerValue = RemoteConfigInstance.getInstance().abTestPlatform.getString(RollenceKey.HOME_COMPONENT_LEGO2BANNER_EXP, RollenceKey.HOME_COMPONENT_LEGO2BANNER_OLD)
    }

    private fun getRollenceValueLego4Banner(): String {
        return if (rollenceLego4BannerValue.isNotEmpty()) rollenceLego4BannerValue else RollenceKey.HOME_COMPONENT_LEGO4BANNER_OLD
    }

    private fun getRollenceValueLego2Banner(): String {
        return if (rollenceLego2BannerValue.isNotEmpty()) rollenceLego2BannerValue else RollenceKey.HOME_COMPONENT_LEGO2BANNER_OLD
    }

    fun isHomeComponentLego4BannerUsingRollenceVariant(): Boolean {
        return getRollenceValueLego4Banner() == RollenceKey.HOME_COMPONENT_LEGO4BANNER_VARIANT
    }

    fun isHomeComponentLego2BannerUsingRollenceVariant(): Boolean {
        return getRollenceValueLego2Banner() == RollenceKey.HOME_COMPONENT_LEGO2BANNER_VARIANT
    }
}