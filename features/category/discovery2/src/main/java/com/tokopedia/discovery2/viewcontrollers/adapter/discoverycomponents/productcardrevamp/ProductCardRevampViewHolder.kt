package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardrevamp

import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.DiscoveryRecycleAdapter
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import android.widget.Toast
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show


class ProductCardRevampViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView) {

    private lateinit var mProductCarouselRecyclerView: RecyclerView
    private lateinit var mDiscoveryRecycleAdapter: DiscoveryRecycleAdapter
    private lateinit var mProductRevampComponentViewModel: ProductCardRevampViewModel
    private val discoveryProgressBar: ProgressBar = itemView.findViewById(R.id.discovery_progress_bar)

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        mProductRevampComponentViewModel = discoveryBaseViewModel as ProductCardRevampViewModel
        initView()
        setUpDataObserver(fragment.viewLifecycleOwner)
        mProductRevampComponentViewModel.fetchProductCarouselData((fragment as DiscoveryFragment).pageEndPoint)

        if (fragment.last){
            Log.d("page last", fragment.last.toString())
            mProductRevampComponentViewModel.fetchProductCarouselDataSecond((fragment as DiscoveryFragment).pageEndPoint)
            discoveryProgressBar.show()
            fragment.last = false

        }
    }

    private fun initView() {
        mProductCarouselRecyclerView = itemView.findViewById(R.id.tokopoints_rv)
        mProductCarouselRecyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        mDiscoveryRecycleAdapter = DiscoveryRecycleAdapter(fragment)
        mProductCarouselRecyclerView.adapter = mDiscoveryRecycleAdapter

    }

    private fun setUpDataObserver(lifecycleOwner: LifecycleOwner) {
        mProductRevampComponentViewModel.getProductCarouselItemsListData((fragment as DiscoveryFragment).last).observe(lifecycleOwner, Observer { item ->
            mDiscoveryRecycleAdapter.setDataList(item)
        })
    }
}