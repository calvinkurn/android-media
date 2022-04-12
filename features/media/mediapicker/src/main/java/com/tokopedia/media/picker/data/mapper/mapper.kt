package com.tokopedia.media.picker.data.mapper

import com.tokopedia.media.picker.data.entity.Album
import com.tokopedia.media.picker.data.entity.Media
import com.tokopedia.picker.common.uimodel.AlbumUiModel
import com.tokopedia.picker.common.uimodel.MediaUiModel

fun Media.toUiModel() = MediaUiModel(
    id = id,
    name = name,
    path = path,
    uri = uri,
)

@JvmName("listMediaToListUiModel")
fun List<Media>.toUiModel() = map {
    it.toUiModel()
}

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