package com.tokopedia.sellerhome.settings.view.uimodel.base

sealed class SettingUiType {
    object BALANCE : SettingUiType()
    object DIVIDER : SettingUiType()
    object MENU_ITEM : SettingUiType()
    object SETTING_TITLE : SettingUiType()

}