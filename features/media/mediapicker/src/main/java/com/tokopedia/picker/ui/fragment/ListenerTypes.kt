package com.tokopedia.picker.ui.fragment

import com.tokopedia.picker.ui.uimodel.AlbumUiModel
import com.tokopedia.picker.ui.uimodel.MediaUiModel

typealias OnAlbumClickListener = (AlbumUiModel) -> Unit
typealias OnMediaClickListener = (MediaUiModel, Boolean) -> Boolean
typealias OnMediaSelectedListener = (List<MediaUiModel>) -> Unit