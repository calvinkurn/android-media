package com.tokopedia.gamification.pdp.presentation.viewHolders

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.gamification.pdp.presentation.viewHolders.viewModel.KetupatBenefitCouponVHModel
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.gamification.R as gamificationR

class KetupatProductRecommVH(itemView: View) : AbstractViewHolder<KetupatBenefitCouponVHModel>(itemView) {
//upper layout might not needed
    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = gamificationR.layout.ketupat_product_recomm
    }

    override fun bind(element: KetupatBenefitCouponVHModel?) {
        element?.benefitCoupon.let { couponData ->
            itemView.findViewById<Typography>(gamificationR.id.coupon_banner_title)?.text =
                couponData?.title

            itemView.findViewById<Typography>(gamificationR.id.coupon_banner_subtitle)?.text =
                couponData?.text?.find { it?.key == "SUBTITLE" }?.value

            itemView.findViewById<Typography>(gamificationR.id.coupon_banner_subtitle)?.apply {
                RouteManager.route(
                    context,
                    couponData?.cta?.find { it?.type == "redirection" }?.appLink
                )
            }
        }

    }
}
