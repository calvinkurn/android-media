package com.tokopedia.product.manage.feature.cashback.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.manage.feature.cashback.presentation.adapter.viewholder.SetCashbackViewHolder
import com.tokopedia.product.manage.feature.cashback.presentation.adapter.viewmodel.SetCashbackUiModel
import com.tokopedia.product.manage.feature.filter.presentation.widget.SelectClickListener

class SetCashbackAdapterTypeFactory(private val selectClickListener: SelectClickListener) : BaseAdapterTypeFactory(), SetCashbackTypeFactory {

    override fun type(setCashbackUiModel: SetCashbackUiModel): Int {
        return SetCashbackViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            SetCashbackViewHolder.LAYOUT -> SetCashbackViewHolder(parent, selectClickListener)
            else -> super.createViewHolder(parent, type)
        }
    }

}