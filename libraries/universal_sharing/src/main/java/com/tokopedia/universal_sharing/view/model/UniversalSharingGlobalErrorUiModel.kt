package com.tokopedia.universal_sharing.view.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.universal_sharing.view.bottomsheet.typefactory.UniversalSharingTypeFactory

data class UniversalSharingGlobalErrorUiModel(
    val errorType: ErrorType = ErrorType.ERROR_GENERAL
) : Visitable<UniversalSharingTypeFactory> {
    override fun type(typeFactory: UniversalSharingTypeFactory): Int {
        return typeFactory.type(this)
    }

    enum class ErrorType {
        ERROR_GENERAL, ERROR_NETWORK
    }
}
