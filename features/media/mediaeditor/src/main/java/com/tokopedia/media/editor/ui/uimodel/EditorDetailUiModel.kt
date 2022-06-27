package com.tokopedia.media.editor.ui.uimodel

import android.annotation.SuppressLint
import android.os.Parcelable
import com.tokopedia.picker.common.types.EditorToolType
import kotlinx.parcelize.Parcelize

@Parcelize
@SuppressLint("ParamFieldAnnotation")
data class EditorDetailUiModel(
    val imageUrl: String = "",

    @EditorToolType
    val editorToolType: Int = EditorToolType.NONE
) : Parcelable