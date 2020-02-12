package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.tokopoints

import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.DiscoveryRecycleAdapter
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment

class TokopointsViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView) {

    private lateinit var mTokopointsRecyclerView: RecyclerView
    private lateinit var mTokopointsTitleTextView: TextView
    private lateinit var mDiscoveryRecycleAdapter: DiscoveryRecycleAdapter
    private lateinit var mTokopointsComponentViewModel: TokopointsViewModel

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        mTokopointsComponentViewModel = discoveryBaseViewModel as TokopointsViewModel
        initView()
        setUpDataObserver(fragment.viewLifecycleOwner)
        mTokopointsComponentViewModel.fetchTokopointsListData((fragment as DiscoveryFragment).pageEndPoint)
    }

    private fun initView() {
        mTokopointsRecyclerView = itemView.findViewById(R.id.tokopoints_rv)
        mTokopointsTitleTextView = itemView.findViewById(R.id.tokopoints_title_tv)
        mTokopointsRecyclerView.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        mDiscoveryRecycleAdapter = DiscoveryRecycleAdapter(fragment)
        mTokopointsRecyclerView.adapter = mDiscoveryRecycleAdapter
    }

    private fun setUpDataObserver(lifecycleOwner: LifecycleOwner) {
        mTokopointsComponentViewModel.getTokopointsItemsListData().observe(lifecycleOwner, Observer { item ->
            mDiscoveryRecycleAdapter.setDataList(item)
        })

        mTokopointsComponentViewModel.getTokopointsComponentData().observe(lifecycleOwner, Observer {
            if (it.title.isNullOrEmpty()) {
                mTokopointsTitleTextView.visibility = View.GONE
            } else {
                mTokopointsTitleTextView.visibility = View.VISIBLE
                mTokopointsTitleTextView.text = it.title
            }
        })
    }
}