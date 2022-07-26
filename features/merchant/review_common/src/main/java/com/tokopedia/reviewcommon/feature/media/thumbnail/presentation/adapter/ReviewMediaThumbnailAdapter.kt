package com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.adapter.diffutil.ReviewMediaThumbnailDiffUtilCallback
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.adapter.typefactory.ReviewMediaThumbnailTypeFactory
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaThumbnailVisitable

class ReviewMediaThumbnailAdapter(
    typeFactory: ReviewMediaThumbnailTypeFactory
): BaseAdapter<ReviewMediaThumbnailTypeFactory>(typeFactory) {
    @Suppress("UNCHECKED_CAST")
    fun updateItems(items: List<ReviewMediaThumbnailVisitable>) {
        val diffUtilCallback = ReviewMediaThumbnailDiffUtilCallback(
            visitables.toMutableList() as List<ReviewMediaThumbnailVisitable>,
            items
        )
        val result = DiffUtil.calculateDiff(diffUtilCallback)
        visitables.clear()
        visitables.addAll(items)
        result.dispatchUpdatesTo(this)
    }
}