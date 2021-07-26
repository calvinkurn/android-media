package com.tokopedia.home_component

import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey

/**
 * Created by yfsx on 4/28/21.
 */
object HomeComponentRollenceController {

    private var rollenceLego4BannerValue: String = ""
    private var rollenceCategoryWidgetValue: String = ""

    fun fetchHomeComponentRollenceValue() {
        rollenceLego4BannerValue = RemoteConfigInstance.getInstance().abTestPlatform.getString(RollenceKey.HOME_COMPONENT_LEGO4BANNER_EXP, RollenceKey.HOME_COMPONENT_LEGO4BANNER_OLD)
        rollenceCategoryWidgetValue = RemoteConfigInstance.getInstance().abTestPlatform.getString(RollenceKey.HOME_COMPONENT_CATEGORYWIDGET_EXP, RollenceKey.HOME_COMPONENT_CATEGORYWIDGET_OLD)
    }

    private fun getRollenceValueLego4Banner() : String {
        return if (rollenceLego4BannerValue.isNotEmpty()) rollenceLego4BannerValue else RollenceKey.HOME_COMPONENT_LEGO4BANNER_OLD
    }

    private fun getRollenceValueCategoryWidget() : String {
        return if (rollenceCategoryWidgetValue.isNotEmpty()) rollenceCategoryWidgetValue else RollenceKey.HOME_COMPONENT_CATEGORYWIDGET_OLD
    }

    fun isHomeComponentLego4BannerUsingRollenceVariant(): Boolean {
        return getRollenceValueLego4Banner() == RollenceKey.HOME_COMPONENT_LEGO4BANNER_VARIANT
    }

    fun checkCategoryWidgetRollenceType(isTypeControl:() -> Unit = {}, isTypeTextInside: () -> Unit = {}, isTypeTextBox: () -> Unit = {}) {
        when (getRollenceValueCategoryWidget()) {
            RollenceKey.HOME_COMPONENT_CATEGORYWIDGET_OLD -> isTypeControl.invoke()
            RollenceKey.HOME_COMPONENT_CATEGORYWIDGET_VARIANT_TEXT_BOX -> isTypeTextBox.invoke()
            RollenceKey.HOME_COMPONENT_CATEGORYWIDGET_VARIANT_TEXT_INSIDE -> isTypeTextInside.invoke()
            else -> isTypeControl.invoke()
        }
    }
}