package com.tokopedia.discovery2.viewcontrollers.adapter

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryListViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.factory.DiscoveryHomeFactory
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import timber.log.Timber
import java.util.concurrent.atomic.AtomicInteger

class DiscoveryRecycleAdapter(private val fragment: Fragment, private val parentComponent: AbstractViewHolder? = null)
    : RecyclerView.Adapter<AbstractViewHolder>() {

    companion object {
        private var noOfObject = 0
    }

    // To set the common ViewPool for Inner Recycler View to recycle views
    private val viewPool = RecyclerView.RecycledViewPool()
    private val count: AtomicInteger = AtomicInteger(0)

    private val componentList: ArrayList<ComponentsItem> = ArrayList()
    private var viewHolderListModel = ViewModelProviders.of(fragment).get((DiscoveryListViewModel::class.java.canonicalName
            ?: "") + noOfObject++, DiscoveryListViewModel::class.java)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder {
        Timber.d("Inflate view holder ${count.incrementAndGet()}")
        val viewHolder: AbstractViewHolder = DiscoveryHomeFactory.createViewHolder(parent, viewType, fragment) as AbstractViewHolder
        viewHolder.getInnerRecycleView()?.setRecycledViewPool(viewPool)
        return viewHolder
    }

    override fun onBindViewHolder(holder: AbstractViewHolder, position: Int) {
        holder.bindView(viewHolderListModel.getViewHolderModel(
                DiscoveryHomeFactory.createViewModel(getItemViewType(position)), componentList[position], position), parentComponent)
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
        return id ?: 0
    }

    override fun getItemId(position: Int): Long {
        if (!componentList.isNullOrEmpty() && !componentList[position].data.isNullOrEmpty()) {
            return componentList[position].data?.get(0)?.productId?.toLong()!!
        }
        return super.getItemId(position)
    }


    fun setDataList(dataList: ArrayList<ComponentsItem>?) {
        if (dataList != null) {
            componentList.clear()
            viewHolderListModel.clearList()
            componentList.addAll(dataList)
        }
        notifyDataSetChanged()
    }

    override fun onViewAttachedToWindow(holder: AbstractViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.onViewAttachedToWindow()
    }

    override fun onViewDetachedFromWindow(holder: AbstractViewHolder) {
        holder.onViewDetachedToWindow()
        super.onViewDetachedFromWindow(holder)
    }

    fun getChildHolderViewModel(position: Int): DiscoveryBaseViewModel? {
        return viewHolderListModel.getInnerComponentViewModel(position)
    }
}