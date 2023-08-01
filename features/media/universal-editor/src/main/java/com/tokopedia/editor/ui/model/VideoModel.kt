package com.tokopedia.editor.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VideoModel(
    var isAudioMute: Boolean = false,
    var texts: List<InputTextModel> = emptyList()
) : Parcelable
