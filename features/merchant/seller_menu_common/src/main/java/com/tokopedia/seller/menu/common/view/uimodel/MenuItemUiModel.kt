package com.tokopedia.seller.menu.common.view.uimodel

import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.seller.menu.common.analytics.SettingTrackingConstant
import com.tokopedia.seller.menu.common.view.typefactory.OtherMenuTypeFactory
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingShopInfoClickTrackable
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingShopInfoImpressionTrackable
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingUiModel

open class MenuItemUiModel(
    open val title: String = "",
    open val drawableReference: Int? = null,
    private val clickApplink: String? = null,
    eventActionSuffix: String = "",
    settingTypeInfix: String = "",
    open val trackingAlias: String? = null,
    open val iconUnify: Int? = null,
    open var notificationCount: Int = 0,
    val isNewItem: Boolean = false,
    open val clickAction: () -> Unit = {}
) : SettingUiModel, SettingShopInfoImpressionTrackable, SettingShopInfoClickTrackable {

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
                trackingAlias?.run {
                    "${SettingTrackingConstant.IMPRESSION} $settingType - $trackingAlias" }.toBlankOrString()
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
        get() = drawableReference == null && iconUnify == null

    override fun type(typeFactory: OtherMenuTypeFactory): Int =
            typeFactory.type(this)
}