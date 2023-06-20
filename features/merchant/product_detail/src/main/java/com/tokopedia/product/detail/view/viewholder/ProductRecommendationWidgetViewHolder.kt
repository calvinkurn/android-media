package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ProductRecommendationWidgetUiModel
import com.tokopedia.product.detail.databinding.ItemProductRecommendationWidgetBinding

class ProductRecommendationWidgetViewHolder(
    view: View
) : AbstractViewHolder<ProductRecommendationWidgetUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_product_recommendation_widget
    }

    private val binding = ItemProductRecommendationWidgetBinding.bind(view)

    override fun bind(element: ProductRecommendationWidgetUiModel) {
        binding.root.bind(element.recommendationWidget)
    }
}
