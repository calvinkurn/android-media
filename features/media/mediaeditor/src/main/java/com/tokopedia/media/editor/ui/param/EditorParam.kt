package com.tokopedia.media.editor.ui.param

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
@SuppressLint("ParamFieldAnnotation")
data class EditorParam(
    val imageUrl: String,
    val editorToolType: Int
) : Parcelable