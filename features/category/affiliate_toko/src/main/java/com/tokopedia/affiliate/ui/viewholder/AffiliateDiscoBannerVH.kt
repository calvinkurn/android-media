package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateDiscoBannerUiModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.unifycomponents.ImageUnify

class AffiliateDiscoBannerVH(itemView: View) :
    AbstractViewHolder<AffiliateDiscoBannerUiModel>(itemView) {
    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_disco_banner_list_item
    }

    private val discoBannerImage =
        itemView.findViewById<ImageUnify>(R.id.affiliate_disco_banner_image)

    override fun bind(element: AffiliateDiscoBannerUiModel?) {
        discoBannerImage.setImageUrl(element?.article?.imageBanner.toString())
    }
}
