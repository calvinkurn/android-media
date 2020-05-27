package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardrevamp

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.AddChildAdapterCallback
import com.tokopedia.discovery2.viewcontrollers.adapter.DiscoveryRecycleAdapter
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment


class ProductCardRevampViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView) {

    private lateinit var mProductCarouselRecyclerView: RecyclerView
    private lateinit var mDiscoveryRecycleAdapter: DiscoveryRecycleAdapter
    private lateinit var mProductRevampComponentViewModel: ProductCardRevampViewModel
    private lateinit var layoutManager: StaggeredGridLayoutManager
    private var addChildAdapterCallback: AddChildAdapterCallback


    init {
        mDiscoveryRecycleAdapter = DiscoveryRecycleAdapter(fragment)
        addChildAdapterCallback = (fragment as AddChildAdapterCallback)
        addChildAdapterCallback.addChildAdapter(mDiscoveryRecycleAdapter)
    }

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        mProductRevampComponentViewModel = discoveryBaseViewModel as ProductCardRevampViewModel
        setUpDataObserver(fragment.viewLifecycleOwner)
        mProductRevampComponentViewModel.fetchProductCarouselData((fragment as DiscoveryFragment).pageEndPoint)
    }

    private fun setUpDataObserver(lifecycleOwner: LifecycleOwner) {
        mProductRevampComponentViewModel.getProductCarouselItemsListData().observe(lifecycleOwner, Observer { item ->
            mDiscoveryRecycleAdapter.setDataList(item)
            addChildAdapterCallback.notifyMergeAdapter()

        })
    }
}