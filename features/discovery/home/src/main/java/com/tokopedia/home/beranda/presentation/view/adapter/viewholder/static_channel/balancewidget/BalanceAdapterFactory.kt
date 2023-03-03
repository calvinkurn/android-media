package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.balancewidget

import android.view.View
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.BalanceViewHolder
import com.tokopedia.home.beranda.presentation.view.helper.HomeRollenceController

class BalanceAdapterFactory {
    private fun getLayout() : Int {
        return if(HomeRollenceController.isUsingAtf2Variant())
            BalanceAtf2ViewHolder.LAYOUT
        else BalanceViewHolder.LAYOUT
    }

    fun createViewHolder(view: View, totalItems: Int) : BaseBalanceViewHolder {
        return when(getLayout()) {
            BalanceAtf2ViewHolder.LAYOUT -> BalanceAtf2ViewHolder(view)
            BalanceViewHolder.LAYOUT -> BalanceViewHolder(view, totalItems)
            else -> BalanceViewHolder(view, totalItems)
        }
    }
}
