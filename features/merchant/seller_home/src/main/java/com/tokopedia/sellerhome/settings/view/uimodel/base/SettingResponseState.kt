package com.tokopedia.sellerhome.settings.view.uimodel.base

sealed class SettingResponseState {
    object SettingError : SettingResponseState()
    object SettingLoading : SettingResponseState()
}

open class SettingSuccess: SettingResponseState()