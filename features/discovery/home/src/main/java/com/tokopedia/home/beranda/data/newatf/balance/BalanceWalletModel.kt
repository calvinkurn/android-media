package com.tokopedia.home.beranda.data.newatf.balance

import com.tokopedia.home_component.widget.common.DataStatus
import com.tokopedia.navigation_common.usecase.pojo.walletapp.Balances

data class BalanceWalletModel (
    val data: Balances? = null,
    override val state: DataStatus,
): BalanceItemModel {
    override val type: String
        get() = BalanceItemModel.GOPAY
}
