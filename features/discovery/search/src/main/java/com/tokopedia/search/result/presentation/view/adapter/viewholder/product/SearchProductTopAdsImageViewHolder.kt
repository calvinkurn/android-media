package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchResultProductTopAdsImageViewLayoutBinding
import com.tokopedia.search.result.presentation.model.SearchProductTopAdsImageDataView
import com.tokopedia.search.result.product.tdn.TopAdsImageViewListener
import com.tokopedia.topads.sdk.v2.listener.TopAdsImageViewClickListener
import com.tokopedia.utils.view.binding.viewBinding

class SearchProductTopAdsImageViewHolder(
        itemView: View,
        private val topAdsImageViewListener: TopAdsImageViewListener
): AbstractViewHolder<SearchProductTopAdsImageDataView>(itemView) {

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.search_result_product_top_ads_image_view_layout

        const val IMAGE_CORNER_RADIUS = 20
    }
    private var binding: SearchResultProductTopAdsImageViewLayoutBinding? by viewBinding()

    override fun bind(element: SearchProductTopAdsImageDataView?) {
        val binding = binding ?: return
        element ?: return

        val topAdsImageView = binding.searchProductTopAdsImageView
        val topAdsImageViewModel = element.topAdsImageUiModel

        topAdsImageView.loadImage(topAdsImageViewModel, IMAGE_CORNER_RADIUS) {
            topAdsImageView.hide()
        }

        topAdsImageView.addOnImpressionListener(element, object: ViewHintListener {
            override fun onViewHint() {
                topAdsImageViewListener.onTopAdsImageViewImpressed(
                        topAdsImageView.javaClass.canonicalName,
                        element
                )
            }
        })

        topAdsImageView.setTopAdsImageViewClick(object: TopAdsImageViewClickListener {
            override fun onTopAdsImageViewClicked(applink: String?) {
                topAdsImageViewListener.onTopAdsImageViewClick(element)
            }
        })
    }
}
