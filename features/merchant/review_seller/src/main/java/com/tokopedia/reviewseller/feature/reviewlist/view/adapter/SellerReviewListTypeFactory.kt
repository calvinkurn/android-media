package com.tokopedia.reviewseller.feature.reviewlist.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.reviewseller.common.SummaryReviewModel
import com.tokopedia.reviewseller.common.ProductReviewModel
import com.tokopedia.reviewseller.feature.reviewlist.view.viewholder.ReviewSummaryViewHolder
import com.tokopedia.reviewseller.feature.reviewlist.view.viewholder.SellerReviewListViewHolder

class SellerReviewListTypeFactory: BaseAdapterTypeFactory() {

    fun type(summaryReviewModel: SummaryReviewModel): Int {
        return ReviewSummaryViewHolder.LAYOUT_RES
    }

    fun type(productReviewModel: ProductReviewModel): Int {
        return SellerReviewListViewHolder.LAYOUT_RES
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            ReviewSummaryViewHolder.LAYOUT_RES -> ReviewSummaryViewHolder(
                    parent
            )
            SellerReviewListViewHolder.LAYOUT_RES -> SellerReviewListViewHolder(
                    parent)
            else -> return super.createViewHolder(parent, type)
        }
    }

}