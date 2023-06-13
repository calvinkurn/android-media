package com.tokopedia.topads.sdk.view.adapter.viewholder.banner

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.topads.sdk.R
import com.tokopedia.topads.sdk.base.adapter.viewholder.AbstractViewHolder
import com.tokopedia.topads.sdk.view.adapter.viewmodel.banner.BannerProductShimmerUiModel


class BannerProductShimmerViewHolder(itemView: View) : AbstractViewHolder<BannerProductShimmerUiModel?>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_ads_banner_product_shimmer
    }

    override fun bind(element: BannerProductShimmerUiModel?) {

    }
}
