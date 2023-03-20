package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateDiscoBannerListUiModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.unifycomponents.ImageUnify

class AffiliateDiscoBannerListVH(itemView: View) :
    AbstractViewHolder<AffiliateDiscoBannerListUiModel>(itemView) {
    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_disco_promo_item_layout
    }

    private val discoBannerImage =
        itemView.findViewById<ImageUnify>(R.id.image_discovery_promo_list_item)
    private val discoBannerTitle =
        itemView.findViewById<com.tokopedia.unifyprinciples.Typography>(R.id.discovery_promo_list_title)

    override fun bind(element: AffiliateDiscoBannerListUiModel?) {
        discoBannerImage.setImageUrl(element?.article?.imageBanner.toString())
        discoBannerTitle.text = element?.article?.title
    }
}
