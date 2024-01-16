package com.tokopedia.gamification.pdp.presentation.viewHolders

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.gamification.pdp.presentation.viewHolders.viewModel.KetupatBenefitCouponSlugVHModel
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.gamification.R as gamificationR

class KetupatBenefitCouponSlugVH(itemView: View) : AbstractViewHolder<KetupatBenefitCouponSlugVHModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = gamificationR.layout.ketupat_benefit_coupon_slug
    }

    override fun bind(element: KetupatBenefitCouponSlugVHModel?) {
        element?.benefitCouponSlug.let { couponData ->
            itemView.findViewById<Typography>(gamificationR.id.coupon_banner_slug_title)?.text =
                couponData?.title

            itemView.findViewById<Typography>(gamificationR.id.coupon_lihat_semua_slug)?.apply {
                RouteManager.route(
                    context,
                    couponData?.cta?.find { it?.type == "redirection" }?.appLink
                )
            }
        }
    }
}
