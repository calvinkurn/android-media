package com.tokopedia.settingbank.banklist.v2.view.viewState

import com.tokopedia.settingbank.banklist.v2.domain.TemplateData

sealed class TNCViewState
data class OnTNCSuccess(val templateData: TemplateData): TNCViewState()
data class OnTNCError(val throwable: Throwable):TNCViewState()