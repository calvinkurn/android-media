package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ProductRecommendationVerticalPlaceholderDataModel
import com.tokopedia.product.detail.databinding.ViewProductRecommendationVerticalPlaceholderBinding

class ProductRecommendationVerticalPlaceholderViewHolder(
    view: View
) : ProductDetailPageViewHolder<ProductRecommendationVerticalPlaceholderDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.view_product_recommendation_vertical_placeholder
    }

    private val binding = ViewProductRecommendationVerticalPlaceholderBinding.bind(view)

    override fun bind(element: ProductRecommendationVerticalPlaceholderDataModel) {
        setTitle(element)
    }

    override fun bind(
        element: ProductRecommendationVerticalPlaceholderDataModel,
        payloads: MutableList<Any>
    ) {
        setTitle(element)
        super.bind(element, payloads)
    }

    private fun setTitle(element: ProductRecommendationVerticalPlaceholderDataModel) {
        val recomWidgetData = element.recomWidgetData ?: return
        val title = recomWidgetData.title
        binding.productRecommendationVerticalTitle.showIfWithBlock(title.isNotEmpty()) {
            text = title
        }
    }

}