package com.tokopedia.editor.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VideoModel(
    var isAudioMute: Boolean = false,

    /**
     * 1. text
     * 2. configuration
     */
    var texts: Map<String, InputTextModel> = emptyMap()
) : Parcelable
