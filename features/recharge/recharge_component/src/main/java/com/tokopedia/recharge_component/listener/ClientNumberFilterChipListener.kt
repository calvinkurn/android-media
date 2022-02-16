package com.tokopedia.recharge_component.listener

interface ClientNumberFilterChipListener {
    fun onClickIcon(isSwitchChecked: Boolean)
    fun onShowFilterChip(isLabeled: Boolean)
    fun onClickFilterChip(isLabeled: Boolean)
}