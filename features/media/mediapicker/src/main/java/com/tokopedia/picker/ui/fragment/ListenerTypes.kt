package com.tokopedia.picker.ui.fragment

import com.tokopedia.picker.data.entity.Directory
import com.tokopedia.picker.data.entity.Media

typealias OnMediaClickListener = (Boolean) -> Boolean
typealias OnFolderClickListener = (Directory) -> Unit
typealias OnImageSelectedListener = (List<Media>) -> Unit