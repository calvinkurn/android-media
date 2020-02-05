package com.tokopedia.discovery2.viewcontrollers.adapter

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryListViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.factory.DiscoveryHomeFactory
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder

class DiscoveryRecycleAdapter(private val fragment: Fragment)
    : RecyclerView.Adapter<AbstractViewHolder>() {

    private var viewHolderListModel = ViewModelProviders.of(fragment).get(DiscoveryListViewModel::class.java)

    private val componentList: ArrayList<ComponentsItem> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder {
        return DiscoveryHomeFactory.createViewHolder(parent, viewType) as AbstractViewHolder
    }

    override fun onBindViewHolder(holder: AbstractViewHolder, position: Int) {
        holder.bindView(fragment.viewLifecycleOwner, viewHolderListModel.getViewHolderModel(
                DiscoveryHomeFactory.createViewModel(getItemViewType(position)), componentList[position], position))
    }

    override fun getItemCount(): Int {
        return if (componentList.size > 0) {
            componentList.size
        } else {
            0
        }
    }

    override fun getItemViewType(position: Int): Int {
        val id = DiscoveryHomeFactory.getComponentId(componentList[position].name)
        return id ?: -1
    }

    fun setDataList(dataList: ArrayList<ComponentsItem>?) {
        if (dataList != null) {
            componentList.addAll(dataList)
        }
        notifyDataSetChanged()
    }
}