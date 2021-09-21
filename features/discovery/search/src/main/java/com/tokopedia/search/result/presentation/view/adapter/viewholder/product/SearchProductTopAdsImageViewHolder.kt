package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.search.R
import com.tokopedia.search.result.presentation.model.SearchProductTopAdsImageDataView
import com.tokopedia.search.result.presentation.view.listener.TopAdsImageViewListener
import com.tokopedia.topads.sdk.listener.TopAdsImageViewClickListener
import com.tokopedia.topads.sdk.widget.TopAdsImageView

class SearchProductTopAdsImageViewHolder(
        itemView: View,
        private val topAdsImageViewListener: TopAdsImageViewListener
): AbstractViewHolder<SearchProductTopAdsImageDataView>(itemView) {

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.search_result_product_top_ads_image_view_layout
    }

    override fun bind(element: SearchProductTopAdsImageDataView?) {
        element ?: return

        val topAdsImageView = itemView.findViewById<TopAdsImageView?>(R.id.searchProductTopAdsImageView)
        val topAdsImageViewModel = element.topAdsImageViewModel

        topAdsImageView?.loadImage(topAdsImageViewModel, 20) {
            topAdsImageView.hide()
        }

        topAdsImageView?.addOnImpressionListener(element, object: ViewHintListener {
            override fun onViewHint() {
                topAdsImageViewListener.onTopAdsImageViewImpressed(
                        topAdsImageView.javaClass.canonicalName,
                        element
                )
            }
        })

        topAdsImageView?.setTopAdsImageViewClick(object: TopAdsImageViewClickListener {
            override fun onTopAdsImageViewClicked(applink: String?) {
                topAdsImageViewListener.onTopAdsImageViewClick(element)
            }
        })
    }
}