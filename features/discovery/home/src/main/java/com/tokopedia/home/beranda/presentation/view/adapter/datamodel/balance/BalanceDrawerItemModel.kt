package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance

import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.balancewidget.BalanceTypeFactory
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.balancewidget.BalanceVisitable

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
    val headerTitle: String = "",
    val isSubscriberGoToPlus: Boolean = false,
    val position: Int
) : BalanceVisitable {
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

    override fun areContentsTheSame(newItem: BalanceVisitable): Boolean {
        return newItem == this
    }

    override fun areItemsTheSame(newItem: BalanceVisitable): Boolean {
        return newItem is BalanceDrawerItemModel &&
            newItem.drawerItemType == this.drawerItemType &&
            newItem.state == this.state
    }

    override fun type(typeFactory: BalanceTypeFactory): Int {
        return typeFactory.type(this)
    }
}
