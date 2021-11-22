package com.tokopedia.developer_options.presentation.adapter.typefactory

import com.tokopedia.developer_options.presentation.model.*

interface DeveloperOptionTypeFactory {
    fun type(uiModel: PdpDevUiModel): Int
    fun type(uiModel: AccessTokenUiModel): Int
    fun type(uiModel: SystemNonSystemAppsUiModel): Int
    fun type(uiModel: ResetOnBoardingUiModel): Int
    fun type(uiModel: ForceCrashUiModel): Int
}