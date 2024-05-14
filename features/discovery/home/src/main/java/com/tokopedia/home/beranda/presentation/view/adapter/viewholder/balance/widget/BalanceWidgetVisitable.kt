package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.balance.widget

import com.tokopedia.abstraction.base.view.adapter.Visitable

/**
 * Created by frenzel
 */
interface BalanceWidgetVisitable: Visitable<BalanceWidgetTypeFactory> {
    fun areContentsTheSame(newItem: BalanceWidgetVisitable): Boolean
    fun areItemsTheSame(newItem: BalanceWidgetVisitable): Boolean
    override fun type(typeFactory: BalanceWidgetTypeFactory): Int
}
