package com.tokopedia.recharge_component.listener

interface ClientNumberInputFieldListener {
    fun onRenderOperator(isDelayed: Boolean)
    fun onClearInput()
    fun onClickNavigationIcon()
    fun isKeyboardShown(): Boolean
}