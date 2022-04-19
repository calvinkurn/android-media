package com.tokopedia.review.feature.reading.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.review.feature.reading.data.LikeDislike
import com.tokopedia.review.feature.reading.presentation.adapter.uimodel.ReadReviewUiModel

class ReadReviewAdapter(
        readReviewAdapterTypeFactory: ReadReviewAdapterTypeFactory
) : BaseListAdapter<ReadReviewUiModel, ReadReviewAdapterTypeFactory>(readReviewAdapterTypeFactory) {

    fun updateLikeStatus(index: Int, newLikeCount: Int, likeStatus: Int) {
        (visitables.getOrNull(index) as? ReadReviewUiModel)?.reviewData?.likeDislike = LikeDislike(newLikeCount, likeStatus)
        notifyItemChanged(index)
    }
}