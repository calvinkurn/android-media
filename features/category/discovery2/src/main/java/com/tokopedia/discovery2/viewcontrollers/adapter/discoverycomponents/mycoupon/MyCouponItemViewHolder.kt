package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.mycoupon

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.mycoupon.MyCoupon
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.unifycomponents.ImageUnify

private const val DESIGN_1 = 1.0
private const val DESIGN_2 = 2.1
private const val ASPECT_RATIO_3_TO_1 = 3
private const val ASPECT_RATIO_2_TO_1 = 2

class MyCouponItemViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {

    private var myCouponItemViewModel: MyCouponItemViewModel? = null
    private val myCouponImage: ImageUnify = itemView.findViewById(R.id.image_my_coupon)
    private val displayMetrics = Utils.getDisplayMetric(fragment.context)

    private var defaultDesign = DESIGN_2

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        myCouponItemViewModel = discoveryBaseViewModel as MyCouponItemViewModel
        myCouponItemViewModel?.let {
            getSubComponent().inject(it)
        }
        myCouponImage.setOnClickListener {
            sendClickEvent()
            myCouponItemViewModel?.setClick(itemView.context)
        }
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let {
            myCouponItemViewModel?.getComponentData()?.observe(fragment.viewLifecycleOwner) {
                setupImage(myCouponItemViewModel?.getCouponItem())
            }
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let {
            myCouponItemViewModel?.getComponentData()?.removeObservers(it)
        }
    }

    private fun setupImage(couponItem: MyCoupon?) {
        try {
            val layoutParams: ViewGroup.LayoutParams = myCouponImage.layoutParams
            val aspectRatio: Int
            if (myCouponItemViewModel?.getCouponListSize() == 1) {
                defaultDesign = DESIGN_1
                aspectRatio = ASPECT_RATIO_3_TO_1
                myCouponImage.loadImageWithoutPlaceholder(couponItem?.imageURLMobile)
            } else {
                defaultDesign = DESIGN_2
                aspectRatio = ASPECT_RATIO_2_TO_1
                if (!couponItem?.imageHalfURLMobile.isNullOrEmpty()) {
                    myCouponImage.loadImageWithoutPlaceholder(couponItem?.imageHalfURLMobile)
                } else {
                    myCouponImage.loadImageWithoutPlaceholder(couponItem?.imageURLMobile)
                }
            }
            layoutParams.width = (
                (displayMetrics.widthPixels - itemView.context.resources.getDimensionPixelSize(R.dimen.my_coupon_gap)) /
                    defaultDesign
                ).toInt()
            layoutParams.height = (layoutParams.width / aspectRatio)
            myCouponImage.layoutParams = layoutParams
        } catch (exception: NumberFormatException) {
            exception.printStackTrace()
        }
    }

    override fun onViewAttachedToWindow() {
        super.onViewAttachedToWindow()
        myCouponItemViewModel?.let { (fragment as DiscoveryFragment).getDiscoveryAnalytics().trackEventViewMyCouponList(it.components, it.getUserId()) }
    }

    private fun sendClickEvent() {
        myCouponItemViewModel?.let { (fragment as DiscoveryFragment).getDiscoveryAnalytics().trackEventClickMyCouponList(it.components, it.getUserId()) }
    }
}
