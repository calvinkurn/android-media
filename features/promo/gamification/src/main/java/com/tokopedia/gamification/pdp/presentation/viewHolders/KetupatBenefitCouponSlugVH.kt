package com.tokopedia.gamification.pdp.presentation.viewHolders

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.gamification.pdp.data.GamificationAnalytics
import com.tokopedia.gamification.pdp.presentation.adapters.KetupatLandingAdapter
import com.tokopedia.gamification.pdp.presentation.adapters.KetupatLandingAdapterTypeFactory
import com.tokopedia.gamification.pdp.presentation.adapters.KetupatLandingTypeFactory
import com.tokopedia.gamification.pdp.presentation.viewHolders.viewModel.KetupatBenefitCouponSlugItemVHModel
import com.tokopedia.gamification.pdp.presentation.viewHolders.viewModel.KetupatBenefitCouponSlugVHModel
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.gamification.R as gamificationR

class KetupatBenefitCouponSlugVH(itemView: View) :
    AbstractViewHolder<KetupatBenefitCouponSlugVHModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = gamificationR.layout.ketupat_benefit_coupon_slug
    }

    override fun bind(element: KetupatBenefitCouponSlugVHModel?) {
        val scratchCardId = element?.scratchCard?.id.toString()

        element?.benefitCouponSlug.let { couponData ->
            itemView.findViewById<Typography>(gamificationR.id.coupon_banner_slug_title)?.text =
                couponData?.title

            itemView.findViewById<Typography>(gamificationR.id.coupon_lihat_semua_slug)?.apply {
                setOnClickListener {
                    RouteManager.route(
                        context,
                        couponData?.cta?.find { it?.type == "redirection" }?.appLink
                    )
                }
                GamificationAnalytics.sendClickLihatSemuaInCouponWidgetByCatalogSectionEvent(
                    "direct_reward_id: $scratchCardId",
                    "gamification",
                    "tokopediamarketplace"
                )
            }
        }
        element?.value.let { it ->
            val tempList: ArrayList<Visitable<KetupatLandingTypeFactory>> = ArrayList()
            it?.tokopointsCouponListStack?.tokopointsCouponDataStack?.forEach { tokopointsCouponDataStack ->
                tokopointsCouponDataStack?.let { benefitCouponSlugData ->
                    element?.scratchCard?.let { scratchCard ->
                        KetupatBenefitCouponSlugItemVHModel(benefitCouponSlugData, scratchCard)
                    }
                }
                    ?.let { benefitCouponSlugItemVHModel ->
                        tempList.add(
                            benefitCouponSlugItemVHModel
                        )
                    }
            }
            val adapter = KetupatLandingAdapter(
                KetupatLandingAdapterTypeFactory()
            )
            adapter.addMoreData(tempList)
            itemView.findViewById<RecyclerView>(gamificationR.id.rv_benefit_coupon_slug).adapter =
                adapter
        }
        GamificationAnalytics.sendImpressCouponWidgetByCatalogSectionEvent("direct_reward_id: $scratchCardId")
    }
}
