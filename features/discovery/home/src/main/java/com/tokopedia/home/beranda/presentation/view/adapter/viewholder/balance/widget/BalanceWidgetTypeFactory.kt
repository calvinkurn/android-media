package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.balance.widget

/**
 * Created by frenzel
 */
interface BalanceWidgetTypeFactory {
    fun type(visitable: BalanceWidgetUiModel): Int
    fun type(visitable: BalanceWidgetErrorUiModel): Int
    fun type(visitable: BalanceWidgetLoadingUiModel): Int
    fun type(visitable: LoginWidgetUiModel): Int
}
