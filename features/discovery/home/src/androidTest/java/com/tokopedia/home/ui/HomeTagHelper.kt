package com.tokopedia.home.ui

import android.content.Context
import com.tokopedia.home.R
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel
import com.tokopedia.searchbar.navigation_component.icons.IconList

object HomeTagHelper {
    fun getGopayBalanceWidgetTag(context: Context): String {
        return String.format(
            context.getString(R.string.tag_balance_widget),
            BalanceDrawerItemModel.TYPE_WALLET_APP_LINKED.toString()
        )
    }

    fun getTokopointsBalanceWidgetTag(context: Context): String {
        return String.format(
            context.getString(R.string.tag_balance_widget),
            BalanceDrawerItemModel.TYPE_REWARDS.toString()
        )
    }

    fun getSubscriptionBalanceWidgetTag(context: Context): String {
        return String.format(
            context.getString(R.string.tag_balance_widget),
            BalanceDrawerItemModel.TYPE_SUBSCRIPTION.toString()
        )
    }

    fun getNotifCounterMessage(context: Context): String {
        return String.format(
            context.getString(com.tokopedia.searchbar.R.string.tag_counter_id), IconList.NAME_MESSAGE
        )
    }

    fun getNotifCounterCart(context: Context): String {
        return String.format(
            context.getString(com.tokopedia.searchbar.R.string.tag_counter_id), IconList.NAME_CART
        )
    }

    fun getNotifGlobalNav(context: Context): String {
        return String.format(
            context.getString(com.tokopedia.searchbar.R.string.tag_counter_id), IconList.NAME_NAV_GLOBAL
        )
    }
}