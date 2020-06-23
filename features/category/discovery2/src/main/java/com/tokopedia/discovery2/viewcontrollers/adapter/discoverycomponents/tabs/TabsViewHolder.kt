package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.tabs

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.DiscoveryRecycleAdapter
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.customview.SpaceItemDecoration
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.kotlin.extensions.view.setMargin

class TabsViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {
    private val tabsRecyclerView: RecyclerView = itemView.findViewById(R.id.bannerRecyclerView)
    private lateinit var tabsViewModel: TabsViewModel
    private val tabsRecyclerViewAdapter: DiscoveryRecycleAdapter = DiscoveryRecycleAdapter(fragment, this)
    private lateinit var linearLayoutManager: LinearLayoutManager
    private val tabViewOffSet = 30

    init {
        attachRecyclerView()
    }

    private fun attachRecyclerView() {
        linearLayoutManager = LinearLayoutManager(fragment.activity, LinearLayoutManager.HORIZONTAL, false)
        tabsRecyclerView.apply {
            setMargin(resources.getDimensionPixelSize(R.dimen.dp_12),
                    resources.getDimensionPixelSize(R.dimen.dp_8),
                    resources.getDimensionPixelSize(R.dimen.dp_12),
                    resources.getDimensionPixelSize(R.dimen.dp_8))
            adapter = tabsRecyclerViewAdapter
            layoutManager = linearLayoutManager
            addItemDecoration(SpaceItemDecoration(context.resources.getDimensionPixelSize(R.dimen.dp_4), LinearLayoutManager.HORIZONTAL))
        }
    }

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        tabsViewModel = discoveryBaseViewModel as TabsViewModel
    }


    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        tabsViewModel.getListDataLiveData().observe(fragment.viewLifecycleOwner, Observer {
            tabsRecyclerViewAdapter.setDataList(it)
        })
        tabsViewModel.getSyncPageLiveData().observe(fragment.viewLifecycleOwner, Observer { needResync ->
            if (needResync) {
                tabsRecyclerViewAdapter.notifyDataSetChanged()
                (fragment as DiscoveryFragment).reSync()
            }
        })
    }

    fun onTabClick(id: String, position: Int) {
        linearLayoutManager.scrollToPositionWithOffset(position, tabViewOffSet)
        tabsViewModel.onTabClick(id)
    }
}