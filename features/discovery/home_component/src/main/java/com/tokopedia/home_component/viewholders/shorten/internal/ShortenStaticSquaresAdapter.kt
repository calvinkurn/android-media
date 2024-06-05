@file:Suppress("UNCHECKED_CAST")

package com.tokopedia.home_component.viewholders.shorten.internal

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.viewholders.shorten.factory.ShortenViewFactoryImpl

class ShortenStaticSquaresAdapter(
    private val adapterTypeFactory: ShortenViewFactoryImpl
) : ListAdapter<ShortenVisitable, AbstractViewHolder<Visitable<*>>>(ShortenStaticSquaresDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<Visitable<*>> {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return adapterTypeFactory.createViewHolder(view, viewType) as AbstractViewHolder<Visitable<*>>
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<Visitable<*>>, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onBindViewHolder(
        holder: AbstractViewHolder<Visitable<*>>,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty()) {
            holder.bind(getItem(holder.bindingAdapterPosition), payloads)
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type(adapterTypeFactory)
    }

}
