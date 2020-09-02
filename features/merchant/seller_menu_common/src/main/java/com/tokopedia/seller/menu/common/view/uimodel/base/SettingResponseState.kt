package com.tokopedia.seller.menu.common.view.uimodel.base

sealed class SettingResponseState {
    object SettingError : SettingResponseState()
    object SettingLoading : SettingResponseState()
}

open class SettingSuccess: SettingResponseState()