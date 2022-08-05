package com.tokopedia.topads.sdk.shopwidgetthreeproducts.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.topads.sdk.R
import com.tokopedia.topads.sdk.shopwidgetthreeproducts.model.ShopWidgetShimmerViewModel

class ShopWidgetShimmerViewHolder(itemView: View) :
    AbstractViewHolder<ShopWidgetShimmerViewModel>(itemView) {

    override fun bind(item: ShopWidgetShimmerViewModel) {
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_ads_banner_product_shimmer
    }
}
