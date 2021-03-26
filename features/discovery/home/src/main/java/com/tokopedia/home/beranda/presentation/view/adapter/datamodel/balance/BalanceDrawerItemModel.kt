package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance

//simple logic :
//title : check tag title, then check text title
//description : check tag description, then check text description

data class BalanceDrawerItemModel(
        val applinkContainer: String = "",
        val applinkActionText: String = "",
        val redirectUrl: String = "",
        val iconImageUrl: String = "",
        val defaultIconRes: Int? = null,
        var balanceTitleTextAttribute: BalanceTextAttribute? = null,
        var balanceSubTitleTextAttribute: BalanceTextAttribute? = null,
        var balanceTitleTagAttribute: BalanceTagAttribute? = null,
        var balanceSubTitleTagAttribute: BalanceTagAttribute? = null,
        val drawerItemType: Int = TYPE_TOKOPOINT,
        val mainPageTitle: String = "",
        var state: Int = STATE_LOADING
) {
    companion object {
        const val TYPE_UNKNOWN = 0

        const val TYPE_TOKOPOINT = 1

        const val TYPE_FREE_ONGKIR = 2

        const val TYPE_COUPON = 3

        const val TYPE_REWARDS = 4

        const val TYPE_WALLET_OVO = 5

        const val TYPE_WALLET_WITH_TOPUP = 6

        const val TYPE_WALLET_OTHER = 7

        //is not linked for any type of wallet
        const val TYPE_WALLET_PENDING_CASHBACK = 8

        const val STATE_SUCCESS = 0
        const val STATE_LOADING = 1
        const val STATE_ERROR = 2
    }

    fun buildDefaultData(titleTagAttributes: BalanceTagAttribute) {
        balanceTitleTagAttribute = titleTagAttributes
    }
}