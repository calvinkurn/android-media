package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.automatecoupon

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.discovery2.analytics.CouponTrackingMapper.toTrackingProperties
import com.tokopedia.discovery2.analytics.CouponTrackingProperties
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.databinding.CarouselAutomateCouponLayoutBinding
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.DiscoveryRecycleAdapter
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.utils.view.binding.viewBinding

class CarouselAutomateCouponViewHolder(
    itemView: View,
    val fragment: Fragment
) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {

    private val binding: CarouselAutomateCouponLayoutBinding?
        by viewBinding()

    private val couponAdapter: DiscoveryRecycleAdapter
        by lazy {
            DiscoveryRecycleAdapter(fragment)
        }

    private val linearLayoutManager: LinearLayoutManager
        by lazy {
            LinearLayoutManager(
                itemView.context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
        }

    private var viewModel: ListAutomateCouponViewModel? = null

    init {
        binding?.setupRecyclerView()
    }
    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        viewModel = discoveryBaseViewModel as? ListAutomateCouponViewModel

        viewModel?.apply {
            getSubComponent().inject(this)
        }
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        if (lifecycleOwner == null) return

        viewModel?.getComponentList()?.observe(lifecycleOwner) { items ->
            binding?.showWidget(items)
            trackImpression(items)
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        if (lifecycleOwner == null) return

        viewModel?.getComponentList()?.removeObservers(lifecycleOwner)

        super.removeObservers(lifecycleOwner)
    }

    private fun CarouselAutomateCouponLayoutBinding.showWidget(items: ArrayList<ComponentsItem>) {
        automateCouponRv.show()
        couponAdapter.setDataList(items)
    }

    private fun trackImpression(items: ArrayList<ComponentsItem>) {
        val properties = mutableListOf<CouponTrackingProperties>()

        items.forEach {
            properties.add(it.toTrackingProperties())
        }

        val analytics = (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()

        if (properties.isNotEmpty()) analytics?.trackCouponImpression(properties)
    }

    private fun CarouselAutomateCouponLayoutBinding.setupRecyclerView() {
        automateCouponRv.apply {
            adapter = couponAdapter
            layoutManager = linearLayoutManager
        }
    }
}
