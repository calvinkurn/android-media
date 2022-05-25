package com.tokopedia.usercomponents.explicit.view

interface ExplicitAction {
    fun onPositifClick()
    fun onNegatifClick()
    fun onDismiss()
    fun onViewChange(isShowing: Boolean)
}