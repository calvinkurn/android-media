package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ProductRecommendationVerticalDataModel
import com.tokopedia.product.detail.databinding.ViewProductRecommendationVerticalBinding
import com.tokopedia.recommendation_widget_common.extension.toProductCardModel
import com.tokopedia.unifycomponents.UnifyButton

class ProductRecommendationVerticalViewHolder(
    view: View
) : ProductDetailPageViewHolder<ProductRecommendationVerticalDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.view_product_recommendation_vertical
    }

    private val binding = ViewProductRecommendationVerticalBinding.bind(view)

    override fun bind(element: ProductRecommendationVerticalDataModel) {

        val item = element.recommendationItem ?: return

        binding.productDetailCard.show()

        binding.productDetailCard.apply {
            setProductModel(
                item.toProductCardModel(true, UnifyButton.Type.MAIN)
            )
        }
    }
}