package com.tokopedia.media.picker.ui.widget.drawerselector.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.media.R
import com.tokopedia.media.databinding.ViewItemSelectionThumbnailBinding
import com.tokopedia.media.picker.ui.uimodel.MediaUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ThumbnailViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding: ViewItemSelectionThumbnailBinding? by viewBinding()

    fun bind(media: MediaUiModel, deletedOnClick: () -> Unit = {}) {
        binding?.imgThumbnail?.smallThumbnail(media)

        binding?.ivDelete?.setOnClickListener {
            deletedOnClick()
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