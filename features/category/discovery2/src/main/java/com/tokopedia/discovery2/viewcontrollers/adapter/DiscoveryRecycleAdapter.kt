package com.tokopedia.discovery2.viewcontrollers.adapter

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryListViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.factory.DiscoveryHomeFactory
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.interfaces.BannerListener

class DiscoveryRecycleAdapter(val fragment: Fragment)
    : RecyclerView.Adapter<AbstractViewHolder>() {

    var viewModel: DiscoveryListViewModel

    init {
        viewModel = ViewModelProviders.of(fragment).get(DiscoveryListViewModel::class.java)
    }

    private val componentList: ArrayList<ComponentsItem> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder {
        return DiscoveryHomeFactory.createViewHolder(parent, viewType) as AbstractViewHolder
    }

    override fun onBindViewHolder(holder: AbstractViewHolder, position: Int) {
        holder.bindView(fragment, viewModel.getViewModelList
        (DiscoveryHomeFactory.createViewModel(getItemViewType(position))!!, componentList[position], position))
    }

    override fun getItemCount(): Int {
        return componentList.size
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