package com.tokopedia.home_component.widget.balance

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder

/**
 * Created by frenzel
 */
class BalanceWidgetAdapter(
    private val balanceTypeFactory: BalanceTypeFactory
) : ListAdapter<BalanceItemVisitable, AbstractViewHolder<BalanceItemVisitable>>(BalanceWidgetDiffCallback()) {

    @Suppress("TooGenericExceptionCaught")
    fun setItemList(itemList: List<BalanceItemVisitable>) {
        try {
            submitList(itemList)
        } catch (_: Exception) { }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<BalanceItemVisitable> {
        return balanceTypeFactory.onCreateViewHolder(parent, viewType)
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type(balanceTypeFactory)
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<BalanceItemVisitable>, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onBindViewHolder(
        holder: AbstractViewHolder<BalanceItemVisitable>,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if(payloads.isEmpty()) onBindViewHolder(holder, position)
        else holder.bind(getItem(position), payloads)
    }
}
