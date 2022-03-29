package com.tokopedia.reviewcommon.feature.media.gallery.detailed.presentation.uimodel

import com.tokopedia.reviewcommon.uimodel.StringRes

data class ToasterUiModel(
    val key: String,
    val message: StringRes,
    val type: Int,
    val duration: Int,
    val actionText: StringRes
)
