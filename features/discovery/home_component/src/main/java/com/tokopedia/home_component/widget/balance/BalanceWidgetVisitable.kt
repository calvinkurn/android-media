package com.tokopedia.home_component.widget.balance

import com.tokopedia.abstraction.base.view.adapter.Visitable

/**
 * Created by frenzel
 */
interface BalanceWidgetVisitable: Visitable<BalanceWidgetTypeFactory> {
    fun areContentsTheSame(newItem: BalanceWidgetVisitable): Boolean
    fun areItemsTheSame(newItem: BalanceWidgetVisitable): Boolean
    override fun type(typeFactory: BalanceWidgetTypeFactory): Int
}
