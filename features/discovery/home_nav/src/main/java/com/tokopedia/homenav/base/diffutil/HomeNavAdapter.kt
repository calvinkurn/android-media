package com.tokopedia.homenav.base.diffutil

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder

/**
 * Created by Lukas on 20/10/20.
 */
abstract class HomeNavAdapter(
        private val adapterTypeFactory: HomeNavTypeFactory
) : ListAdapter<HomeNavVisitable, AbstractViewHolder<HomeNavVisitable>>(HomeNavDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<HomeNavVisitable> {
        val view = onCreateViewItem(parent, viewType)
        return adapterTypeFactory.createViewHolder(view, viewType)
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<HomeNavVisitable>, position: Int, payloads: MutableList<Any>) {
        if(payloads.isNotEmpty()){
            holder.bind(getItem(position), payloads)
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<HomeNavVisitable>, position: Int) {
        holder.bind(getItem(position))
    }

    private fun onCreateViewItem(parent: ViewGroup, viewType: Int): View {
        return LayoutInflater.from(parent.context).inflate(viewType, parent, false)
    }

    override fun getItemViewType(position: Int): Int = currentList[position].type(adapterTypeFactory)
}