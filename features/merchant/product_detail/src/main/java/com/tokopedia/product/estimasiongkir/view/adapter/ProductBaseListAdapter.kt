package com.tokopedia.product.estimasiongkir.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.HideViewHolder

/**
 * Created by Yehezkiel on 12/10/20
 */
abstract class ProductBaseListAdapter<F : AdapterTypeFactory>(asyncDifferConfig: AsyncDifferConfig<Visitable<*>>,
                                                                 private val adapterTypeFactory: F) : ListAdapter<Visitable<*>, AbstractViewHolder<*>>(asyncDifferConfig) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<*> {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return adapterTypeFactory.createViewHolder(view, viewType)
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<*>, position: Int) {
        (holder as AbstractViewHolder<Visitable<*>>).bind(getItem(position))
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<*>, position: Int,
                                  payloads: List<Any?>) {
        if (payloads.isNotEmpty()) {
            (holder as AbstractViewHolder<Visitable<*>>).bind(getItem(position), payloads)
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentListAdapter = currentList as List<Visitable<F>>
        return if (position < 0 || position >= currentListAdapter.size) {
            HideViewHolder.LAYOUT
        } else currentListAdapter[position].type(adapterTypeFactory)
    }

    override fun onViewRecycled(holder: AbstractViewHolder<*>) {
        super.onViewRecycled(holder)
        holder.onViewRecycled()
    }
}