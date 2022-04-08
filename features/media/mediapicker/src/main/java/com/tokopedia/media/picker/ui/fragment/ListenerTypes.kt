package com.tokopedia.media.picker.ui.fragment

import com.tokopedia.picker.common.uimodel.AlbumUiModel
import com.tokopedia.picker.common.uimodel.MediaUiModel

internal typealias OnAlbumClickListener = (AlbumUiModel) -> Unit
internal typealias OnMediaClickListener = (MediaUiModel, Boolean) -> Boolean
internal typealias OnMediaSelectedListener = (List<MediaUiModel>) -> Unit