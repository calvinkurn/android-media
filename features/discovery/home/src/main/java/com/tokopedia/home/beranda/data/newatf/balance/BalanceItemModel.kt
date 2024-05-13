package com.tokopedia.home.beranda.data.newatf.balance

import com.tokopedia.home_component.widget.common.DataStatus

interface BalanceItemModel {
    val state: DataStatus
    val type: String
        get() = ""

    companion object {
        const val GOPAY = "gopay"
        const val REWARDS = "rewards"
    }
}
