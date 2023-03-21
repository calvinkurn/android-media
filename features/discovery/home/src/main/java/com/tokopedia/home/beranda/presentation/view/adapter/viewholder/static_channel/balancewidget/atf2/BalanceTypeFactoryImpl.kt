package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.balancewidget.atf2

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceAtf2DividerModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.balancewidget.BaseBalanceViewHolder

/**
* Created by frenzel
*/
class BalanceTypeFactoryImpl(private val totalItems: Int) : BalanceTypeFactory {
    override fun type(visitable: BalanceDrawerItemModel): Int {
        return BalanceAtf2ViewHolder.LAYOUT
    }

    override fun type(visitable: BalanceAtf2DividerModel): Int {
        return BalanceAtf2DividerViewHolder.LAYOUT
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): BaseBalanceViewHolder<BalanceVisitable> {
        val view = LayoutInflater
            .from(viewGroup.context)
            .inflate(viewType, viewGroup, false)
        return when(viewType) {
            BalanceAtf2DividerViewHolder.LAYOUT -> BalanceAtf2DividerViewHolder(view)
            BalanceAtf2ViewHolder.LAYOUT -> BalanceAtf2ViewHolder(view, totalItems)
            else -> BalanceAtf2ViewHolder(view, totalItems)
        } as BaseBalanceViewHolder<BalanceVisitable>
    }
}
