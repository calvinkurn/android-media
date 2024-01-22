package com.tokopedia.gamification.pdp.presentation.viewHolders

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.gamification.pdp.data.GamificationAnalytics
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
        val scratchCardId = element?.scratchCard?.id.toString()
        element?.benefitCouponData.let { couponData ->
            couponData?.imageUrlMobile?.let {
                itemView.findViewById<ImageUnify>(gamificationR.id.ketupat_benefit_coupon_banner_image)
                    .apply {
                        setImageUrl(
                            it
                        )
                        setOnClickListener {
                            RouteManager.route(
                                context,
                                "tokopedia://rewards/kupon-saya/detail/${couponData.code}"
                            )
                            GamificationAnalytics.sendClickCouponInCouponWidgetByCategorySectionEvent(
                                "direct_reward_id: $scratchCardId",
                                "gamification",
                                "tokopediamarketplace"
                            )
                        }
                    }
            }
        }
    }
}
