package com.tokopedia.power_merchant.subscribe.view.model

sealed class ViewState {
    object ShowLoading: ViewState()
    object HideLoading: ViewState()
}