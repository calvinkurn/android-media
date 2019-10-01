package com.tokopedia.officialstore.official.presentation.adapter.viewholder

import android.support.annotation.LayoutRes
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.officialstore.R
import com.tokopedia.officialstore.official.presentation.adapter.viewmodel.ProductRecommendationViewModel

class ProductRecommendationViewHolder(view: View?): AbstractViewHolder<ProductRecommendationViewModel>(view) {

    override fun bind(element: ProductRecommendationViewModel?) {

    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.viewmodel_product_recommendation
    }

}