package com.tokopedia.review.feature.createreputation.presentation.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.review.feature.createreputation.presentation.adapter.CreateReviewMediaAdapter
import com.tokopedia.review.feature.createreputation.presentation.uimodel.visitable.CreateReviewMediaUiModel
import com.tokopedia.review.feature.createreputation.presentation.viewholder.CreateReviewMediaPickerAddLargeViewHolder
import com.tokopedia.review.feature.createreputation.presentation.viewholder.CreateReviewMediaPickerAddSmallViewHolder
import com.tokopedia.review.feature.createreputation.presentation.viewholder.CreateReviewMediaPreviewImageViewHolder
import com.tokopedia.review.feature.createreputation.presentation.viewholder.CreateReviewMediaPreviewVideoViewHolder

class CreateReviewMediaTypeFactory(
    private val createReviewMediaAdapterListener: CreateReviewMediaAdapter.Listener
): BaseAdapterTypeFactory() {

    @Suppress("UNUSED_PARAMETER")
    fun type(image: CreateReviewMediaUiModel.Image): Int {
        return CreateReviewMediaPreviewImageViewHolder.LAYOUT
    }

    @Suppress("UNUSED_PARAMETER")
    fun type(video: CreateReviewMediaUiModel.Video): Int {
        return CreateReviewMediaPreviewVideoViewHolder.LAYOUT
    }

    @Suppress("UNUSED_PARAMETER")
    fun type(addSmall: CreateReviewMediaUiModel.AddSmall): Int {
        return CreateReviewMediaPickerAddSmallViewHolder.LAYOUT
    }

    @Suppress("UNUSED_PARAMETER")
    fun type(addLarge: CreateReviewMediaUiModel.AddLarge): Int {
        return CreateReviewMediaPickerAddLargeViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            CreateReviewMediaPreviewImageViewHolder.LAYOUT -> CreateReviewMediaPreviewImageViewHolder(parent, createReviewMediaAdapterListener)
            CreateReviewMediaPreviewVideoViewHolder.LAYOUT -> CreateReviewMediaPreviewVideoViewHolder(parent, createReviewMediaAdapterListener)
            CreateReviewMediaPickerAddSmallViewHolder.LAYOUT -> CreateReviewMediaPickerAddSmallViewHolder(parent, createReviewMediaAdapterListener)
            CreateReviewMediaPickerAddLargeViewHolder.LAYOUT -> CreateReviewMediaPickerAddLargeViewHolder(parent, createReviewMediaAdapterListener)
            else -> super.createViewHolder(parent, type)
        }
    }
}