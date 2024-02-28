package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.utils.extensions.addOnImpressionListener
import com.tokopedia.product.detail.data.model.datamodel.ProductRecommendationVerticalPlaceholderDataModel
import com.tokopedia.product.detail.databinding.ViewProductRecommendationVerticalPlaceholderBinding
import com.tokopedia.product.detail.view.listener.ProductDetailListener

class ProductRecommendationVerticalPlaceholderViewHolder(
    view: View,
    private val listener: ProductDetailListener
) : ProductDetailPageViewHolder<ProductRecommendationVerticalPlaceholderDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.view_product_recommendation_vertical_placeholder
    }

    private val binding = ViewProductRecommendationVerticalPlaceholderBinding.bind(view)

    override fun bind(element: ProductRecommendationVerticalPlaceholderDataModel) {
        setTitle(element)

        itemView.addOnImpressionListener(
            holder = element.impressHolder,
            holders = listener.getImpressionHolders(),
            name = element.name(),
            useHolders = listener.isRemoteCacheableActive()
        ) {
            listener.onImpressComponent(getComponentTrackData(element))
            listener.onImpressRecommendationVertical(getComponentTrackData(element))
        }
    }

    private fun setTitle(element: ProductRecommendationVerticalPlaceholderDataModel) {
        val recomWidgetData = element.recomWidgetData ?: return
        val title = recomWidgetData.title
        binding.productRecommendationVerticalTitle.showIfWithBlock(title.isNotEmpty()) {
            text = title
        }
    }
}
