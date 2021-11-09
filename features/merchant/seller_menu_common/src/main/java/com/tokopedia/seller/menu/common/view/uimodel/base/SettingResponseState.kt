package com.tokopedia.seller.menu.common.view.uimodel.base

sealed class SettingResponseState<out T: Any> {
    object SettingLoading : SettingResponseState<Nothing>()
    class SettingSuccess<T: Any>(val data: T): SettingResponseState<T>()
    class SettingError(val throwable: Throwable): SettingResponseState<Nothing>()
}