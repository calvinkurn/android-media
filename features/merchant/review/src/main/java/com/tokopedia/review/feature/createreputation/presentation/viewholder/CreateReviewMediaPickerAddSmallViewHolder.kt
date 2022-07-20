package com.tokopedia.review.feature.createreputation.presentation.viewholder

import android.os.Bundle
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

    override fun bind(element: CreateReviewMediaUiModel.AddSmall) {
        setupEnableState(element.enabled)
    }

    override fun bind(element: CreateReviewMediaUiModel.AddSmall?, payloads: MutableList<Any>) {
        payloads.firstOrNull().let { payload ->
            if (payload is Bundle) {
                payload.get(CreateReviewMediaUiModel.PAYLOAD_ADD_MEDIA_ENABLE_STATE).let {
                    if (it is Boolean) {
                        setupEnableState(it)
                    }
                }
            }
        }
    }

    private fun setupEnableState(enabled: Boolean) {
        binding.icReviewMediaPickerCamera.isEnabled = enabled
        if (enabled) attachClickListener() else detachClickListener()
    }

    private fun detachClickListener() {
        binding.root.setOnClickListener {}
    }

    private fun attachClickListener() {
        binding.root.setOnClickListener {
            listener.onAddMediaClicked()
        }
    }
}