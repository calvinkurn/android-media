package com.tokopedia.reviewseller.feature.reviewdetail.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.viewholder.ProductFeedbackDetailViewHolder
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.ProductFeedbackDetailUiModel

class SellerReviewDetailAdapterTypeFactory: BaseAdapterTypeFactory() {


    fun type(productFeedbackDetailUiModel: ProductFeedbackDetailUiModel): Int {
        return ProductFeedbackDetailViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when(type) {
            ProductFeedbackDetailViewHolder.LAYOUT -> ProductFeedbackDetailViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}