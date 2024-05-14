package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.balance.widget

class BalanceWidgetErrorUiModel: BalanceWidgetVisitable {
    override fun areContentsTheSame(newItem: BalanceWidgetVisitable): Boolean {
        return true
    }

    override fun areItemsTheSame(newItem: BalanceWidgetVisitable): Boolean {
        return newItem is BalanceWidgetErrorUiModel
    }

    override fun type(typeFactory: BalanceWidgetTypeFactory): Int {
        return typeFactory.type(this)
    }
}
