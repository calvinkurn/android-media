package com.tokopedia.review.feature.createreputation.presentation.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.review.R
import com.tokopedia.review.databinding.ItemCreateReviewMediaPickerAddLargeBinding
import com.tokopedia.review.feature.createreputation.presentation.adapter.CreateReviewMediaAdapter
import com.tokopedia.review.feature.createreputation.presentation.uimodel.visitable.CreateReviewMediaUiModel

class CreateReviewMediaPickerAddLargeViewHolder(
    view: View,
    private val listener: CreateReviewMediaAdapter.Listener
) : AbstractViewHolder<CreateReviewMediaUiModel.AddLarge>(view) {

    companion object {
        val LAYOUT = R.layout.item_create_review_media_picker_add_large
    }

    private val binding = ItemCreateReviewMediaPickerAddLargeBinding.bind(view)

    init {
        attachListener()
    }

    override fun bind(element: CreateReviewMediaUiModel.AddLarge) {}

    private fun attachListener() {
        binding.root.setOnClickListener { listener.onAddMediaClicked(true) }
    }
}