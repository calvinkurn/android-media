package com.tokopedia.discovery2.viewcontrollers.customview

sealed class CustomViewState {
    class ShowText(val value: Any): CustomViewState()
    object HideView : CustomViewState()
}