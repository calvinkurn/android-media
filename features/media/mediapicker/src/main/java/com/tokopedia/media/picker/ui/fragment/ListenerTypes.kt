package com.tokopedia.media.picker.ui.fragment

import com.tokopedia.media.picker.ui.uimodel.AlbumUiModel
import com.tokopedia.media.picker.ui.uimodel.MediaUiModel

typealias OnAlbumClickListener = (AlbumUiModel) -> Unit
typealias OnMediaClickListener = (MediaUiModel, Boolean) -> Boolean
typealias OnMediaSelectedListener = (List<MediaUiModel>) -> Unit