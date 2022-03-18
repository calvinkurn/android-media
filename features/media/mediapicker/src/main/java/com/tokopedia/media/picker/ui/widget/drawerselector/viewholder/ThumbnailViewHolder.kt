package com.tokopedia.media.picker.ui.widget.drawerselector.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.R
import com.tokopedia.media.databinding.ViewItemSelectionThumbnailBinding
import com.tokopedia.picker.common.uimodel.MediaUiModel
import com.tokopedia.picker.common.utils.safeFileDelete
import com.tokopedia.utils.view.binding.viewBinding

class ThumbnailViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding: ViewItemSelectionThumbnailBinding? by viewBinding()
    private val context by lazy { itemView.context }

    fun bind(
        media: MediaUiModel,
        isDraggable: Boolean,
        onClicked: () -> Unit = {},
        onRemoved: () -> Unit = {}
    ) {
        binding?.imgThumbnail?.smallThumbnail(media)
        binding?.btnDrag?.showWithCondition(isDraggable)

        binding?.imgThumbnail?.setOnClickListener {
            onClicked()
        }

        binding?.ivDelete?.setOnClickListener {
            if (media.isFromPickerCamera) {
                onShowDeletionDialog(media.path) { onRemoved() }
            } else {
                onRemoved()
            }
        }
    }

    private fun onShowDeletionDialog(path: String, onRemoved: () -> Unit = {}) {
        val dialog = DialogUnify(context, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE)

        dialog.setTitle(context.getString(R.string.picker_title_deletion))
        dialog.setDescription(context.getString(R.string.picker_deletion_message))
        dialog.setPrimaryCTAText(context.getString(R.string.picker_button_delete_confirm))
        dialog.setSecondaryCTAText(context.getString(R.string.picker_button_cancel))

        dialog.setPrimaryCTAClickListener {
            safeFileDelete(path)
            onRemoved()
            dialog.dismiss()
        }

        dialog.setOnDismissListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    fun setThumbnailSelected(isSelected: Boolean){
        binding?.imgThumbnail?.setThumbnailSelected(isSelected)
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