package com.tokopedia.gamification.pdp.presentation.viewHolders

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.gamification.pdp.presentation.viewHolders.viewModel.C3VHModel
import com.tokopedia.gamification.pdp.presentation.viewHolders.viewModel.C4VHModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.gamification.R as gamificationR

class C4VH(itemView: View) : AbstractViewHolder<C4VHModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = gamificationR.layout.ketupat_benefit_coupon
    }

    override fun bind(element: C4VHModel?) {
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
