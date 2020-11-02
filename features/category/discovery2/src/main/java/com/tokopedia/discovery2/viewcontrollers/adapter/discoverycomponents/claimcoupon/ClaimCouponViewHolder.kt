package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.claimcoupon

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.discovery2.Constant.ClaimCouponConstant.DOUBLE_COLUMNS
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.DiscoveryRecycleAdapter
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment

class ClaimCouponViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {


    private val recyclerView: RecyclerView = itemView.findViewById(R.id.claim_coupon_rv)
    private var discoveryRecycleAdapter: DiscoveryRecycleAdapter = DiscoveryRecycleAdapter(fragment)
    private var spanCount = 1

    private lateinit var claimCouponViewModel: ClaimCouponViewModel

    init {
        recyclerView.adapter = discoveryRecycleAdapter
    }

    private fun addShimmer(isDouble: Boolean) {
        val height = if (isDouble)
            200 else 290
        val list: ArrayList<ComponentsItem> = ArrayList()
        list.add(ComponentsItem(name = "shimmer", shimmerHeight = height))
        list.add(ComponentsItem(name = "shimmer", shimmerHeight = height))
        discoveryRecycleAdapter.setDataList(list)
    }

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {

        claimCouponViewModel = discoveryBaseViewModel as ClaimCouponViewModel
        getSubComponent().inject(claimCouponViewModel)
        if (claimCouponViewModel.components.properties?.columns == DOUBLE_COLUMNS) {
            spanCount = 2
            addShimmer(true)
        } else
            addShimmer(false)
        recyclerView.layoutManager = GridLayoutManager(itemView.context, spanCount, GridLayoutManager.VERTICAL, false)

    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        claimCouponViewModel.getComponentList().observe(fragment.viewLifecycleOwner, Observer { item ->
            fragment as DiscoveryFragment
            fragment.getDiscoveryAnalytics().trackEventImpressionCoupon(item)
            discoveryRecycleAdapter.setDataList(item)
        })
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let { claimCouponViewModel.getComponentList().removeObservers(it) }
    }

}
