package com.tokopedia.discovery2.viewcontrollers.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.discovery2.viewcontrollers.adapter.factory.DiscoveryHomeFactory
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder

class DiscoveryRecycleAdapter()
    : RecyclerView.Adapter<AbstractViewHolder<BaseDataModel>>() {

    private val componentList: ArrayList<BaseDataModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<BaseDataModel> {
        val itemViewType: View =
                LayoutInflater.from(parent.context).inflate(viewType, parent, false);
        return DiscoveryHomeFactory.createViewHolder(viewType, itemViewType) as AbstractViewHolder<BaseDataModel>
    }

    override fun getItemCount(): Int {
        return componentList.size
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<BaseDataModel>, position: Int) {
        holder.bindView(componentList[position])
    }

    override fun getItemViewType(position: Int): Int {
        val id = DiscoveryHomeFactory.getComponentId(componentList[position].type())
        return id ?: -1
    }

    fun setDataList(dataList: List<BaseDataModel>) {
        componentList.addAll(dataList)
        notifyDataSetChanged()
    }
}