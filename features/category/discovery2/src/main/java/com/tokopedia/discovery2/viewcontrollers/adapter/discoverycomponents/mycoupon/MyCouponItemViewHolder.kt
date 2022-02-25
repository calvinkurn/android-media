package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.mycoupon

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.mycoupon.MyCoupon
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.unifycomponents.ImageUnify

private const val DEFAULT_DESIGN = 2.1

class MyCouponItemViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView,fragment.viewLifecycleOwner) {

    private lateinit var myCouponItemViewModel: MyCouponItemViewModel
    private val myCouponImage: ImageUnify = itemView.findViewById(R.id.image_my_coupon)
    private val displayMetrics = Utils.getDisplayMetric(fragment.context)

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
                setupImage(myCouponItemViewModel.getCouponItem(),it)
            })
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let {
            myCouponItemViewModel.getComponentData().removeObservers(it)
        }
    }

    private fun setupImage(couponItem: MyCoupon?, componentItem: ComponentsItem){
        try {
            val layoutParams: ViewGroup.LayoutParams = myCouponImage.layoutParams
            layoutParams.width = ((displayMetrics.widthPixels - itemView.context.resources.getDimensionPixelSize(R.dimen.carousel_gap))
                    / DEFAULT_DESIGN).toInt()
                val aspectRatio = 2 / 1
                layoutParams.height = (layoutParams.width / aspectRatio)
            myCouponImage.layoutParams = layoutParams
            myCouponImage.loadImageWithoutPlaceholder(couponItem?.imageURLMobile)
        } catch (exception: NumberFormatException) {
            exception.printStackTrace()
        }
    }

    override fun onViewAttachedToWindow() {
        super.onViewAttachedToWindow()
        (fragment as DiscoveryFragment).getDiscoveryAnalytics().trackEventViewMyCouponList(myCouponItemViewModel.components,myCouponItemViewModel.getUserId())
    }

    private fun sendClickEvent() {
        (fragment as DiscoveryFragment).getDiscoveryAnalytics().trackEventClickMyCouponList(myCouponItemViewModel.components,myCouponItemViewModel.getUserId())
    }

}