package com.tokopedia.stickylogin.view

interface StickyLoginAction {
    fun onClick()
    fun onDismiss()
    fun onViewChange(isShowing: Boolean)
}