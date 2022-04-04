package com.tokopedia.review.feature.createreputation.presentation.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.review.R
import com.tokopedia.review.databinding.ItemCreateReviewMediaPickerAddSmallBinding
import com.tokopedia.review.feature.createreputation.presentation.adapter.CreateReviewMediaAdapter
import com.tokopedia.review.feature.createreputation.presentation.uimodel.visitable.CreateReviewMediaUiModel

class CreateReviewMediaPickerAddSmallViewHolder(
    view: View,
    private val listener: CreateReviewMediaAdapter.Listener
) : AbstractViewHolder<CreateReviewMediaUiModel.AddSmall>(view) {

    companion object {
        val LAYOUT = R.layout.item_create_review_media_picker_add_small
    }

    private val binding = ItemCreateReviewMediaPickerAddSmallBinding.bind(view)

    init {
        binding.root.setOnClickListener {
            listener.onAddMediaClicked()
        }
    }

    override fun bind(element: CreateReviewMediaUiModel.AddSmall) {}
}