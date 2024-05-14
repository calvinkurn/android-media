package com.tokopedia.home_component.widget.balance

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder

/**
 * Created by frenzel
 */
class BalanceTypeFactoryImpl: BaseAdapterTypeFactory(), BalanceTypeFactory {
    override fun type(visitable: BalanceItemUiModel): Int {
        return BalanceItemViewHolder.LAYOUT
    }

    override fun type(visitable: BalanceItemErrorUiModel): Int {
        return BalanceItemErrorViewHolder.LAYOUT
    }

    override fun type(visitable: BalanceItemLoadingUiModel): Int {
        return BalanceItemLoadingViewHolder.LAYOUT
    }

    override fun type(visitable: AddressUiModel): Int {
        return AddressViewHolder.LAYOUT
    }

    override fun onCreateViewHolder(view: View, viewType: Int): AbstractViewHolder<BalanceItemVisitable> {
        return when(viewType) {
            BalanceItemViewHolder.LAYOUT -> BalanceItemViewHolder(view)
            BalanceItemErrorViewHolder.LAYOUT -> BalanceItemErrorViewHolder(view)
            BalanceItemLoadingViewHolder.LAYOUT -> BalanceItemLoadingViewHolder(view)
            else -> super.createViewHolder(view, viewType)
        } as AbstractViewHolder<BalanceItemVisitable>
    }
}
