package com.tokopedia.logisticcart.shipping.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CoachmarkPlusData(
    val isShown: Boolean = false,
    val title: String = "",
    val content: String = ""
) : Parcelable
