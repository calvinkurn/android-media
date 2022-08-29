package com.tokopedia.recharge_component.listener

interface ClientNumberInputFieldListener {
    fun onRenderOperator(isDelayed: Boolean, isManualInput: Boolean)
    fun onClearInput()
    fun onClickNavigationIcon()
    fun isKeyboardShown(): Boolean
}