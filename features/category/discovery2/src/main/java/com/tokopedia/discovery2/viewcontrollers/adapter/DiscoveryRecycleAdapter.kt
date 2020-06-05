package com.tokopedia.discovery2.viewcontrollers.adapter

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryListViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcarditem.ProductCardItemViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.factory.DiscoveryHomeFactory
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder

class DiscoveryRecycleAdapter(private val fragment: Fragment, private val parentComponent: AbstractViewHolder? = null)
    : ListAdapter<ComponentsItem, AbstractViewHolder>(ComponentsDiffCallBacks()) {

    companion object {
        private var noOfObject = 0
    }

    override fun submitList(list: List<ComponentsItem>?) {
        componentList = list as ArrayList<ComponentsItem>
        super.submitList(list)
    }

    // To set the common ViewPool for Inner Recycler View to recycle views
    private val viewPool = RecyclerView.RecycledViewPool()
//    private val count: AtomicInteger = AtomicInteger(0)

    private var componentList: ArrayList<ComponentsItem> = ArrayList()
    private var viewHolderListModel = ViewModelProviders.of(fragment).get((DiscoveryListViewModel::class.java.canonicalName
            ?: "") + noOfObject++, DiscoveryListViewModel::class.java)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder {
//        Timber.d("Inflate view holder ${count.incrementAndGet()}")
        val viewHolder: AbstractViewHolder = DiscoveryHomeFactory.createViewHolder(parent, viewType, fragment) as AbstractViewHolder
        viewHolder.getInnerRecycleView()?.setRecycledViewPool(viewPool)
        return viewHolder
    }

    override fun onBindViewHolder(holder: AbstractViewHolder, position: Int) {
        setViewSpanType(holder)
        holder.bindView(viewHolderListModel.getViewHolderModel(
                DiscoveryHomeFactory.createViewModel(getItemViewType(position)), componentList[position], position), parentComponent)
    }


    override fun getItemViewType(position: Int): Int {
        val id = DiscoveryHomeFactory.getComponentId(componentList[position].name)
        return id ?: 0
    }

    override fun getItemId(position: Int): Long {
        if (!componentList.isNullOrEmpty() && !componentList[position].data.isNullOrEmpty()) {
            return componentList[position].data?.get(0)?.productId?.toLong()!!
        }
        return super.getItemId(position)
    }

    fun addDataList(dataList: ArrayList<ComponentsItem>?) {
        if (dataList != null) {
            componentList.clear()
            viewHolderListModel.clearList()
            componentList.addAll(dataList)
        }
        submitList(dataList)
    }

    fun setDataList(dataList: ArrayList<ComponentsItem>?) {
        addDataList(dataList)
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
            layoutParams.isFullSpan= when (holder) {
                is ProductCardItemViewHolder ->  false
                else -> true
            }
        }

    }
}


    class ComponentsDiffCallBacks : DiffUtil.ItemCallback<ComponentsItem>() {
        override fun areItemsTheSame(oldItem: ComponentsItem, newItem: ComponentsItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ComponentsItem, newItem: ComponentsItem): Boolean {
            return newItem == oldItem
        }


    }