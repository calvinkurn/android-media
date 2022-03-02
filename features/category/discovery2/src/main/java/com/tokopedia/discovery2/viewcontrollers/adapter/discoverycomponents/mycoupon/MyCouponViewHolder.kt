package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.mycoupon

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.DiscoveryRecycleAdapter
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.bannercarousel.BannerCarouselItemDecorator
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder

class MyCouponViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {


    private val myCouponRecyclerView: RecyclerView = itemView.findViewById(R.id.my_coupon_rv)
    private var discoveryRecycleAdapter: DiscoveryRecycleAdapter = DiscoveryRecycleAdapter(fragment)
    private val myCouponItemDecorator = MyCouponItemDecorator()

    private lateinit var myCouponViewModel: MyCouponViewModel

    init {
        myCouponRecyclerView.adapter = discoveryRecycleAdapter
    }

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        myCouponViewModel = discoveryBaseViewModel as MyCouponViewModel
        getSubComponent().inject(myCouponViewModel)
        addDefaultItemDecorator()
        myCouponRecyclerView.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)

    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        myCouponViewModel.getComponentList().observe(fragment.viewLifecycleOwner,  { item ->
            discoveryRecycleAdapter.setDataList(item)
        })
    }

    private fun addDefaultItemDecorator() {
        if (myCouponRecyclerView.itemDecorationCount > 0)
            myCouponRecyclerView.removeItemDecorationAt(0)
        myCouponRecyclerView.addItemDecoration(myCouponItemDecorator)
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let { myCouponViewModel.getComponentList().removeObservers(it) }
    }

}
