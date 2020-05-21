package com.tokopedia.discovery2.viewcontrollers.adapter

import android.util.Log
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

    private val viewPool = RecyclerView.RecycledViewPool()

    companion object {
        private var noOfObject = 0
    }

    private var viewHolderListModel = ViewModelProviders.of(fragment).get((DiscoveryListViewModel::class.java.canonicalName
            ?: "") + noOfObject++, DiscoveryListViewModel::class.java)

    private val componentList: ArrayList<ComponentsItem> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder {
        return DiscoveryHomeFactory.createViewHolder(parent, viewType, fragment) as AbstractViewHolder
    }

    override fun onBindViewHolder(holder: AbstractViewHolder, position: Int) {
        Log.d("DiscoonBindViewHolder", position.toString())
        holder.bindView(viewHolderListModel.getViewHolderModel(
                DiscoveryHomeFactory.createViewModel(getItemViewType(position)), componentList[position], position))
//        holder.getInnerRecycleView()?.setRecycledViewPool(viewPool)
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

    fun setDataList(dataList: ArrayList<ComponentsItem>?) {
        if (dataList != null) {
            componentList.clear()
            componentList.addAll(dataList)
        }
        // TODO : Remove notify for horizontal adapter
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
}