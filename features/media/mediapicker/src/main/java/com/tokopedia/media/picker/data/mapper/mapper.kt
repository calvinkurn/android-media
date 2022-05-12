package com.tokopedia.media.picker.data.mapper

import com.tokopedia.media.picker.data.entity.Album
import com.tokopedia.media.picker.data.entity.Media
import com.tokopedia.picker.common.uimodel.AlbumUiModel
import com.tokopedia.picker.common.uimodel.MediaUiModel

fun mediaToUiModel(mediaList: List<Media>): List<MediaUiModel>{
    return mediaList.map {
        MediaUiModel(it.id, it.name, it.path, it.uri)
    }
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