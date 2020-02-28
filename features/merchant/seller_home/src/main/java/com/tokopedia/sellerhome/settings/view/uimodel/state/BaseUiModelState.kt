package com.tokopedia.sellerhome.settings.view.uimodel.state

sealed class BaseUiModelState {
    object Loading: BaseUiModelState()
    object Error: BaseUiModelState()
    object Success: BaseUiModelState()
    object NoData: BaseUiModelState()
}