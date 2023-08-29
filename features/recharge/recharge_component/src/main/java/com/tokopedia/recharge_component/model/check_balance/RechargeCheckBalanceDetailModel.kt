package com.tokopedia.recharge_component.model.check_balance

data class RechargeCheckBalanceDetailModel(
    val title: String = "",
    val subtitle: String = "",
    val applink: String = "",
    val buttonText: String = "",
    val subtitleColor: String = "",
    val productId: String = "",
    val productPrice: Double = 0.0
)
