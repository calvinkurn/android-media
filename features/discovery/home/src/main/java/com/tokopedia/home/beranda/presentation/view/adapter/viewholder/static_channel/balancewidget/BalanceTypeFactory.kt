package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.balancewidget

import android.view.ViewGroup
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDividerModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel

/**
 * Created by frenzel
 */
interface BalanceTypeFactory {
    fun type(visitable: BalanceDrawerItemModel): Int
    fun type(visitable: BalanceDividerModel): Int
    fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): BaseBalanceViewHolder<BalanceVisitable>
}
