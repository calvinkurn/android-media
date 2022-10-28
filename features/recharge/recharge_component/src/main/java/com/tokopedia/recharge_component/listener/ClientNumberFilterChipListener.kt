package com.tokopedia.recharge_component.listener

import com.tokopedia.recharge_component.model.client_number.RechargeClientNumberChipModel

interface ClientNumberFilterChipListener {
    fun onClickIcon(isSwitchChecked: Boolean)
    fun onShowFilterChip(isLabeled: Boolean)
    fun onClickFilterChip(isLabeled: Boolean, favorite: RechargeClientNumberChipModel)
}