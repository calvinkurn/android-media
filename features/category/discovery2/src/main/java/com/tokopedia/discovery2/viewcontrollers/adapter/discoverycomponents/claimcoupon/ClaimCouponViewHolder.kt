package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.claimcoupon

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.discovery2.Constant.ClaimCouponConstant.DOUBLE_COLUMNS
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.analytics.CouponTrackingMapper.toTrackingProps
import com.tokopedia.discovery2.analytics.CouponTrackingProperties
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

    private var claimCouponViewModel: ClaimCouponViewModel? = null

    init {
        recyclerView.adapter = discoveryRecycleAdapter
    }

    private fun addShimmer(isDouble: Boolean) {
        if (claimCouponViewModel?.components?.isBackgroundPresent == true) return

        val height = if (isDouble) {
            200
        } else {
            290
        }
        val list: ArrayList<ComponentsItem> = ArrayList()
        list.add(ComponentsItem(name = "shimmer", shimmerHeight = height))
        list.add(ComponentsItem(name = "shimmer", shimmerHeight = height))
        discoveryRecycleAdapter.setDataList(list)
    }

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        claimCouponViewModel = discoveryBaseViewModel as ClaimCouponViewModel
        claimCouponViewModel?.let {
            getSubComponent().inject(it)
        }
        if (claimCouponViewModel?.components?.properties?.columns == DOUBLE_COLUMNS) {
            spanCount = 2
            addShimmer(true)
        } else {
            spanCount = 1
            addShimmer(false)
        }
        recyclerView.layoutManager = GridLayoutManager(itemView.context, spanCount, GridLayoutManager.VERTICAL, false)
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        claimCouponViewModel?.getComponentList()?.observe(fragment.viewLifecycleOwner) { item ->
            trackImpression(item)

            discoveryRecycleAdapter.setDataList(item)
        }
    }

    private fun trackImpression(components: ArrayList<ComponentsItem>) {
        val properties = mutableListOf<CouponTrackingProperties>()

        components.forEach { component ->
            properties.addAll(component.toTrackingProps())
        }

        val analytics = (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()

        if (properties.isNotEmpty()) analytics?.trackCouponImpression(properties)
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let { claimCouponViewModel?.getComponentList()?.removeObservers(it) }
    }
}
