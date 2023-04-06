package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance

import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.balancewidget.atf2.BalanceTypeFactory
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.balancewidget.atf2.BalanceVisitable

/**
 * Created by dhaba
 */
class BalanceAtf2DividerModel : BalanceVisitable {
    override fun areContentsTheSame(newItem: BalanceVisitable): Boolean {
        return this.areItemsTheSame(newItem)
    }

    override fun areItemsTheSame(newItem: BalanceVisitable): Boolean {
        return newItem is BalanceAtf2DividerModel
    }

    override fun type(typeFactory: BalanceTypeFactory): Int {
        return typeFactory.type(this)
    }
}
