package com.tokopedia.home_component.widget.balance

import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder

/**
 * Created by frenzel
 */
class BalanceWidgetTypeFactoryImpl: BaseAdapterTypeFactory(), BalanceWidgetTypeFactory {
    override fun type(visitable: BalanceWidgetUiModel): Int {
        return BalanceWidgetViewHolder.LAYOUT
    }

    override fun type(visitable: BalanceWidgetErrorUiModel): Int {
        return BalanceWidgetErrorViewHolder.LAYOUT
    }

    override fun type(visitable: BalanceWidgetLoadingUiModel): Int {
        return BalanceWidgetLoadingViewHolder.LAYOUT
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): AbstractViewHolder<BalanceWidgetVisitable> {
        return when(viewType) {
            BalanceWidgetViewHolder.LAYOUT -> BalanceWidgetViewHolder(viewGroup)
            BalanceWidgetErrorViewHolder.LAYOUT -> BalanceWidgetErrorViewHolder(viewGroup)
            BalanceWidgetLoadingViewHolder.LAYOUT -> BalanceWidgetLoadingViewHolder(viewGroup)
            else -> super.createViewHolder(viewGroup, viewType)
        } as AbstractViewHolder<BalanceWidgetVisitable>
    }

}
