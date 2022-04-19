package com.tokopedia.topupbills.telco.prepaid.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.factory.BaseListCheckableTypeFactory
import com.tokopedia.abstraction.base.view.adapter.holder.BaseCheckableViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topupbills.telco.data.FilterTagDataCollection
import com.tokopedia.topupbills.telco.prepaid.adapter.viewholder.TelcoFilterViewHolder

class TelcoFilterAdapterTypeFactory(private val interactionListener: BaseCheckableViewHolder.CheckableInteractionListener)
    : BaseAdapterTypeFactory(), BaseListCheckableTypeFactory<FilterTagDataCollection> {

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return if (type == TelcoFilterViewHolder.LAYOUT) {
            TelcoFilterViewHolder(parent, interactionListener)
        } else {
            super.createViewHolder(parent, type)
        }
    }

    override fun type(viewModel: FilterTagDataCollection?): Int {
        return TelcoFilterViewHolder.LAYOUT
    }
}