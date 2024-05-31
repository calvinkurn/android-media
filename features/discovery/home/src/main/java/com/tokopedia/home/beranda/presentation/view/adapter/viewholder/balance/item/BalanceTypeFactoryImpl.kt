package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.balance.item

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
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

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            BalanceItemViewHolder.LAYOUT -> BalanceItemViewHolder(parent, listener)
            BalanceItemErrorViewHolder.LAYOUT -> BalanceItemErrorViewHolder(parent, listener)
            BalanceItemLoadingViewHolder.LAYOUT -> BalanceItemLoadingViewHolder(parent)
            AddressViewHolder.LAYOUT -> AddressViewHolder(parent, listener)
            else -> super.createViewHolder(parent, type)
        } as AbstractViewHolder<Visitable<*>>
    }
}
