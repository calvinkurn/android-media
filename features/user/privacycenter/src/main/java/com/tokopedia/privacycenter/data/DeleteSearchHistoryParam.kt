package com.tokopedia.privacycenter.data

import android.annotation.SuppressLint

@SuppressLint("ParamFieldAnnotation")
data class DeleteSearchHistoryParam(
    val clearAll: Boolean = false,
    val query: String = "",
    val type: String = "",
    val id: String = "",
    val position: Int = -1
)
