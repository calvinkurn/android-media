package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateDiscoBannerListFooterUiModel
import com.tokopedia.affiliate_toko.R

class AffiliateDiscoBannerListFooterVH(
    itemView: View
) :
    AbstractViewHolder<AffiliateDiscoBannerListFooterUiModel>(itemView) {
    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_disco_promo_footer_item_layout
    }

    override fun bind(element: AffiliateDiscoBannerListFooterUiModel?) = Unit
}
