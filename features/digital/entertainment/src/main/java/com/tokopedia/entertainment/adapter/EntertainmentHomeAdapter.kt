package com.tokopedia.entertainment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.HideViewHolder
import com.tokopedia.entertainment.adapter.factory.HomeTypeFactoryImpl

/**
 * Author errysuprayogi on 29,January,2020
 */
class EntertainmentHomeAdapter(val typeFactory: HomeTypeFactoryImpl, var items: List<out HomeItem<*>>) :
        RecyclerView.Adapter<HomeViewHolder<*>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder<*> {
        val view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false)
        return typeFactory.createViewHolder(view as ViewGroup, viewType)
    }

    override fun getItemViewType(position: Int): Int {
        return items.get(position).type(typeFactory)
    }

    override fun onBindViewHolder(holder: HomeViewHolder<*>, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int {
        return items.size
    }
}