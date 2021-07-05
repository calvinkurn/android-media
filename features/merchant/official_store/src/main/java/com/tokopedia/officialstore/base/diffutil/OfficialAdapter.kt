package com.tokopedia.officialstore.base.diffutil

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder

/**
 * Created by Lukas on 27/10/20.
 */
abstract class OfficialAdapter<T: Visitable<*>, V : OfficialTypeFactory>(
        private val adapterTypeFactory: V,
        diffUtil: DiffUtil.ItemCallback<T>
) : ListAdapter<T, AbstractViewHolder<Visitable<*>>>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<Visitable<*>> {
        val view = onCreateViewItem(parent, viewType)
        return adapterTypeFactory.createViewHolder(view, viewType)
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<Visitable<*>>, position: Int, payloads: MutableList<Any>) {
        if(payloads.isNotEmpty()){
            bind(holder, getItem(position), payloads)
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    private fun onCreateViewItem(parent: ViewGroup, viewType: Int): View {
        return LayoutInflater.from(parent.context).inflate(viewType, parent, false)
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<Visitable<*>>, position: Int) {
        bind(holder, getItem(position))
    }

    protected abstract fun bind(holder: AbstractViewHolder<Visitable<*>>, item: T)

    protected abstract fun bind(holder: AbstractViewHolder<Visitable<*>>, item: T, payloads: MutableList<Any>)
}