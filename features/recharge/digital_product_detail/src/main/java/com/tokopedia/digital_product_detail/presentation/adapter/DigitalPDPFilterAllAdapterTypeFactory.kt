package com.tokopedia.digital_product_detail.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.factory.BaseListCheckableTypeFactory
import com.tokopedia.abstraction.base.view.adapter.holder.BaseCheckableViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital_product_detail.data.model.data.FilterTagDataCollection
import com.tokopedia.digital_product_detail.databinding.ViewPdpFilterCheckboxBinding
import com.tokopedia.digital_product_detail.presentation.adapter.viewholder.DigitalPDPFilterAllViewHolder

class DigitalPDPFilterAllAdapterTypeFactory (private val interactionListener: BaseCheckableViewHolder.CheckableInteractionListener)
    : BaseAdapterTypeFactory(), BaseListCheckableTypeFactory<FilterTagDataCollection> {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return if (type == DigitalPDPFilterAllViewHolder.LAYOUT) {
            val binding = ViewPdpFilterCheckboxBinding.bind(parent)
            DigitalPDPFilterAllViewHolder(binding, interactionListener)
        } else {
            super.createViewHolder(parent, type)
        }
    }

    override fun type(viewModel: FilterTagDataCollection?): Int {
        return DigitalPDPFilterAllViewHolder.LAYOUT
    }
}