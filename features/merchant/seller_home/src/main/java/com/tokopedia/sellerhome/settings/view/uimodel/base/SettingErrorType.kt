package com.tokopedia.sellerhome.settings.view.uimodel.base

sealed class SettingErrorType {
    object GENERAL_INFO_ERROR : SettingErrorType()
    object BADGES_ERROR : SettingErrorType()
    object FOLLOWERS_ERROR : SettingErrorType()
}