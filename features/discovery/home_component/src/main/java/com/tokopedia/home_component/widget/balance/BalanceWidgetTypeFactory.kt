package com.tokopedia.home_component.widget.balance

import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder

/**
 * Created by frenzel
 */
interface BalanceWidgetTypeFactory {
    fun type(visitable: BalanceWidgetUiModel): Int
    fun type(visitable: BalanceWidgetErrorUiModel): Int
    fun type(visitable: BalanceWidgetLoadingUiModel): Int
    fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): AbstractViewHolder<BalanceWidgetVisitable>
}
