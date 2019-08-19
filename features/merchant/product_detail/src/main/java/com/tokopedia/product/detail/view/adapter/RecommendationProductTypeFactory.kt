package com.tokopedia.product.detail.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.detail.data.model.addtocartrecommendation.AddToCartDoneRecommendationProductDataModel
import com.tokopedia.product.detail.view.viewholder.AddToCartDoneRecommendationProductViewHolder
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener

class RecommendationProductTypeFactory(
        val recommendationListener: RecommendationListener
) : BaseAdapterTypeFactory() {
    fun type(addToCartDoneRecommendationProductDataModel: AddToCartDoneRecommendationProductDataModel): Int {
        return AddToCartDoneRecommendationProductViewHolder.LAYOUT_RES
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            AddToCartDoneRecommendationProductViewHolder.LAYOUT_RES -> AddToCartDoneRecommendationProductViewHolder(
                    parent,
                    recommendationListener
            )
            else -> return super.createViewHolder(parent, type)
        }
    }
}