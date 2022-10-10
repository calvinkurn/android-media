package com.tokopedia.campaignlist.page.presentation.ui.color

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color


interface TokopediaColor {
    val NN0: Color
    val BN50: Color
    val BN200: Color
    val BN400: Color
    val BN800: Color
    val BN950: Color
    val NN600: Color
    val NN950: Color
    val NN900: Color
    val GN500: Color
}

data class UnifyColor(
    override val NN0: Color,
    override val BN50: Color,
    override val BN200: Color,
    override val BN400: Color,
    override val BN800: Color,
    override val BN950: Color,
    override val NN600: Color,
    override val NN900: Color,
    override val NN950: Color,
    override val GN500: Color,
) : TokopediaColor

internal val LocalColors = staticCompositionLocalOf<TokopediaColor> {
    UnifyColor(
        NN0 = UnifyNN0,
        BN50 = UnifyBN50,
        BN200 = UnifyBN200,
        BN400 = UnifyBN400,
        BN800 = UnifyBN800,
        BN950 = UnifyBN950,
        NN600 = UnifyNN600,
        NN900 = UnifyNN900,
        NN950 = UnifyNN950,
        GN500 = UnifyGN500
    )
}

val UnifyNN300 = Color(0xFFBFC9D9)

val UnifyBN50 = Color(0xFFEBFFFE)
val UnifyBN50Dark = Color(0xFF012838)
val UnifyBN200 = Color(0xFF70EAFA)
val UnifyBN200Dark = Color(0xFF035E82)
val UnifyBN400 = Color(0xFF28B9E1)
val UnifyBN400Dark = Color(0xFF0E96CC)
val UnifyBN800 = Color(0xFF144D73)
val UnifyBN800Dark = Color(0xFF73D7FF)
val UnifyBN950 = Color(0xFF102736)
val UnifyBN950Dark = Color(0xFFD4F3FF)


val UnifyGN50 = Color(0xFFECFEF4)
val UnifyGN50Dark = Color(0xFF111C17)
val UnifyGN400 = Color(0xFF20CE7D)
val UnifyGN500 = Color(0xFF00AA5B)
val UnifyGN500Dark = Color(0xFF2ABF70)

//Background
val UnifyNN0 = Color(0xFFFFFFFF)
val UnifyNN0Dark = Color(0xFF1D2025)

val UnifyNN600 = Color(0xFF6D7588)
val UnifyNN600Dark = Color(0xFF808FA1)

val UnifyN700 = Color(0xFF31353B)
val UnifyN700Dark = Color(0xFFFFFFFF)

val UnifyNN900 = Color(0xFF2E3137)
val UnifyNN900Dark = Color(0xFFCBD4E1)

val UnifyNN950 = Color(0xFF212121)
val UnifyNN950Dark = Color(0xFFDCE4ED)