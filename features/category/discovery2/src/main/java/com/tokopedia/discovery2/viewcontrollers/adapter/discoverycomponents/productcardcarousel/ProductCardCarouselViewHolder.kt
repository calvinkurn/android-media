package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardcarousel

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
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment

class ProductCardCarouselViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView) {

    private var mProductCarouselRecyclerView: RecyclerView
    private var mDiscoveryRecycleAdapter: DiscoveryRecycleAdapter
    private lateinit var mProductCarouselComponentViewModel: ProductCardCarouselViewModel
    private var linearLayoutManager: LinearLayoutManager


    init {
        mProductCarouselRecyclerView = itemView.findViewById(R.id.tokopoints_rv)
        linearLayoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        linearLayoutManager.initialPrefetchItemCount = 4
        mProductCarouselRecyclerView.layoutManager = linearLayoutManager
        mDiscoveryRecycleAdapter = DiscoveryRecycleAdapter(fragment)
        mProductCarouselRecyclerView.adapter = mDiscoveryRecycleAdapter
    }

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        mProductCarouselComponentViewModel = discoveryBaseViewModel as ProductCardCarouselViewModel
        init()
    }

    private fun init() {
        setUpDataObserver(fragment.viewLifecycleOwner)
        mProductCarouselComponentViewModel.fetchProductCarouselData((fragment as DiscoveryFragment).pageEndPoint)
    }

    private fun setUpDataObserver(lifecycleOwner: LifecycleOwner) {
        mProductCarouselComponentViewModel.getProductCarouselItemsListData().observe(lifecycleOwner, Observer { item ->
            mDiscoveryRecycleAdapter.setDataList(item)
        })
    }

    override fun getInnerRecycleView(): RecyclerView? {
        return mProductCarouselRecyclerView
    }
}