package com.tokopedia.discovery2.viewcontrollers.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.discovery2.viewcontrollers.adapter.factory.DiscoveryHomeFactoryImpl
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder

class DiscoveryRecycleAdapter(private var discoveryHomeFactory: DiscoveryHomeFactoryImpl)
    : RecyclerView.Adapter<AbstractViewHolder<DiscoveryVisitable>>() {

    private val componentList: ArrayList<DiscoveryVisitable> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<DiscoveryVisitable> {
        val itemViewType: View =
                LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return discoveryHomeFactory.createViewHolder(itemViewType, viewType) as AbstractViewHolder<DiscoveryVisitable>
    }

    override fun getItemCount(): Int {
        return componentList.size
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<DiscoveryVisitable>, position: Int) {
        holder.bindView(componentList[position])
    }

    override fun getItemViewType(position: Int): Int {
        return componentList[position].type(discoveryHomeFactory)
    }

    fun setDataList(dataList: List<DiscoveryVisitable>) {
        componentList.addAll(dataList)
        notifyDataSetChanged()
    }
}