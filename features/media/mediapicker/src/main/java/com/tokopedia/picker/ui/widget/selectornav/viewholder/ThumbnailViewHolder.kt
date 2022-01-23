package com.tokopedia.picker.ui.widget.selectornav.viewholder

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.picker.R
import com.tokopedia.picker.databinding.ViewItemSelectionThumbnailBinding
import com.tokopedia.picker.ui.uimodel.MediaUiModel
import com.tokopedia.picker.utils.isVideoFormat
import com.tokopedia.picker.utils.pickerLoadImage
import com.tokopedia.picker.utils.extractVideoDuration
import com.tokopedia.picker.utils.toVideoDurationFormat
import com.tokopedia.utils.view.binding.viewBinding

class ThumbnailViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding: ViewItemSelectionThumbnailBinding? by viewBinding()
    private val context by lazy { itemView.context }

    fun bind(media: MediaUiModel, deletedOnClick: () -> Unit = {}) {
        binding?.imageView?.pickerLoadImage(media.path)
        renderVideoDuration(media.path)

        binding?.ivDelete?.setOnClickListener {
            deletedOnClick()
        }
    }

    private fun renderVideoDuration(path: String) {
        if (isVideoFormat(path)) {
            val durationVideo = extractVideoDuration(context, Uri.parse(path))
            binding?.tvDuration?.text = durationVideo.toVideoDurationFormat()
            binding?.tvDuration?.show()
        } else {
            binding?.tvDuration?.hide()
        }
    }

    companion object {
        @LayoutRes val LAYOUT = R.layout.view_item_selection_thumbnail

        fun create(viewGroup: ViewGroup): ThumbnailViewHolder {
            val view = LayoutInflater
                .from(viewGroup.context)
                .inflate(LAYOUT, viewGroup, false)

            return ThumbnailViewHolder(view)
        }
    }

}