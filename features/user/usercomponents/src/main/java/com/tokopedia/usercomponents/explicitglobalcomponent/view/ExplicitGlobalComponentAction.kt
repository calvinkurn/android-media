package com.tokopedia.usercomponents.explicitglobalcomponent.view

interface ExplicitGlobalComponentAction {
    fun onPositifClick()
    fun onNegatifClick()
    fun onDismiss()
    fun onViewChange(isShowing: Boolean)
}