package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.claimcoupon

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.DiscoveryRecycleAdapter
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment

class ClaimCouponViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView) {


    private val recyclerView: RecyclerView = itemView.findViewById(R.id.claim_coupon_rv)
    private var discoveryRecycleAdapter: DiscoveryRecycleAdapter

    private lateinit var claimCouponViewModel: ClaimCouponViewModel


    init {
        recyclerView.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.VERTICAL, false)
        discoveryRecycleAdapter = DiscoveryRecycleAdapter(fragment)
        recyclerView.adapter = discoveryRecycleAdapter


    }

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {

        claimCouponViewModel = discoveryBaseViewModel as ClaimCouponViewModel
        claimCouponViewModel.getClickCouponData((fragment as DiscoveryFragment).pageEndPoint)
        claimCouponViewModel.getComponentList().observe(fragment.viewLifecycleOwner, Observer { item ->
            discoveryRecycleAdapter.setDataList(item)
        })

    }

}
