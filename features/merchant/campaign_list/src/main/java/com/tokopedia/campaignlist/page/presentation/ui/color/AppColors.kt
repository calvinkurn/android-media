package com.tokopedia.campaignlist.page.presentation.ui.color

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color


data class PredefinedColor(
    val NN950: Color,
    val GN500: Color
)


internal val LocalColors = staticCompositionLocalOf { PredefinedColor(UnifyNN950, UnifyGN500) }



val UnifyNN300 = Color(0xFFBFC9D9)

val UnifyGN50 = Color(0xFFECFEF4)
val UnifyGN50Dark = Color(0xFF111C17)
val UnifyGN400 = Color(0xFF20CE7D)
val UnifyGN500 = Color(0xFF2ABF70)
val UnifyGN500Dark = Color(0xFF2ABF70)

//Background
val UnifyNN0 = Color(0xFFFFFFFF)
val UnifyNN0Dark = Color(0xFF1D2025)

val UnifyN700 = Color(0xFF31353B)
val UnifyN700Dark = Color(0xFFFFFFFF)

val UnifyNN950 = Color(0xFF212121)
val UnifyNN950Dark = Color(0xFFDCE4ED)