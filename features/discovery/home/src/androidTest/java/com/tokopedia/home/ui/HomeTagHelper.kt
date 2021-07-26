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

    fun getOvoBalanceWidgetTag(context: Context): String {
        return String.format(
            context.getString(R.string.tag_balance_widget),
            BalanceDrawerItemModel.TYPE_WALLET_OVO.toString()
        )
    }

    fun getTokopointBalanceWidgetTag(context: Context): String {
        return String.format(
            context.getString(R.string.tag_balance_widget),
            BalanceDrawerItemModel.TYPE_TOKOPOINT.toString()
        )
    }

    fun getCouponBalanceWidgetTag(context: Context): String {
        return String.format(
            context.getString(R.string.tag_balance_widget),
            BalanceDrawerItemModel.TYPE_COUPON.toString()
        )
    }

    fun getBBOBalanceWidgetTag(context: Context): String {
        return String.format(
            context.getString(R.string.tag_balance_widget),
            BalanceDrawerItemModel.TYPE_FREE_ONGKIR.toString()
        )
    }

    fun getNotifCounterMessage(context: Context): String {
        return String.format(
            context.getString(R.string.tag_counter_id), IconList.NAME_MESSAGE
        )
    }

    fun getNotifCounterCart(context: Context): String {
        return String.format(
            context.getString(R.string.tag_counter_id), IconList.NAME_CART
        )
    }

    fun getNotifGlobalNav(context: Context): String {
        return String.format(
            context.getString(R.string.tag_counter_id), IconList.NAME_NAV_GLOBAL
        )
    }
}