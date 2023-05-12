package com.tokopedia.recharge_component.listener

import com.tokopedia.recharge_component.model.check_balance.RechargeCheckBalanceOTPBottomSheetModel

interface ClientNumberCheckBalanceListener {
    fun onClickCheckBalance(model: RechargeCheckBalanceOTPBottomSheetModel)
}
