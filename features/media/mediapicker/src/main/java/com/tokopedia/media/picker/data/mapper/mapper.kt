package com.tokopedia.media.picker.data.mapper

import com.tokopedia.media.picker.data.entity.Album
import com.tokopedia.media.picker.data.entity.Media
import com.tokopedia.picker.common.uimodel.AlbumUiModel
import com.tokopedia.picker.common.uimodel.MediaUiModel
import com.tokopedia.picker.common.utils.wrapper.PickerFile.Companion.asPickerFile

fun mediaToUiModel(mediaList: List<Media>): List<MediaUiModel> {
    return mediaList.map {
        MediaUiModel(
            id = it.id,
            uri = it.uri,
            file = it.path.asPickerFile(),
        )
    }
}

fun MediaUiModel.toModel() = Media(
    id = id,
    file = file!!
)

fun Album.toUiModel() = AlbumUiModel(
    id = id,
    name = name,
    preview = preview,
    count = count,
)

@JvmName("listAlbumToListUiModel")
fun List<Album>.toUiModel() = map {
    it.toUiModel()
}
