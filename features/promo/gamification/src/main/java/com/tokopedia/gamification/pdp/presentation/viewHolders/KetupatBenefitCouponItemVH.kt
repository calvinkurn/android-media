package com.tokopedia.gamification.pdp.presentation.viewHolders

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.gamification.pdp.presentation.viewHolders.viewModel.KetupatBenefitCouponItemVHModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.gamification.R as gamificationR

class KetupatBenefitCouponItemVH(itemView: View) :
    AbstractViewHolder<KetupatBenefitCouponItemVHModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = gamificationR.layout.ketupat_benefit_coupon_item
    }

    override fun bind(element: KetupatBenefitCouponItemVHModel?) {
        element?.benefitCouponData.let { couponData ->
            couponData?.imageUrlMobile?.let {
                itemView.findViewById<ImageUnify>(gamificationR.id.ketupat_benefit_coupon_banner_image)
                    .setImageUrl(
                        it
                    )
            }
        }
    }
}
