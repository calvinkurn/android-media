package com.tokopedia.developer_options.presentation.adapter.typefactory

import com.tokopedia.developer_options.presentation.model.*

interface DeveloperOptionTypeFactory {
    fun type(uiModel: PdpDevUiModel): Int
    fun type(uiModel: AccessTokenUiModel): Int
    fun type(uiModel: SystemNonSystemAppsUiModel): Int
    fun type(uiModel: ResetOnBoardingUiModel): Int
    fun type(uiModel: ForceCrashUiModel): Int
    fun type(uiModel: SendFirebaseCrashExceptionUiModel): Int
    fun type(uiModel: OpenScreenRecorderUiModel): Int
    fun type(uiModel: NetworkLogOnNotificationUiModel): Int
    fun type(uiModel: ViewNetworkLogUiModel): Int
    fun type(uiModel: DeviceIdUiModel): Int
    fun type(uiModel: ForceDarkModeUiModel): Int
    fun type(uiModel: TopAdsLogOnNotificationUiModel): Int
    fun type(uiModel: ViewTopAdsLogUiModel): Int
}