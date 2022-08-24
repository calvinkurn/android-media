package com.tokopedia.review.feature.createreputation.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.review.feature.createreputation.presentation.adapter.diffutil.CreateReviewMediaDiffUtil
import com.tokopedia.review.feature.createreputation.presentation.adapter.typefactory.CreateReviewMediaTypeFactory
import com.tokopedia.review.feature.createreputation.presentation.uimodel.visitable.CreateReviewMediaUiModel

class CreateReviewMediaAdapter(
    typeFactory: CreateReviewMediaTypeFactory
): BaseAdapter<CreateReviewMediaTypeFactory>(typeFactory) {
    
    @Suppress("UNCHECKED_CAST")
    fun setMediaReviewData(data: List<CreateReviewMediaUiModel>) {
        val diffUtilCallback = CreateReviewMediaDiffUtil(
            visitables.toMutableList() as List<CreateReviewMediaUiModel>,
            data
        )
        val result = DiffUtil.calculateDiff(diffUtilCallback)
        visitables.clear()
        visitables.addAll(data)
        result.dispatchUpdatesTo(this)
    }

    interface Listener {
        fun onAddMediaClicked(enabled: Boolean)
        fun onRemoveMediaClicked(media: CreateReviewMediaUiModel)
    }
}
