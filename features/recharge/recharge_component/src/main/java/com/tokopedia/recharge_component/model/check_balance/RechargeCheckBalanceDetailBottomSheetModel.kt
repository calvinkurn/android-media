package com.tokopedia.recharge_component.model.check_balance

data class RechargeCheckBalanceDetailBottomSheetModel(
    val title: String = "",
    val details: List<RechargeCheckBalanceDetailModel> = emptyList()
)
