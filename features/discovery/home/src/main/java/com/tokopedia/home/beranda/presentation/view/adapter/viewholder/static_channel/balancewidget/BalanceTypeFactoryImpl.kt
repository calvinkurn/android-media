package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.balancewidget

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDividerModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel
import com.tokopedia.home.beranda.presentation.view.helper.HomeThematicUtil

/**
* Created by frenzel
*/
class BalanceTypeFactoryImpl(
    private val totalItems: Int,
    private val homeThematicUtil: HomeThematicUtil,
) : BalanceTypeFactory {
    override fun type(visitable: BalanceDrawerItemModel): Int {
        return BalanceViewHolder.LAYOUT
    }

    override fun type(visitable: BalanceDividerModel): Int {
        return BalanceDividerViewHolder.LAYOUT
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): BaseBalanceViewHolder<BalanceVisitable> {
        val view = LayoutInflater
            .from(viewGroup.context)
            .inflate(viewType, viewGroup, false)
        return when(viewType) {
            BalanceDividerViewHolder.LAYOUT -> BalanceDividerViewHolder(view, homeThematicUtil)
            BalanceViewHolder.LAYOUT -> BalanceViewHolder(view, totalItems, homeThematicUtil)
            else -> BalanceViewHolder(view, totalItems, homeThematicUtil)
        } as BaseBalanceViewHolder<BalanceVisitable>
    }
}
