package com.tokopedia.promousage.domain.entity.list

import android.os.Parcelable
import androidx.annotation.ColorRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class PromoSimpleItem(
    val title: String,
    val type: String,
    val typeColor: String,
    val typeColorDark: String,
    val desc: String,
    val backgroundUrl: String,
    val backgroundUrlDark: String,
    val iconUrl: String,
    val iconUrlDark: String,
    @ColorRes val curveColor: Int,
    @ColorRes val curveColorDark: Int,
    val curveAlpha: Int = 255, // max 255
    val curveAlphaDark: Int = 255 // max 255
) : Parcelable
