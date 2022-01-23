package com.tokopedia.picker.data.mapper

import com.tokopedia.picker.data.entity.Album
import com.tokopedia.picker.data.entity.Media
import com.tokopedia.picker.ui.uimodel.AlbumUiModel
import com.tokopedia.picker.ui.uimodel.MediaUiModel

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