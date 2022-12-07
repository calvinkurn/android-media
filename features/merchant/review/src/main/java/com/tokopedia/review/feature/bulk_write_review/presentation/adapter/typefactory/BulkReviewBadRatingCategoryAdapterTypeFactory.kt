package com.tokopedia.review.feature.bulk_write_review.presentation.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.review.feature.bulk_write_review.presentation.adapter.viewholder.BulkReviewBadRatingCategoryViewHolder
import com.tokopedia.review.feature.bulk_write_review.presentation.uimodel.BulkReviewBadRatingCategoryUiModel

class BulkReviewBadRatingCategoryAdapterTypeFactory(
    private val listener: BulkReviewBadRatingCategoryViewHolder.Listener
) : BaseAdapterTypeFactory() {
    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            BulkReviewBadRatingCategoryViewHolder.LAYOUT -> BulkReviewBadRatingCategoryViewHolder(parent, listener)
            else -> super.createViewHolder(parent, type)
        }
    }

    fun type(bulkReviewBadRatingCategoryUiModel: BulkReviewBadRatingCategoryUiModel): Int {
        return BulkReviewBadRatingCategoryViewHolder.LAYOUT
    }
}
