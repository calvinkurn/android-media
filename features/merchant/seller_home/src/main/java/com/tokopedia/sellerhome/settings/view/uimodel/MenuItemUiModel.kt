package com.tokopedia.sellerhome.settings.view.uimodel

import com.tokopedia.sellerhome.settings.analytics.SettingTrackingConstant
import com.tokopedia.sellerhome.settings.view.typefactory.OtherMenuTypeFactory
import com.tokopedia.sellerhome.settings.view.uimodel.base.SettingShopInfoClickTrackable
import com.tokopedia.sellerhome.settings.view.uimodel.base.SettingShopInfoImpressionTrackable
import com.tokopedia.sellerhome.settings.view.uimodel.base.SettingUiModel
import com.tokopedia.sellerhome.settings.view.uimodel.base.SettingUiType

class MenuItemUiModel(val title: String = "",
                      val drawableReference: Int? = null,
                      private val clickApplink: String? = null,
                      eventActionSuffix: String = "",
                      settingTypeInfix: String = "",
                      val clickAction: () -> Unit = {})
    : SettingUiModel, SettingShopInfoImpressionTrackable, SettingShopInfoClickTrackable {

    override val settingUiType: SettingUiType
        get() = SettingUiType.MENU_ITEM

    override val onClickApplink: String?
        get() = clickApplink

    override val impressionEventName: String =
            if (isNoIcon) {
                SettingTrackingConstant.VIEW_SHOP_SETTING_IRIS
            } else {
                super.impressionEventName
            }

    override val impressionEventCategory: String =
            if (isNoIcon) {
                SettingTrackingConstant.SETTINGS
            } else {
                super.impressionEventCategory
            }

    override val impressionEventAction: String =
            if (isNoIcon) {
                var settingType = settingTypeInfix
                if (settingTypeInfix == SettingTrackingConstant.APP_SETTING) {
                    settingType = SettingTrackingConstant.APPLICATION_SETTING
                }
                "${SettingTrackingConstant.IMPRESSION} $settingType - $title"
            } else {
                "${SettingTrackingConstant.IMPRESSION} $eventActionSuffix"
            }

    override val clickEventName: String =
            if(isNoIcon) {
                SettingTrackingConstant.CLICK_SHOP_SETTING
            } else {
                super.clickEventName
            }

    override val clickEventCategory: String =
            if (isNoIcon) {
                SettingTrackingConstant.SETTINGS
            } else {
                super.clickEventCategory
            }

    override val clickEventAction: String =
            if (isNoIcon) {
                "${SettingTrackingConstant.CLICK} $settingTypeInfix - $title"
            } else {
                "${SettingTrackingConstant.CLICK} $eventActionSuffix"
            }

    val isNoIcon: Boolean
        get() = drawableReference == null

    override fun type(typeFactory: OtherMenuTypeFactory): Int =
            typeFactory.type(this)
}