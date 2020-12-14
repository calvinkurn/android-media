package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.search.R
import com.tokopedia.search.result.presentation.model.SearchProductTopAdsImageViewModel
import com.tokopedia.topads.sdk.widget.TopAdsImageView

class SearchProductTopAdsImageViewHolder(itemView: View): AbstractViewHolder<SearchProductTopAdsImageViewModel>(itemView) {

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.search_result_product_top_ads_image_view_layout
    }

    override fun bind(element: SearchProductTopAdsImageViewModel?) {
        element ?: return

        val topAdsImageView = itemView.findViewById<TopAdsImageView?>(R.id.searchProductTopAdsImageView)

        topAdsImageView?.loadImage(element.topAdsImageViewModel, 0) {
            topAdsImageView.hide()
        }

//        topAdsImageView?.setTopAdsImageViewImpression()
    }
}