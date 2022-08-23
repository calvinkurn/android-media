package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ProductRecommendationVerticalPlaceholderDataModel

class ProductRecommendationVerticalPlaceholderViewHolder(
    view: View
) : ProductDetailPageViewHolder<ProductRecommendationVerticalPlaceholderDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.view_placeholder
    }

    override fun bind(element: ProductRecommendationVerticalPlaceholderDataModel?) {

    }

}