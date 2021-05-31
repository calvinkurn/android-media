package com.tokopedia.home_component

import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.abtest.AbTestPlatform

/**
 * Created by yfsx on 4/28/21.
 */
object HomeComponentRollenceController {

    private var rollenceLego4BannerValue: String = ""
    private var rollenceCategoryWidgetValue: String = ""

    fun fetchHomeComponentRollenceValue() {
        rollenceLego4BannerValue = RemoteConfigInstance.getInstance().abTestPlatform.getString(AbTestPlatform.HOME_COMPONENT_LEGO4BANNER_EXP, AbTestPlatform.HOME_COMPONENT_LEGO4BANNER_OLD)
        rollenceCategoryWidgetValue = RemoteConfigInstance.getInstance().abTestPlatform.getString(AbTestPlatform.HOME_COMPONENT_CATEGORYWIDGET_EXP, AbTestPlatform.HOME_COMPONENT_CATEGORYWIDGET_OLD)
    }

    private fun getRollenceValueLego4Banner() : String {
        return if (rollenceLego4BannerValue.isNotEmpty()) rollenceLego4BannerValue else AbTestPlatform.HOME_COMPONENT_LEGO4BANNER_OLD
    }

    private fun getRollenceValueCategoryWidget() : String {
        return if (rollenceCategoryWidgetValue.isNotEmpty()) rollenceCategoryWidgetValue else AbTestPlatform.HOME_COMPONENT_CATEGORYWIDGET_OLD
    }

    fun isHomeComponentLego4BannerUsingRollenceVariant(): Boolean {
        return HomeComponentRollenceController.getRollenceValueLego4Banner() == AbTestPlatform.HOME_COMPONENT_LEGO4BANNER_VARIANT
    }

    fun isHomeComponentCategoryWidgetRollenceVariant(): Boolean {
        return HomeComponentRollenceController.getRollenceValueCategoryWidget() == AbTestPlatform.HOME_COMPONENT_CATEGORYWIDGET_VARIANT
    }
}