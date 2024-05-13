package com.tokopedia.home_component.widget.balance

data class BalanceWidgetUiModel (
    val balanceItems: List<BalanceItemVisitable>
): BalanceWidgetVisitable {
    override fun areContentsTheSame(newItem: BalanceWidgetVisitable): Boolean {
        return newItem == this
    }

    override fun areItemsTheSame(newItem: BalanceWidgetVisitable): Boolean {
        // there must be exactly 1 balance widget, so areItemsTheSame is always true.
        return true
    }

    override fun type(typeFactory: BalanceWidgetTypeFactory): Int {
        return typeFactory.type(this)
    }
}
