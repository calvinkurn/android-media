package com.tokopedia.product.detail.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.detail.data.model.addtocartrecommendation.AddToCartDoneAddedProductDataModel
import com.tokopedia.product.detail.data.model.addtocartrecommendation.AddToCartDoneRecommendationDataModel
import com.tokopedia.product.detail.view.viewholder.AddToCartDoneAddedProductViewHolder
import com.tokopedia.product.detail.view.viewholder.AddToCartDoneRecommendationViewHolder
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener

class AddToCartDoneTypeFactory(
        private val addToCartDoneAddedProductListener: AddToCartDoneAddedProductViewHolder.AddToCartDoneAddedProductListener,
        val recommendationListener: RecommendationListener
) : BaseAdapterTypeFactory() {

    fun type(addToCartDoneRecommendationDataModel: AddToCartDoneRecommendationDataModel): Int {
        return AddToCartDoneRecommendationViewHolder.LAYOUT_RES
    }

    fun type(addToCartDoneAddedProductDataModel: AddToCartDoneAddedProductDataModel): Int {
        return AddToCartDoneAddedProductViewHolder.LAYOUT_RES
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            AddToCartDoneRecommendationViewHolder.LAYOUT_RES -> AddToCartDoneRecommendationViewHolder(
                    parent,
                    recommendationListener
            )
            AddToCartDoneAddedProductViewHolder.LAYOUT_RES -> AddToCartDoneAddedProductViewHolder(
                    parent,
                    addToCartDoneAddedProductListener
            )
            else -> return super.createViewHolder(parent, type)
        }
    }
}