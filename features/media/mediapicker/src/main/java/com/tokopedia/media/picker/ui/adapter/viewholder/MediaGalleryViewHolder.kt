package com.tokopedia.media.picker.ui.adapter.viewholder

import android.view.View
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.databinding.ViewItemGalleryPickerBinding
import com.tokopedia.picker.common.uimodel.MediaUiModel
import com.tokopedia.utils.view.binding.viewBinding

class MediaGalleryViewHolder(
    view: View,
    private val listener: Listener?
) : BaseViewHolder(view) {

    private val binding: ViewItemGalleryPickerBinding? by viewBinding()

    fun bind(data: MediaUiModel?) {
        if (data == null) return
        val isSelected = listener?.isMediaSelected(data) == true

        binding?.icCheck?.showWithCondition(isSelected)
        binding?.viewSelected?.alpha = if (isSelected) 0.5f else 0f
        binding?.imgThumbnail?.regularThumbnail(data)

        itemView.setOnClickListener {
            listener?.onMediaItemClicked(data, bindingAdapterPosition)
        }
    }

    interface Listener {
        fun onMediaItemClicked(data: MediaUiModel, position: Int)
        fun isMediaSelected(data: MediaUiModel): Boolean
    }
}
