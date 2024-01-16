package com.tokopedia.gamification.pdp.presentation.viewHolders

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.gamification.pdp.presentation.viewHolders.viewModel.KetupatRedirectionBannerVHModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.gamification.R as gamificationR

class KetupatRedirectionBannerVH(itemView: View) : AbstractViewHolder<KetupatRedirectionBannerVHModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = gamificationR.layout.ketupat_redirection_banner
    }

    override fun bind(element: KetupatRedirectionBannerVHModel?) {
        element?.redirectionBanner.let { bannerData ->
            itemView.findViewById<Typography>(gamificationR.id.redirection_banner_title)?.text =
                bannerData?.title
            itemView.findViewById<ImageUnify>(gamificationR.id.redirection_img_1)?.apply {
                this.setOnClickListener {
                    RouteManager.route(
                        context,
                        bannerData?.cta?.find { it?.type == "redirection" }?.appLink
                    )
                }
            }
            itemView.findViewById<ImageUnify>(gamificationR.id.redirection_img_2)?.apply {
                this.setOnClickListener {
                    RouteManager.route(
                        context,
                        bannerData?.cta?.find { it?.type == "redirection" }?.appLink
                    )
                }
            }
        }
    }
}
