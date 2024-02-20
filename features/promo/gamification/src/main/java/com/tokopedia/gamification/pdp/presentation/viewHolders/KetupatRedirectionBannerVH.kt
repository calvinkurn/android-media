package com.tokopedia.gamification.pdp.presentation.viewHolders

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.gamification.pdp.data.GamificationAnalytics
import com.tokopedia.gamification.pdp.presentation.viewHolders.viewModel.KetupatRedirectionBannerVHModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.gamification.R as gamificationR

class KetupatRedirectionBannerVH(itemView: View) :
    AbstractViewHolder<KetupatRedirectionBannerVHModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = gamificationR.layout.ketupat_redirection_banner
    }

    override fun bind(element: KetupatRedirectionBannerVHModel?) {
        val scratchCardId = element?.scratchCard?.id.toString()

        element?.redirectionBanner.let { bannerData ->
            itemView.findViewById<Typography>(gamificationR.id.redirection_banner_title)?.text =
                bannerData?.title
            itemView.findViewById<ImageUnify>(gamificationR.id.redirection_img_1)?.apply {
                setImageUrl(bannerData?.cta?.get(0)?.imageURL.toString())
                this.setOnClickListener {
                    RouteManager.route(
                        context,
                        bannerData?.cta?.get(0)?.appLink
                    )
                    GamificationAnalytics.sendClickBannersInFooterBannerSectionEvent(
                        "{'direct_reward_id':'${scratchCardId}'}",
                        "gamification",
                        "tokopediamarketplace"
                    )
                }
            }
            itemView.findViewById<ImageUnify>(gamificationR.id.redirection_img_2)?.apply {
                setImageUrl(bannerData?.cta?.get(1)?.imageURL.toString())
                this.setOnClickListener {
                    RouteManager.route(
                        context,
                        bannerData?.cta?.get(1)?.appLink
                    )
                    GamificationAnalytics.sendClickBannersInFooterBannerSectionEvent(
                        "direct_reward_id: $scratchCardId",
                        "gamification",
                        "tokopediamarketplace"
                    )
                }
            }
            GamificationAnalytics.sendImpressFooterBannerSectionEvent("{'direct_reward_id':'${scratchCardId}'}")
        }
    }
}
