package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.balance

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.HideViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.balance.item.BalanceItemVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.balance.item.BalanceTypeFactory
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.balance.widget.BalanceWidgetDiffCallback
import com.tokopedia.home_component.util.recordCrashlytics
import timber.log.Timber

/**
 * Created by frenzel
 */
class BalanceWidgetAdapter(
    private val balanceTypeFactory: BalanceTypeFactory
) : ListAdapter<BalanceItemVisitable, AbstractViewHolder<Visitable<*>>>(BalanceWidgetDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<Visitable<*>> {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return balanceTypeFactory.createViewHolder(view, viewType)
    }

    override fun getItemViewType(position: Int): Int {
        return try {
            if (position < 0 || position >= itemCount) {
                HideViewHolder.LAYOUT
            } else getItem(position).type(balanceTypeFactory)
        } catch (e: Exception) {
            e.recordCrashlytics()
            HideViewHolder.LAYOUT
        }
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<Visitable<*>>, position: Int) {
        try {
            holder.bind(getItem(position))
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    override fun onBindViewHolder(
        holder: AbstractViewHolder<Visitable<*>>,
        position: Int,
        payloads: MutableList<Any>
    ) {
        try {
            if(payloads.isEmpty()) {
                onBindViewHolder(holder, position)
            }
            else {
                holder.bind(getItem(position), payloads)
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }
}
