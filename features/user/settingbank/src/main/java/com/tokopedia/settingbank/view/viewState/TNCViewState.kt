package com.tokopedia.settingbank.view.viewState

import com.tokopedia.settingbank.domain.TemplateData

sealed class TNCViewState
data class OnTNCSuccess(val templateData: TemplateData): TNCViewState()
data class OnTNCError(val throwable: Throwable):TNCViewState()