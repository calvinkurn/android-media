package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.tokopoints

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

class TokopointsListViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView) {

    private lateinit var mTokopointsRecyclerView: RecyclerView
    private lateinit var mDiscoveryRecycleAdapter: DiscoveryRecycleAdapter
    private lateinit var mTokopointsComponentViewModel: TokopointsViewModel

    override fun bindView( discoveryBaseViewModel: DiscoveryBaseViewModel) {
        mTokopointsComponentViewModel = discoveryBaseViewModel as TokopointsViewModel
        initView()
        setUpDataObserver(fragment.viewLifecycleOwner)
        mTokopointsComponentViewModel.fetchTokopointsListData()
    }

    private fun initView() {
        mTokopointsRecyclerView = itemView.findViewById(R.id.tokopoints_rv)
        mTokopointsRecyclerView.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        mDiscoveryRecycleAdapter = DiscoveryRecycleAdapter(fragment)
        mTokopointsRecyclerView.adapter = mDiscoveryRecycleAdapter
    }

    private fun setUpDataObserver(lifecycleOwner: LifecycleOwner) {
        mTokopointsComponentViewModel.getTokopointsMutableListData().observe(lifecycleOwner, Observer { item ->
            mDiscoveryRecycleAdapter.setDataList(item)
        })
    }
}