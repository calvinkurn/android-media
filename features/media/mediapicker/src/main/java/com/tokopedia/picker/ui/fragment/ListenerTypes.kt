package com.tokopedia.picker.ui.fragment

import com.tokopedia.picker.data.entity.Album
import com.tokopedia.picker.data.entity.Media

typealias OnAlbumClickListener = (Album) -> Unit
typealias OnMediaClickListener = (Media, Boolean) -> Boolean
typealias OnMediaSelectedListener = (List<Media>) -> Unit