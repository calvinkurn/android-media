package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.addtocartrecommendation.AddToCartDoneRecommendationViewModel

class AddToCartDoneRecommendationViewHolder(
        itemView: View
) : AbstractViewHolder<AddToCartDoneRecommendationViewModel>(itemView) {
    override fun bind(element: AddToCartDoneRecommendationViewModel?) {

    }

    companion object {
        val LAYOUT_RES = R.layout.add_to_cart_done_recommendation_layout
    }
}