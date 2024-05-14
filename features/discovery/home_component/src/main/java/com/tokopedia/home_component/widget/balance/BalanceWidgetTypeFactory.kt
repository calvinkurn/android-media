package com.tokopedia.home_component.widget.balance

/**
 * Created by frenzel
 */
interface BalanceWidgetTypeFactory {
    fun type(visitable: BalanceWidgetUiModel): Int
    fun type(visitable: BalanceWidgetErrorUiModel): Int
    fun type(visitable: BalanceWidgetLoadingUiModel): Int
}
