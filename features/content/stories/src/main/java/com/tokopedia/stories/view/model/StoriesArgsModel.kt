package com.tokopedia.stories.view.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StoriesArgsModel(
    val authorId: String = "",
    val authorType: String = "",
    val source: String = "",
    val sourceId: String = "",
    val entryPoint: String = "",
) : Parcelable
