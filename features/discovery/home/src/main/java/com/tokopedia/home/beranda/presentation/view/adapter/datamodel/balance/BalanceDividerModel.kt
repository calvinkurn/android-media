package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance

import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.balancewidget.BalanceTypeFactory
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.balancewidget.BalanceVisitable

/**
 * Created by dhaba
 */
class BalanceDividerModel : BalanceVisitable {
    override fun areContentsTheSame(newItem: BalanceVisitable): Boolean {
        return this.areItemsTheSame(newItem)
    }

    override fun areItemsTheSame(newItem: BalanceVisitable): Boolean {
        return newItem is BalanceDividerModel
    }

    override fun type(typeFactory: BalanceTypeFactory): Int {
        return typeFactory.type(this)
    }
}
