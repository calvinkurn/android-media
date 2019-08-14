package com.tokopedia.product.detail.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.addtocartrecommendation.AddToCartDoneAddedProductViewModel
import com.tokopedia.product.detail.data.model.addtocartrecommendation.AddToCartDoneRecommendationViewModel
import com.tokopedia.product.detail.view.viewholder.AddToCartDoneAddedProductViewHolder
import com.tokopedia.product.detail.view.viewholder.AddToCartDoneRecommendationViewHolder

class AddToCartDoneTypeFactory : BaseAdapterTypeFactory() {
    fun type(addToCartDoneRecommendationViewModel: AddToCartDoneRecommendationViewModel): Int {
        return AddToCartDoneRecommendationViewHolder.LAYOUT_RES
    }

    fun type(addToCartDoneAddedProductViewModel: AddToCartDoneAddedProductViewModel): Int {
        return AddToCartDoneAddedProductViewHolder.LAYOUT_RES
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            AddToCartDoneRecommendationViewHolder.LAYOUT_RES -> AddToCartDoneRecommendationViewHolder(parent)
            AddToCartDoneAddedProductViewHolder.LAYOUT_RES -> AddToCartDoneAddedProductViewHolder(parent)
            else -> return super.createViewHolder(parent, type)
        }
    }
}