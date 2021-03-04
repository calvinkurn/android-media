package com.tokopedia.sellerorder.filter.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerorder.filter.presentation.adapter.viewholder.SomFilterDateViewHolder
import com.tokopedia.sellerorder.filter.presentation.adapter.viewholder.SomFilterEmptyViewHolder
import com.tokopedia.sellerorder.filter.presentation.adapter.viewholder.SomFilterLoadingViewHolder
import com.tokopedia.sellerorder.filter.presentation.adapter.viewholder.SomFilterViewHolder
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterDateUiModel
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterEmptyUiModel
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterUiModel

class SomFilterAdapterTypeFactory(private val filterListener: SomFilterListener) : BaseAdapterTypeFactory(), TypeFactorySomFilterAdapter {

    override fun type(sortUiModel: SomFilterUiModel): Int {
        return SomFilterViewHolder.LAYOUT
    }

    override fun type(sortFilterDateUiModel: SomFilterDateUiModel): Int {
        return SomFilterDateViewHolder.LAYOUT
    }

    override fun type(somFilterEmptyUiModel: SomFilterEmptyUiModel): Int {
        return  SomFilterEmptyViewHolder.LAYOUT
    }

    override fun type(viewModel: LoadingModel): Int {
        return SomFilterLoadingViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            SomFilterViewHolder.LAYOUT -> SomFilterViewHolder(parent, filterListener)
            SomFilterDateViewHolder.LAYOUT -> SomFilterDateViewHolder(parent, filterListener)
            SomFilterEmptyViewHolder.LAYOUT -> SomFilterEmptyViewHolder(parent)
            SomFilterLoadingViewHolder.LAYOUT -> SomFilterLoadingViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}