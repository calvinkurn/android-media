package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.mycoupon

import android.view.View
import androidx.fragment.app.Fragment
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.mycoupon.MyCoupon
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.ImageUnify

class MyCouponItemViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView) {

    private lateinit var myCouponItemViewModel: MyCouponItemViewModel
    private val myCouponImage: ImageUnify = itemView.findViewById(R.id.image_my_coupon)

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        myCouponItemViewModel = discoveryBaseViewModel as MyCouponItemViewModel
        getSubComponent().inject(myCouponItemViewModel)
        myCouponItemViewModel.getComponentData().observe(fragment.viewLifecycleOwner, {
            setData(it)
        })
    }

    private fun setData(dataItem: MyCoupon?) {
         myCouponImage.loadImage(dataItem?.imageURLMobile)
    }

}