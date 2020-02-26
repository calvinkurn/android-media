package com.tokopedia.sellerhome.settings.view.uimodel

sealed class BaseUiModelState {
    object Loading: BaseUiModelState()
    object Error: BaseUiModelState()
    object Success: BaseUiModelState()
    object NoData: BaseUiModelState()
}