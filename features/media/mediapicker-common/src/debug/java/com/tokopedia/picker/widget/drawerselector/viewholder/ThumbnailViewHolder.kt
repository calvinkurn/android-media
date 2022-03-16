package com.tokopedia.picker.widget.drawerselector.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.picker.common.R
import com.tokopedia.picker.common.databinding.ViewItemSelectionThumbnailBinding
import com.tokopedia.picker.common.uimodel.MediaUiModel
import com.tokopedia.utils.view.binding.viewBinding
import java.io.File

class ThumbnailViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding: ViewItemSelectionThumbnailBinding? by viewBinding()
    private val context by lazy { itemView.context }

    fun bind(media: MediaUiModel, onClicked: () -> Unit = {}, onRemoved: () -> Unit = {}) {
        binding?.imgThumbnail?.smallThumbnail(media)

        binding?.imgThumbnail?.setOnClickListener {
            onClicked()
        }

        binding?.ivDelete?.setOnClickListener {
//            onRemoved()
        }
    }

    private fun deleteFile(path: String) {
        val file = File(path)

        if (file.exists()) {
            file.delete()
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