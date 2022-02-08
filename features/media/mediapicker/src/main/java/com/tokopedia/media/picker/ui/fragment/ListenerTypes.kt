package com.tokopedia.media.picker.ui.fragment

import com.tokopedia.media.common.uimodel.AlbumUiModel
import com.tokopedia.media.common.uimodel.MediaUiModel

typealias OnAlbumClickListener = (AlbumUiModel) -> Unit
typealias OnMediaClickListener = (MediaUiModel, Boolean) -> Boolean
typealias OnMediaSelectedListener = (List<MediaUiModel>) -> Unit