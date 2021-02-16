package com.tokopedia.product.manage.feature.list.view.model

sealed class ViewState {

    object ShowProgressDialog: ViewState()
    object HideProgressDialog: ViewState()
    object ShowLoadingDialog: ViewState()
    object HideLoadingDialog: ViewState()
}