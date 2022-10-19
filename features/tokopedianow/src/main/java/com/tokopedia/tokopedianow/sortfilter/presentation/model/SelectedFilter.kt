package com.tokopedia.tokopedianow.sortfilter.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SelectedFilter(
    val id: String,
    val parentId: String = "",
    val text: String = ""
): Parcelable