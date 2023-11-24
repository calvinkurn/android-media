package com.tokopedia.recharge_component.model.check_balance

data class RechargeCheckBalanceOTPModel(
    val subtitle: String = "",
    val label: String = "",
    val bottomSheetModel: RechargeCheckBalanceOTPBottomSheetModel = RechargeCheckBalanceOTPBottomSheetModel()
)
