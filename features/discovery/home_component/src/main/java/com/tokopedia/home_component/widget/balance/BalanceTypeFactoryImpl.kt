package com.tokopedia.home_component.widget.balance

import android.view.ViewGroup
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

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): AbstractViewHolder<BalanceItemVisitable> {
        return when(viewType) {
            BalanceItemViewHolder.LAYOUT -> BalanceItemViewHolder(viewGroup)
            BalanceItemErrorViewHolder.LAYOUT -> BalanceItemErrorViewHolder(viewGroup)
            BalanceItemLoadingViewHolder.LAYOUT -> BalanceItemLoadingViewHolder(viewGroup)
            else -> super.createViewHolder(viewGroup, viewType)
        } as AbstractViewHolder<BalanceItemVisitable>
    }
}
