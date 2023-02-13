package com.tokopedia.tokopedianow.common.model

import android.os.Parcelable
import com.tokopedia.image_gallery.ImageGalleryItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class MediaGalleryUiModel(
    val mediaList: List<MediaItemUiModel>,
    val selectedPosition: Int
) : Parcelable {

    fun mapToImageGalleryItems(isAutoPlay: Boolean = false): ArrayList<ImageGalleryItem> {
        return ArrayList(
            mediaList.map { item ->
                ImageGalleryItem(
                    drawable = null,
                    url = item.url,
                    thumbnailUrl = item.thumbnailUrl
                ).setAutoPlay(isAutoPlay)
            }
        )
    }
}