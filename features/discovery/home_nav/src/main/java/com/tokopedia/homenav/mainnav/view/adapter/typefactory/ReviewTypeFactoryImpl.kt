package com.tokopedia.homenav.mainnav.view.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.mainnav.view.adapter.viewholder.orderlist.ReviewItemViewHolder
import com.tokopedia.homenav.mainnav.view.adapter.viewholder.review.EmptyReviewViewHolder
import com.tokopedia.homenav.mainnav.view.adapter.viewholder.review.OtherReviewViewHolder
import com.tokopedia.homenav.mainnav.view.datamodel.review.EmptyStateReviewDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.review.OtherReviewModel
import com.tokopedia.homenav.mainnav.view.datamodel.review.ReviewModel
import com.tokopedia.homenav.mainnav.view.datamodel.review.ReviewNavVisitable
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener

class ReviewTypeFactoryImpl(val mainNavListener: MainNavListener) : BaseAdapterTypeFactory(), ReviewTypeFactory {

    override fun type(reviewModel: ReviewModel): Int {
        return ReviewItemViewHolder.LAYOUT
    }

    override fun type(otherReviewModel: OtherReviewModel): Int {
        return OtherReviewViewHolder.LAYOUT
    }

    override fun type(emptyStateReviewDataModel: EmptyStateReviewDataModel): Int {
        return EmptyReviewViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<ReviewNavVisitable> {
        return when (viewType) {
            ReviewItemViewHolder.LAYOUT -> ReviewItemViewHolder(view, mainNavListener)
            OtherReviewViewHolder.LAYOUT -> OtherReviewViewHolder(view, mainNavListener)
            EmptyReviewViewHolder.LAYOUT -> EmptyReviewViewHolder(view)
            else -> super.createViewHolder(view, viewType)
        } as AbstractViewHolder<ReviewNavVisitable>
    }
}
