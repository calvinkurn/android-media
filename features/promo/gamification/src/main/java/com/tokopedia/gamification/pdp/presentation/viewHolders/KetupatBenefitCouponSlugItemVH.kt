package com.tokopedia.gamification.pdp.presentation.viewHolders

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.gamification.pdp.presentation.viewHolders.viewModel.KetupatBenefitCouponSlugItemVHModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.gamification.R as gamificationR

class KetupatBenefitCouponSlugItemVH(itemView: View) :
    AbstractViewHolder<KetupatBenefitCouponSlugItemVHModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = gamificationR.layout.ketupat_benefit_coupon_slug_item
    }

    override fun bind(element: KetupatBenefitCouponSlugItemVHModel?) {
        element?.benefitCouponSlugData.let { couponData ->
            couponData?.imageURLMobile?.let {
                itemView.findViewById<ImageUnify>(gamificationR.id.ketupat_benefit_coupon_slug_banner_image)
                    .apply {
                        this.setImageUrl(
                            it
                        )
                        this.setOnClickListener {
                            RouteManager.route(
                                context,
                                "tokopedia://rewards/kupon-saya/detail/${couponData.code}"
                            )
                        }
                    }
            }
        }
    }
}
