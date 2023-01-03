package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance

// simple logic :
// title : check tag title, then check text title
// description : check tag description, then check text description

data class BalanceDrawerItemModel(
    val applinkContainer: String = "",
    val applinkActionText: String = "",
    val redirectUrl: String = "",
    val iconImageUrl: String? = null,
    val defaultIconRes: Int? = null,
    var balanceTitleTextAttribute: BalanceTextAttribute? = null,
    var balanceSubTitleTextAttribute: BalanceTextAttribute? = null,
    var balanceTitleTagAttribute: BalanceTagAttribute? = null,
    var balanceSubTitleTagAttribute: BalanceTagAttribute? = null,
    val drawerItemType: Int = TYPE_REWARDS,
    val mainPageTitle: String = "",
    var state: Int = STATE_LOADING,
    val trackingAttribute: String = "",
    var alternateBalanceDrawerItem: List<BalanceDrawerItemModel>? = null,
    var balanceCoachmark: BalanceCoachmark? = null,
    val reserveBalance: String = "",
    val headerTitle: String = "",
    val isSubscriberGoToPlus: Boolean = false
) {
    companion object {
        const val TYPE_REWARDS = 4

        // is not linked for any type of wallet
        const val TYPE_WALLET_APP_LINKED = 9
        const val TYPE_WALLET_APP_NOT_LINKED = 11

        // goto plus
        const val TYPE_SUBSCRIPTION = 12

        const val STATE_SUCCESS = 0
        const val STATE_LOADING = 1
        const val STATE_ERROR = 2
    }
}
