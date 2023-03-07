package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.balancewidget.atf2

import com.tokopedia.abstraction.base.view.adapter.Visitable

/**
 * Created by frenzel
 */
interface BalanceVisitable : Visitable<BalanceTypeFactory> {
    fun areContentsTheSame(newItem: BalanceVisitable): Boolean
    fun areItemsTheSame(newItem: BalanceVisitable): Boolean
    override fun type(typeFactory: BalanceTypeFactory): Int
}
