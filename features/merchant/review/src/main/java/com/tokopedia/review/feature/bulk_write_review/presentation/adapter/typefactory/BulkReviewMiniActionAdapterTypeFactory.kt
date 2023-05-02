package com.tokopedia.review.feature.bulk_write_review.presentation.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.review.feature.bulk_write_review.presentation.adapter.viewholder.BulkReviewMiniActionViewHolder
import com.tokopedia.review.feature.bulk_write_review.presentation.uimodel.BulkReviewMiniActionUiModel

class BulkReviewMiniActionAdapterTypeFactory(
    private val listener: BulkReviewMiniActionViewHolder.Listener
) : BaseAdapterTypeFactory() {
    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            BulkReviewMiniActionViewHolder.LAYOUT -> BulkReviewMiniActionViewHolder(parent, listener)
            else -> super.createViewHolder(parent, type)
        }
    }

    fun type(miniActionUiModel: BulkReviewMiniActionUiModel): Int {
        return BulkReviewMiniActionViewHolder.LAYOUT
    }
}
