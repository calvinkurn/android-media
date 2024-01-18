package com.tokopedia.gamification.pdp.presentation.viewHolders

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.gamification.pdp.presentation.viewHolders.viewModel.KetupatCrackBannerVHModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.gamification.R as gamificationR

class KetupatCrackBannerVH(itemView: View) : AbstractViewHolder<KetupatCrackBannerVHModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = gamificationR.layout.ketupat_crack_banner
    }

    override fun bind(element: KetupatCrackBannerVHModel?) {
        element?.crack.let { crackData ->
            itemView.findViewById<Typography>(gamificationR.id.crack_banner_title)?.text =
                crackData?.title
            itemView.findViewById<ImageUnify>(gamificationR.id.crack_bg_img)
                ?.setImageUrl(crackData?.assets?.find { it?.key == "BACKGROUND_IMAGE" }?.value.toString())
            itemView.findViewById<ImageUnify>(gamificationR.id.crack_icon_img)
                ?.setImageUrl(crackData?.assets?.find { it?.key == "IMAGE_ICON" }?.value.toString())
            itemView.findViewById<Typography>(gamificationR.id.crack_img_text)?.text =
                crackData?.text?.find { it?.key == "DEFAULT" }?.value
            itemView.findViewById<UnifyButton>(gamificationR.id.more_info_btn).apply {
                text = "Bara Cara Main"
                this.buttonVariant = UnifyButton.Variant.GHOST
                this.setOnClickListener {
                    RouteManager.route(
                        context,
                        crackData?.cta?.find { it?.type == "crack" }?.appLink
                    )
                }
            }
            itemView.findViewById<ImageUnify>(gamificationR.id.open_btn_bg).apply {
//                text = "Belah Ketupat, Yuk!"

//                this.buttonVariant = UnifyButton.Variant.GHOST
                this.setOnClickListener {
                    itemView.findViewById<ImageUnify>(gamificationR.id.crack_icon_img)
                        ?.setImageUrl(crackData?.assets?.find { it?.key == "IMAGE_ICON_OPENED" }?.value.toString())
                    itemView.findViewById<Typography>(gamificationR.id.crack_img_text)?.text =
                        crackData?.text?.find { it?.key == "OPENED" }?.value
                    this.hide()
                }
            }
            crackData?.cta?.get(0)?.imageURL?.let {
                itemView.findViewById<ImageUnify>(gamificationR.id.open_btn_bg).setImageUrl(
                    it
                )
            }

        }
    }

}
