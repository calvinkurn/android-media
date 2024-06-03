package com.tokopedia.home.beranda.data.newatf.balance

data class BalanceItemModel(
    val state: Int,
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
        const val STATE_SUCCESS = 0
        const val STATE_LOADING = 1
        const val STATE_ERROR = 2
    }
}
