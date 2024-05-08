package com.tokopedia.topads.sdk.v2.shopadslayout8or9.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.topads.sdk.R
import com.tokopedia.topads.sdk.v2.shopadslayout8or9.model.ShopWidgetShimmerUiModel

class ShopWidgetShimmerViewHolder(itemView: View) :
    AbstractViewHolder<ShopWidgetShimmerUiModel>(itemView) {

    override fun bind(item: ShopWidgetShimmerUiModel) {
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_ads_banner_product_shimmer
    }
}
