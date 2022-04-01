package com.tokopedia.review.feature.reading.presentation.adapter

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.review.feature.reading.data.LikeDislike
import com.tokopedia.review.feature.reading.presentation.adapter.uimodel.ReadReviewUiModel
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaThumbnailVisitable

class ReadReviewAdapter(
        readReviewAdapterTypeFactory: ReadReviewAdapterTypeFactory
) : BaseListAdapter<ReadReviewUiModel, ReadReviewAdapterTypeFactory>(readReviewAdapterTypeFactory) {

    fun updateLikeStatus(index: Int, newLikeCount: Int, likeStatus: Int) {
        (visitables.getOrNull(index) as? ReadReviewUiModel)?.reviewData?.likeDislike = LikeDislike(newLikeCount, likeStatus)
        notifyItemChanged(index)
    }

    fun findReviewContainingThumbnail(item: ReviewMediaThumbnailVisitable): Pair<Int, ReadReviewUiModel>? {
        return visitables.filterIsInstance<ReadReviewUiModel>().let { readReviewUiModels ->
            readReviewUiModels.indexOfFirst {
                it.mediaThumbnails.mediaThumbnails.contains(item)
            }.takeIf { it != RecyclerView.NO_POSITION }?.let {
                it to readReviewUiModels[it]
            }
        }
    }
}