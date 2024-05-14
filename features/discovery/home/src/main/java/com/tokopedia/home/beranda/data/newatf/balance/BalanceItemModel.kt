package com.tokopedia.home.beranda.data.newatf.balance

import com.tokopedia.home_component.widget.common.DataStatus

data class BalanceItemModel(
    val state: DataStatus,
    val type: String,
    val applink: String = "",
    val url: String = "",
    val imageUrl: String? = null,
    val text: String = "",
    val isLinked: Boolean = false,
) {

    companion object {
        const val GOPAY = "gopay"
        const val REWARDS = "rewards"
        const val ADDRESS = "choose_address"
    }
}
