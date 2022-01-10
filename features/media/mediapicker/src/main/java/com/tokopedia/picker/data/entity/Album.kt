package com.tokopedia.picker.data.entity

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Album(
    var id: Long,
    var name: String,
    var preview: Uri? = null,
    var count: Int = 0
) : Parcelable