package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.mycoupon

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.mycoupon.MyCoupon
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.ImageUnify

class MyCouponItemViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView,fragment.viewLifecycleOwner) {

    private lateinit var myCouponItemViewModel: MyCouponItemViewModel
    private val myCouponImage: ImageUnify = itemView.findViewById(R.id.image_my_coupon)

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        myCouponItemViewModel = discoveryBaseViewModel as MyCouponItemViewModel
        getSubComponent().inject(myCouponItemViewModel)
        myCouponImage.setOnClickListener {
            sendClickEvent()
            myCouponItemViewModel.setClick(itemView.context)
        }
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let {
            myCouponItemViewModel.getComponentData().observe(fragment.viewLifecycleOwner, {
                setData(myCouponItemViewModel.getCouponItem())
            })
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let {
            myCouponItemViewModel.getComponentData().removeObservers(it)
        }
    }

    private fun setData(couponItem: MyCoupon?) {
         myCouponImage.loadImage(couponItem?.imageURLMobile)
    }

    override fun onViewAttachedToWindow() {
        super.onViewAttachedToWindow()
        (fragment as DiscoveryFragment).getDiscoveryAnalytics().trackEventViewMyCouponList(myCouponItemViewModel.components,myCouponItemViewModel.getUserId())
    }

    private fun sendClickEvent() {
        (fragment as DiscoveryFragment).getDiscoveryAnalytics().trackEventClickMyCouponList(myCouponItemViewModel.components,myCouponItemViewModel.getUserId())
    }

}