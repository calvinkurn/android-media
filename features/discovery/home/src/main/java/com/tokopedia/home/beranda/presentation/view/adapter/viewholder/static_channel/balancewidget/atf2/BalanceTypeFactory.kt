package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.balancewidget.atf2

import android.view.ViewGroup
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceAtf2DividerModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.balancewidget.BaseBalanceViewHolder

/**
 * Created by frenzel
 */
interface BalanceTypeFactory {
    fun type(visitable: BalanceDrawerItemModel): Int
    fun type(visitable: BalanceAtf2DividerModel): Int
    fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): BaseBalanceViewHolder<BalanceVisitable>
}
