package com.tokopedia.gamification.pdp.presentation.viewHolders

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.gamification.pdp.presentation.viewHolders.viewModel.KetupatTopBannerVHModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.gamification.R as gamificationR

class KetupatTopBannerVH(itemView: View) : AbstractViewHolder<KetupatTopBannerVHModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = gamificationR.layout.ketupat_top_banner
    }

    override fun bind(element: KetupatTopBannerVHModel?) {
        element?.header?.let { header ->
            itemView.findViewById<ImageUnify>(gamificationR.id.header_image)
            .apply {
                this.cornerRadius = 0
            }?.setImageUrl(header.assets.find { it?.key == "BACKGROUND_IMAGE" }?.value.toString())
        }
    }

}
