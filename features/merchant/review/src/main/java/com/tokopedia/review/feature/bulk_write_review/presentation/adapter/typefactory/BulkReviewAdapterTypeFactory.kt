package com.tokopedia.review.feature.bulk_write_review.presentation.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.review.feature.bulk_write_review.presentation.adapter.viewholder.BulkReviewAnnouncementViewHolder
import com.tokopedia.review.feature.bulk_write_review.presentation.adapter.viewholder.BulkReviewItemViewHolder
import com.tokopedia.review.feature.bulk_write_review.presentation.uimodel.BulkReviewAnnouncementUiModel
import com.tokopedia.review.feature.bulk_write_review.presentation.uimodel.BulkReviewItemUiModel

class BulkReviewAdapterTypeFactory(
    private val bulkReviewItemListener: BulkReviewItemViewHolder.Listener
) : BaseAdapterTypeFactory() {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            BulkReviewItemViewHolder.LAYOUT -> BulkReviewItemViewHolder(parent, bulkReviewItemListener)
            BulkReviewAnnouncementViewHolder.LAYOUT -> BulkReviewAnnouncementViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }

    fun type(bulkReviewItemUiModel: BulkReviewItemUiModel): Int {
        return BulkReviewItemViewHolder.LAYOUT
    }

    fun type(bulkReviewAnnouncementUiModel: BulkReviewAnnouncementUiModel): Int {
        return BulkReviewAnnouncementViewHolder.LAYOUT
    }
}
