package com.tokopedia.picker.common.uimodel

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AlbumUiModel(
    var id: Long = 0L,
    var name: String = "",
    var uri: Uri? = null,
    var count: Int = 0
) : Parcelable
