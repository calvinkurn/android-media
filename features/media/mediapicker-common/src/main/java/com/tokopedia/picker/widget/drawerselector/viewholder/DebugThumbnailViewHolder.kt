package com.tokopedia.picker.widget.drawerselector.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.picker.common.R
import com.tokopedia.picker.common.databinding.ViewItemSelectionThumbnailDebugBinding
import com.tokopedia.picker.common.uimodel.MediaUiModel
import com.tokopedia.utils.view.binding.viewBinding
import java.io.File

class DebugThumbnailViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding: ViewItemSelectionThumbnailDebugBinding? by viewBinding()

    fun bind(media: MediaUiModel, onClicked: () -> Unit = {}) {
        binding?.imgThumbnail?.smallThumbnail(media)

        binding?.imgThumbnail?.setOnClickListener {
            onClicked()
        }
    }

    companion object {
        @LayoutRes val LAYOUT = R.layout.view_item_selection_thumbnail_debug

        fun create(viewGroup: ViewGroup): DebugThumbnailViewHolder {
            val view = LayoutInflater
                .from(viewGroup.context)
                .inflate(LAYOUT, viewGroup, false)

            return DebugThumbnailViewHolder(view)
        }
    }

}