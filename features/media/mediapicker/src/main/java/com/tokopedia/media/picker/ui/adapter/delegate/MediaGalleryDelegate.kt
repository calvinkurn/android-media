package com.tokopedia.media.picker.ui.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.media.picker.ui.adapter.viewholder.MediaGalleryViewHolder
import com.tokopedia.media.R
import com.tokopedia.picker.common.uimodel.MediaUiModel

class MediaGalleryDelegate constructor(
    private val listener: MediaGalleryViewHolder.Listener?
) : TypedAdapterDelegate<MediaUiModel, MediaUiModel, MediaGalleryViewHolder>(
    R.layout.view_item_gallery_picker
) {
    override fun onBindViewHolder(item: MediaUiModel, holder: MediaGalleryViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): MediaGalleryViewHolder {
        return MediaGalleryViewHolder(basicView, listener)
    }

}
