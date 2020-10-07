package com.tokopedia.discovery2.viewcontrollers.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryListViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcarditem.ProductCardItemViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shimmer.ShimmerProductCardViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.factory.ComponentsList
import com.tokopedia.discovery2.viewcontrollers.adapter.factory.DiscoveryHomeFactory
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder

class DiscoveryRecycleAdapter(private val fragment: Fragment, private val parentComponent: AbstractViewHolder? = null)
    : ListAdapter<ComponentsItem, AbstractViewHolder>(ComponentsDiffCallBacks()) {

    companion object {
        private var noOfObject = 0
    }

    private var componentList: ArrayList<ComponentsItem> = ArrayList()
    private var viewHolderListModel = ViewModelProviders.of(fragment).get((DiscoveryListViewModel::class.java.canonicalName
            ?: "") + noOfObject++, DiscoveryListViewModel::class.java)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder {
        val itemView: View =
                LayoutInflater.from(parent.context).inflate(ComponentsList.values()[viewType].id, parent, false)
        return DiscoveryHomeFactory.createViewHolder(itemView, viewType, fragment) as AbstractViewHolder
    }

    override fun onBindViewHolder(holder: AbstractViewHolder, position: Int) {
        if (componentList.size <= position)  //tmp code need this handling to handle multithread enviorment
            return
        setViewSpanType(holder)
        holder.bindView(viewHolderListModel.getViewHolderModel(
                DiscoveryHomeFactory.createViewModel(getItemViewType(position)), componentList[position], position), parentComponent)
    }


    override fun getItemViewType(position: Int): Int {
        if (componentList.size <= position)
            return 0
        val id = DiscoveryHomeFactory.getComponentId(componentList[position].name)
        return id ?: 0
    }

    override fun getItemId(position: Int): Long {
        if (componentList.isNullOrEmpty() || position >= componentList.size || componentList[position].data.isNullOrEmpty()) {
            return super.getItemId(position)
        }
        return componentList[position].data?.get(0)?.productId?.toLong()!!
    }

    fun addDataList(dataList: List<ComponentsItem>) {
        componentList.clear()
        clearListViewModel()
        componentList.addAll(dataList)

        submitList(dataList)
    }

    fun setDataList(dataList: ArrayList<ComponentsItem>?) {
        dataList?.let { componentItemsList ->
            addDataList(componentItemsList as List<ComponentsItem>)
        }
    }

    override fun onViewAttachedToWindow(holder: AbstractViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.onViewAttachedToWindow()
    }

    override fun onViewDetachedFromWindow(holder: AbstractViewHolder) {
        holder.onViewDetachedToWindow()
        super.onViewDetachedFromWindow(holder)
    }

    private fun setViewSpanType(holder: AbstractViewHolder) {

        val layoutParams = holder.itemView.layoutParams
        if (layoutParams is StaggeredGridLayoutManager.LayoutParams) {
            layoutParams.isFullSpan = when (holder) {
                is ProductCardItemViewHolder -> false
                is ShimmerProductCardViewHolder -> false
                else -> true
            }
        }

    }

    private fun clearListViewModel() {
        viewHolderListModel.clearList()
    }

    fun getViewModelAtPosition(position: Int): DiscoveryBaseViewModel? {
        return viewHolderListModel.getViewModelAtPosition(position)
    }

    fun isStickyHeaderView(it: Int): Boolean {
        return DiscoveryHomeFactory.isStickyHeader(getItemViewType(it))
    }
}


class ComponentsDiffCallBacks : DiffUtil.ItemCallback<ComponentsItem>() {
    override fun areItemsTheSame(oldItem: ComponentsItem, newItem: ComponentsItem): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: ComponentsItem, newItem: ComponentsItem): Boolean {
        return newItem == oldItem
    }
}