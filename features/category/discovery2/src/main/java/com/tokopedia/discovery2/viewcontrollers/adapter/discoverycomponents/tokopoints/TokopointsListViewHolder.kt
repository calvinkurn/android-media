package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.tokopoints

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.DiscoveryRecycleAdapter
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder

class TokopointsListViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView) {

    private lateinit var mTokopointsRecyclerView: RecyclerView
    private lateinit var mDiscoveryRecycleAdapter: DiscoveryRecycleAdapter

    override fun bindView( discoveryBaseViewModel: DiscoveryBaseViewModel) {
        initView(itemView)
    }

    private fun initView(itemView: View) {
        mTokopointsRecyclerView = itemView.findViewById(R.id.discovery_recyclerView)
        mTokopointsRecyclerView.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        mDiscoveryRecycleAdapter = DiscoveryRecycleAdapter(fragment)
        mTokopointsRecyclerView.adapter = mDiscoveryRecycleAdapter
    }
}