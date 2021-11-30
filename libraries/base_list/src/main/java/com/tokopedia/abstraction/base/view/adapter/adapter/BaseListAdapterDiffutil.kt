package com.tokopedia.abstraction.base.view.adapter.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.HideViewHolder

/**
 * Created by Yehezkiel on 06/05/21
 */
abstract class BaseListAdapterDiffutil<F : AdapterTypeFactory>(asyncDifferConfig: AsyncDifferConfig<Visitable<*>>,
                                                               private val adapterTypeFactory: F) : ListAdapter<Visitable<*>, AbstractViewHolder<*>>(asyncDifferConfig) {

    companion object {
        const val PAYLOAD_KEY_EXTRA = "payload"
    }

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
            bind(holder, getItem(position), payloads)
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    fun bind(holder: AbstractViewHolder<*>, item: Visitable<*>, payloads: List<Any?>) {
        val payloadInt = (payloads.firstOrNull() as? Bundle)?.getInt(PAYLOAD_KEY_EXTRA)
        if (payloads.isNotEmpty() && payloads.firstOrNull() != null && payloadInt != null) {
            (holder as AbstractViewHolder<Visitable<*>>).bind(item, listOf(payloadInt))
        } else {
            (holder as AbstractViewHolder<Visitable<*>>).bind(item)
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