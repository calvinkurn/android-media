package com.tokopedia.topupbills.telco.prepaid.adapter

import android.content.Context
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topupbills.telco.prepaid.adapter.viewholder.TelcoProductMccmListViewHolder
import com.tokopedia.topupbills.telco.prepaid.adapter.viewholder.TelcoProductViewHolder

class TelcoProductAdapter(val context: Context,
                          productAdapterFactory: TelcoProductAdapterFactory)
    : BaseListAdapter<Visitable<*>, TelcoProductAdapterFactory>(productAdapterFactory) {

    var selectedPosition = INIT_SELECTED_POSITION
    var selectedMccmPosition = INIT_SELECTED_POSITION

    override fun onBindViewHolder(holder: AbstractViewHolder<out Visitable<*>>, position: Int, payloads: MutableList<Any>) {
        if (holder is TelcoProductViewHolder) {
            holder.adapter = this
        } else if (holder is TelcoProductMccmListViewHolder) {
            holder.selectedMccmPosition = selectedMccmPosition
        }
        super.onBindViewHolder(holder, position, payloads)
    }

    fun renderList(data: List<Visitable<*>>) {
        clearAllElements()
        addElement(data)
    }

    fun selectItemProduct(position: Int) {
        if (selectedPosition > INIT_SELECTED_POSITION) {
            val prevPosition = selectedPosition
            notifyItemChanged(prevPosition)
        }
        selectedPosition = position
        notifyItemChanged(position)
    }

    companion object {
        const val INIT_SELECTED_POSITION = -1
    }
}
