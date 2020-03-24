package com.tokopedia.sellerhome.settings.view.uimodel

import com.tokopedia.kotlin.model.ImpressHolder
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
                      override val impressionEventLabel: String = "",
                      override val clickEventLabel: String = "",
                      override val impressHolder: ImpressHolder = ImpressHolder(),
                      val clickAction: () -> Unit = {})
    : SettingUiModel, SettingShopInfoImpressionTrackable, SettingShopInfoClickTrackable {

    override val settingUiType: SettingUiType
        get() = SettingUiType.MENU_ITEM

    override val onClickApplink: String?
        get() = clickApplink

    override val impressionEventName: String =
            if (isNoIcon) {
                ""
            } else {
                super.impressionEventName
            }

    override val impressionEventCategory: String =
            if (isNoIcon) {
                ""
            } else {
                super.impressionEventCategory
            }

    override val impressionEventAction: String =
            if (isNoIcon) {
                ""
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