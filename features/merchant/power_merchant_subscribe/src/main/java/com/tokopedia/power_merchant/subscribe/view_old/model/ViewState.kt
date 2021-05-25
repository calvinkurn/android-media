package com.tokopedia.power_merchant.subscribe.view_old.model

sealed class ViewState {
    object ShowLoading: ViewState()
    object HideLoading: ViewState()
}