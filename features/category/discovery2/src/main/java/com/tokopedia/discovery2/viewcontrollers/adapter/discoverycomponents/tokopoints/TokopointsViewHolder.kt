package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.tokopoints

import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.DiscoveryRecycleAdapter
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

private const val SHIMMER_HEIGHT = 290
private const val SHIMMER = "shimmer"

class TokopointsViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView) {

    private var mTokopointsRecyclerView: RecyclerView = itemView.findViewById(R.id.tokopoints_rv)
    private var mTokopointsTitleTextView: TextView = itemView.findViewById(R.id.tokopoints_title_tv)
    private var mDiscoveryRecycleAdapter: DiscoveryRecycleAdapter
    private lateinit var mTokopointsComponentViewModel: TokopointsViewModel

    init {
        mTokopointsRecyclerView.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        mDiscoveryRecycleAdapter = DiscoveryRecycleAdapter(fragment)
        mTokopointsRecyclerView.adapter = mDiscoveryRecycleAdapter
    }

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        mTokopointsComponentViewModel = discoveryBaseViewModel as TokopointsViewModel
        getSubComponent().inject(mTokopointsComponentViewModel)
        init()
    }

    private fun init() {
        setUpDataObserver()
        fetchTokopointsData()
        addShimmer()
    }

    private fun addShimmer() {
        val list: ArrayList<ComponentsItem> = ArrayList()
        list.add(ComponentsItem(name = SHIMMER, shimmerHeight = SHIMMER_HEIGHT))
        list.add(ComponentsItem(name = SHIMMER, shimmerHeight = SHIMMER_HEIGHT))
        mDiscoveryRecycleAdapter.setDataList(list)
    }

    private fun fetchTokopointsData() {
        mTokopointsComponentViewModel.fetchTokopointsListData((fragment as DiscoveryFragment).pageEndPoint)
    }

    private fun setUpDataObserver() {
        val lifecycleOwner = fragment.viewLifecycleOwner
        mTokopointsComponentViewModel.getTokopointsItemsListData().observe(lifecycleOwner, Observer { item ->
            mDiscoveryRecycleAdapter.setDataList(item)
        })

        mTokopointsComponentViewModel.getTokopointsComponentData().observe(lifecycleOwner, Observer {
            if (it.title.isNullOrEmpty()) {
                mTokopointsTitleTextView.hide()
            } else {
                mTokopointsTitleTextView.show()
                mTokopointsTitleTextView.text = it.title
            }
        })
    }
}