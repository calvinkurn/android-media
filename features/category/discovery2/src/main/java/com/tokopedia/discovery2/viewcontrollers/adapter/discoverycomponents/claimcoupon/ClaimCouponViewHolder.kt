package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.claimcoupon

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.discovery2.ClaimCouponConstant.Companion.DOUBLE_COLUMNS
import com.tokopedia.discovery2.R
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

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {

        claimCouponViewModel = discoveryBaseViewModel as ClaimCouponViewModel
        if (claimCouponViewModel.components.properties?.columns == DOUBLE_COLUMNS) {
            spanCount = 2
        }
        recyclerView.layoutManager = GridLayoutManager(itemView.context, spanCount, GridLayoutManager.VERTICAL, false)
        claimCouponViewModel.getClickCouponData((fragment as DiscoveryFragment).pageEndPoint)
        claimCouponViewModel.getComponentList().observe(fragment.viewLifecycleOwner, Observer { item ->
            fragment.getDiscoveryAnalytics().trackEventImpressionCoupon(item)
            discoveryRecycleAdapter.setDataList(item)
        })

    }

}
