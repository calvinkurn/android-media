package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.tabs

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.design.list.adapter.SpaceItemDecoration
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.AddChildAdapterCallback
import com.tokopedia.discovery2.viewcontrollers.adapter.DiscoveryRecycleAdapter
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.setMargin

class TabsViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView) {
    private val tabsRecyclerView: RecyclerView = itemView.findViewById(R.id.bannerRecyclerView)
    private lateinit var tabsViewModel: TabsViewModel
    private val tabsRecyclerViewAdapter: DiscoveryRecycleAdapter = DiscoveryRecycleAdapter(fragment, this)

    private var compositeAdapter: DiscoveryRecycleAdapter = DiscoveryRecycleAdapter(fragment)
    private var addChildAdapterCallback: AddChildAdapterCallback

    init {
        attachRecyclerView()
        addChildAdapterCallback = (fragment as AddChildAdapterCallback)
        addChildAdapterCallback.addChildAdapter(compositeAdapter)
    }

    private fun attachRecyclerView() {
        tabsRecyclerView.apply {
            setMargin(resources.getDimensionPixelSize(R.dimen.dp_12),
                    resources.getDimensionPixelSize(R.dimen.dp_8),
                    resources.getDimensionPixelSize(R.dimen.dp_12),
                    resources.getDimensionPixelSize(R.dimen.dp_8))
            adapter = tabsRecyclerViewAdapter
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(SpaceItemDecoration(context.resources.getDimensionPixelSize(R.dimen.dp_4), LinearLayoutManager.HORIZONTAL))
        }
    }

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        tabsViewModel = discoveryBaseViewModel as TabsViewModel
        setUpObservers()
    }


    private fun setUpObservers() {
        tabsViewModel.getListDataLiveData().observe(fragment.viewLifecycleOwner, Observer {
            tabsRecyclerViewAdapter.setDataList(it)
        })
    }

    fun getCompositeComponentsList(compositeList: List<ComponentsItem>) {
        compositeAdapter.setDataList(compositeList as ArrayList<ComponentsItem>)
        addChildAdapterCallback.notifyMergeAdapter()
    }
}