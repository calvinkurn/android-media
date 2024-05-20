package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.balance.widget

import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.balance.item.BalanceItemVisitable

data class BalanceWidgetUiModel (
    val balanceItems: List<BalanceItemVisitable>
): BalanceWidgetVisitable {
    override fun areContentsTheSame(newItem: BalanceWidgetVisitable): Boolean {
        return newItem == this
    }

    override fun areItemsTheSame(newItem: BalanceWidgetVisitable): Boolean {
        return newItem is BalanceWidgetUiModel
    }

    override fun type(typeFactory: BalanceWidgetTypeFactory): Int {
        return typeFactory.type(this)
    }
}
