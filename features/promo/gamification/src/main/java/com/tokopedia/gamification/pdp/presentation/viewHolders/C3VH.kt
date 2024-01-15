package com.tokopedia.gamification.pdp.presentation.viewHolders

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.gamification.pdp.presentation.viewHolders.viewModel.C3VHModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.gamification.R as gamificationR

class C3VH(itemView: View) : AbstractViewHolder<C3VHModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = gamificationR.layout.ketupat_refferal_banner
    }

    override fun bind(element: C3VHModel?) {
        element?.referral.let { referralData ->
            itemView.findViewById<Typography>(gamificationR.id.referral_banner_title)?.text =
                referralData?.title
            itemView.findViewById<ImageUnify>(gamificationR.id.referral_bg_img)?.setImageUrl(referralData?.assets?.find { it?.key == "BACKGROUND_IMAGE_START" }?.value.toString())
            itemView.findViewById<ImageUnify>(gamificationR.id.referral_bg_img)?.apply {
                this.setOnClickListener {
                    RouteManager.route(
                        context,
                        referralData?.cta?.find { it?.type == "redirection" }?.appLink
                    )
                }
            }
        }
    }
}
