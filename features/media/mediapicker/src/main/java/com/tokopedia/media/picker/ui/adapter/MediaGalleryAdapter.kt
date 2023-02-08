// This suppress necessary cause the method only executed when removing all items
@file:Suppress("NotifyDataSetChanged")

package com.tokopedia.media.picker.ui.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.media.picker.ui.adapter.delegate.MediaGalleryDelegate
import com.tokopedia.media.picker.ui.adapter.viewholder.MediaGalleryViewHolder
import com.tokopedia.picker.common.uimodel.MediaUiModel
import com.tokopedia.picker.common.utils.wrapper.PickerFile

class MediaGalleryAdapter constructor(
    mediaListener: MediaGalleryViewHolder.Listener
) : BaseDiffUtilAdapter<MediaUiModel>() {

    private val selectedMedias: MutableList<MediaUiModel> = mutableListOf()

    init {
        delegatesManager
            .addDelegate(MediaGalleryDelegate(mediaListener))
    }

    override fun areItemsTheSame(oldItem: MediaUiModel, newItem: MediaUiModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MediaUiModel, newItem: MediaUiModel): Boolean {
        return oldItem == newItem
    }

    fun addSelected(media: MediaUiModel, position: Int) {
        selectedMedias.add(media)
        notifyItemChanged(position)
    }

    fun removeSelected(media: MediaUiModel) {
        val elementIndex = getItems().indexOf(media)
        val selectedMediaIndex = selectedMedias.indexOf(media)

        if (selectedMediaIndex != -1 && elementIndex != -1) {
            selectedMedias.removeAt(selectedMediaIndex)
            notifyItemChanged(elementIndex)
        }
    }

    fun removeSelected(media: MediaUiModel, position: Int) {
        selectedMedias.remove(media)
        notifyItemChanged(position)
    }

    fun removeSubtractionSelected(medias: List<MediaUiModel>) {
        selectedMedias
            .fastSubtract(medias)
            .map {
                removeSelected(it)
            }
    }

    fun removeAllSelectedItem() {
        selectedMedias.clear()
        notifyDataSetChanged()
    }

    fun isMediaSelected(media: MediaUiModel): Boolean {
        return selectedMedias.any {
            it.file == media.file
        }
    }

    private fun List<MediaUiModel>.fastSubtract(
        medias: List<MediaUiModel>
    ): List<MediaUiModel> {
        val elements = mutableMapOf<PickerFile?, Boolean>()
        val results = mutableListOf<MediaUiModel>()

        medias.forEach {
            elements[it.file] = true
        }

        for (i in this.indices) {
            if (elements.containsKey(this[i].file)) {
                continue
            }

            results.add(this[i])
        }

        return results
    }
}
