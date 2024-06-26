package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.automatecoupon

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.discovery2.analytics.CouponTrackingMapper.toTrackingProperties
import com.tokopedia.discovery2.analytics.CouponTrackingProperties
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.DiscoveryRecycleAdapter
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.utils.resources.isDarkMode

class GridAutomateCouponViewHolder(
    itemView: View,
    val fragment: Fragment
) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {

    companion object {
        private const val GRID_SPAN_COUNT = 2
    }

    private val automateCouponRv = itemView.findViewById<RecyclerView>(R.id.automate_coupon_rv)

    private val mAdapter: DiscoveryRecycleAdapter
        by lazy {
            DiscoveryRecycleAdapter(fragment)
        }

    private val mLayoutManager: GridLayoutManager
        by lazy {
            GridLayoutManager(
                itemView.context,
                GRID_SPAN_COUNT,
                GridLayoutManager.VERTICAL,
                false
            )
        }

    private var viewModel: ListAutomateCouponViewModel? = null

    init {
        setupRecyclerView()
    }

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        viewModel = discoveryBaseViewModel as? ListAutomateCouponViewModel

        viewModel?.apply {
            getSubComponent().inject(this)

            fetch(itemView.context.isDarkMode())
        }
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        if (lifecycleOwner == null) return

        viewModel?.getComponentList()?.observe(lifecycleOwner) { items ->
            showWidget(items)
            trackImpression(items)
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        if (lifecycleOwner == null) return
        viewModel?.getComponentList()?.removeObservers(lifecycleOwner)

        super.removeObservers(lifecycleOwner)
    }

    private fun showWidget(items: ArrayList<ComponentsItem>) {
        automateCouponRv.show()
        mAdapter.setDataList(items)
    }

    private fun trackImpression(items: ArrayList<ComponentsItem>) {
        val properties = mutableListOf<CouponTrackingProperties>()

        items.forEach {
            properties.add(it.toTrackingProperties())
        }

        val analytics = (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()

        if (properties.isNotEmpty()) analytics?.trackCouponImpression(properties)
    }

    private fun setupRecyclerView() {
        automateCouponRv.apply {
            adapter = mAdapter
            layoutManager = mLayoutManager
        }
    }
}
