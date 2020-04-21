package com.tokopedia.product.manage.feature.quickedit.variant.data.model

sealed class ViewState {
    object ShowProgressBar: ViewState()
    object HideProgressBar: ViewState()

    object ShowErrorView: ViewState()
    object HideErrorView: ViewState()
}