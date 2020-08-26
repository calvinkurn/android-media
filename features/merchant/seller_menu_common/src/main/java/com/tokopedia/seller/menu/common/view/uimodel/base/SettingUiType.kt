package com.tokopedia.seller.menu.common.view.uimodel.base

sealed class SettingUiType {
    object DIVIDER : SettingUiType()
    object MENU_ITEM : SettingUiType()
    object SETTING_TITLE : SettingUiType()
    object INDENTED_SETTING_TITLE : SettingUiType()
    object SETTING_TITLE_MENU : SettingUiType()
}