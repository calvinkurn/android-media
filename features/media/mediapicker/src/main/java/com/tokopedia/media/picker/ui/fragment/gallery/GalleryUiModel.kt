package com.tokopedia.media.picker.ui.fragment.gallery

import android.os.Parcelable
import com.tokopedia.media.picker.data.MediaQueryDataSourceImpl.Companion.BUCKET_ALL_MEDIA_ID
import kotlinx.parcelize.Parcelize

@Parcelize
data class GalleryUiModel(
    var bucketId: Long = BUCKET_ALL_MEDIA_ID,
    var hasChangeAlbum: Boolean = true
) : Parcelable {

    companion object {
        const val KEY = "gallery_fragment_model"
    }
}
