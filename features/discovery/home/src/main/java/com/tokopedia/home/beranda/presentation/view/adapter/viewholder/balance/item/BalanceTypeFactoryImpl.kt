package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.balance.item

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.beranda.listener.HomeCategoryListener

/**
 * Created by frenzel
 */
class BalanceTypeFactoryImpl(
    private val listener: HomeCategoryListener
): BaseAdapterTypeFactory(), BalanceTypeFactory {
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
            BalanceItemViewHolder.LAYOUT -> BalanceItemViewHolder(view, listener)
            BalanceItemErrorViewHolder.LAYOUT -> BalanceItemErrorViewHolder(view, listener)
            BalanceItemLoadingViewHolder.LAYOUT -> BalanceItemLoadingViewHolder(view)
            else -> super.createViewHolder(view, viewType)
        } as AbstractViewHolder<BalanceItemVisitable>
    }
}
