package com.tokopedia.product.detail.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.addtocartrecommendation.AddToCartDoneAddedProductViewModel
import com.tokopedia.product.detail.data.model.addtocartrecommendation.AddToCartDoneRecommendationViewModel
import com.tokopedia.product.detail.data.model.addtocartrecommendation.RecommendationProductViewModel
import com.tokopedia.product.detail.view.viewholder.AddToCartDoneAddedProductViewHolder
import com.tokopedia.product.detail.view.viewholder.AddToCartDoneRecommendationViewHolder
import com.tokopedia.product.detail.view.viewholder.RecommendationProductViewHolder
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.trackingoptimizer.TrackingQueue

class RecommendationProductTypeFactory(
        val recommendationListener: RecommendationListener
) : BaseAdapterTypeFactory() {
    fun type(recommendationProductViewModel: RecommendationProductViewModel): Int {
        return RecommendationProductViewHolder.LAYOUT_RES
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            RecommendationProductViewHolder.LAYOUT_RES -> RecommendationProductViewHolder(
                    parent,
                    recommendationListener
            )
            else -> return super.createViewHolder(parent, type)
        }
    }
}