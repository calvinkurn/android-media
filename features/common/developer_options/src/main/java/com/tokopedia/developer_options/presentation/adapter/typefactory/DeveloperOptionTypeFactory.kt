package com.tokopedia.developer_options.presentation.adapter.typefactory

import com.tokopedia.developer_options.presentation.model.AccessTokenUiModel
import com.tokopedia.developer_options.presentation.model.PdpDevUiModel
import com.tokopedia.developer_options.presentation.model.SystemNonSystemAppsUiModel

interface DeveloperOptionTypeFactory {
    fun type(uiModel: PdpDevUiModel): Int
    fun type(uiModel: AccessTokenUiModel): Int
    fun type(uiModel: SystemNonSystemAppsUiModel): Int
}