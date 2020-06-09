package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.claimcoupon

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.discovery2.ClaimCouponConstant.Companion.DOUBLE_COLUMNS
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.DiscoveryRecycleAdapter
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment

class ClaimCouponViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView) {


    private val recyclerView: RecyclerView = itemView.findViewById(R.id.claim_coupon_rv)
    private var discoveryRecycleAdapter: DiscoveryRecycleAdapter = DiscoveryRecycleAdapter(fragment)
    private var spanCount = 1

    private lateinit var claimCouponViewModel: ClaimCouponViewModel

    init {
        recyclerView.adapter = discoveryRecycleAdapter
    }

    private fun addShimmer(isDouble: Boolean) {
        val height = if(isDouble)
            200 else 290
        val list : ArrayList<ComponentsItem> = ArrayList()
        list.add(ComponentsItem(name = "shimmer", shimmerHeight = height))
        list.add(ComponentsItem(name = "shimmer", shimmerHeight = height))
        discoveryRecycleAdapter.setDataList(list)
    }

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {

        claimCouponViewModel = discoveryBaseViewModel as ClaimCouponViewModel
        if (claimCouponViewModel.components.properties?.columns == DOUBLE_COLUMNS) {
            spanCount = 2
            addShimmer(true)
        } else
            addShimmer(false)
        recyclerView.layoutManager = GridLayoutManager(itemView.context, spanCount, GridLayoutManager.VERTICAL, false)
        fragment as DiscoveryFragment
        claimCouponViewModel.getComponentList().observe(fragment.viewLifecycleOwner, Observer { item ->
            fragment.getDiscoveryAnalytics().trackEventImpressionCoupon(item)
            discoveryRecycleAdapter.setDataList(item)
        })

    }

}
