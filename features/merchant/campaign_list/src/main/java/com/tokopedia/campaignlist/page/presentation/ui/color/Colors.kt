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
    val NN100: Color
    val NN200: Color
    val NN300: Color
    val NN600: Color
    val NN950: Color
    val NN900: Color
    val GN50: Color
    val GN100: Color
    val GN400: Color
    val GN500: Color
    val YN100: Color
    val YN500: Color
    val RN100: Color
    val RN500: Color
}

data class UnifyColor(
    override val NN0: Color,
    override val BN50: Color,
    override val BN200: Color,
    override val BN400: Color,
    override val BN800: Color,
    override val BN950: Color,
    override val NN100: Color,
    override val NN200: Color,
    override val NN300: Color,
    override val NN600: Color,
    override val NN900: Color,
    override val NN950: Color,
    override val GN50: Color,
    override val GN100: Color,
    override val GN400: Color,
    override val GN500: Color,
    override val YN100: Color,
    override val YN500: Color,
    override val RN100: Color,
    override val RN500: Color
) : TokopediaColor

internal val LocalColors = staticCompositionLocalOf<TokopediaColor> {
    UnifyColor(
        NN0 = UnifyNN0,
        BN50 = UnifyBN50,
        BN200 = UnifyBN200,
        BN400 = UnifyBN400,
        BN800 = UnifyBN800,
        BN950 = UnifyBN950,
        NN100 = UnifyNN100,
        NN200 = UnifyNN200,
        NN300 = UnifyNN300,
        NN600 = UnifyNN600,
        NN900 = UnifyNN900,
        NN950 = UnifyNN950,
        GN50 = UnifyGN50,
        GN100 = UnifyGN100,
        GN400 = UnifyGN400,
        GN500 = UnifyGN500,
        YN100 = UnifyYN100,
        YN500 = UnifyYN500,
        RN100 = UnifyRN100,
        RN500 = UnifyRN500
    )
}

val UnifyNN200 = Color(0xFFD6DFEB)
val UnifyNN200Dark = Color(0xFF363B45)
val UnifyNN300 = Color(0xFFBFC9D9)
val UnifyNN300Dark = Color(0xFF3D444F)

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
val UnifyGN100 = Color(0xFFC9FDE0)
val UnifyGN100Dark = Color(0xFF22382E)
val UnifyGN400 = Color(0xFF20CE7D)
val UnifyGN400Dark = Color(0xFF289D5F)
val UnifyGN500 = Color(0xFF00AA5B)
val UnifyGN500Dark = Color(0xFF2ABF70)


val UnifyNN0 = Color(0xFFFFFFFF)
val UnifyNN0Dark = Color(0xFF1D2025)
val UnifyNN100 = Color(0xFFE4EBF5)
val UnifyNN100Dark = Color(0xFF2D323A)
val UnifyNN600 = Color(0xFF6D7588)
val UnifyNN600Dark = Color(0xFF808FA1)
val UnifyNN700 = Color(0xFF31353B)
val UnifyNN700Dark = Color(0xFFFFFFFF)
val UnifyNN900 = Color(0xFF2E3137)
val UnifyNN900Dark = Color(0xFFCBD4E1)
val UnifyNN950 = Color(0xFF212121)
val UnifyNN950Dark = Color(0xFFDCE4ED)


val UnifyYN100 = Color(0xFFFFF1BA)
val UnifyYN100Dark = Color(0xFFB33D09)
val UnifyYN500 = Color(0xFFFF7F17)
val UnifyYN500Dark = Color(0xFFFFA617)

val UnifyRN100 = Color(0xFFFFDBE2)
val UnifyRN100Dark = Color(0xFF862430)
val UnifyRN500 = Color(0xFFF94D63)
val UnifyRN500Dark = Color(0xFFFF6577)

fun getColor(darkTheme: Boolean) : TokopediaColor {
    return if (darkTheme) {
        UnifyColor(
            NN0 = UnifyNN0Dark,
            BN50 = UnifyBN50Dark,
            BN200 = UnifyBN200Dark,
            BN400 = UnifyBN400Dark,
            BN800 = UnifyBN800Dark,
            BN950 = UnifyBN950Dark,
            NN100 = UnifyNN100Dark,
            NN200 = UnifyNN200Dark,
            NN300 = UnifyNN300Dark,
            NN600 = UnifyNN600Dark,
            NN900 = UnifyNN900Dark,
            NN950 = UnifyNN950Dark,
            GN50 = UnifyGN50Dark,
            GN100 = UnifyGN100Dark,
            GN400 = UnifyGN400Dark,
            GN500 = UnifyGN500Dark,
            YN100 = UnifyYN100Dark,
            YN500 = UnifyYN500Dark,
            RN100 = UnifyRN100Dark,
            RN500 = UnifyRN500Dark
        )
    } else {
        UnifyColor(
            NN0 = UnifyNN0,
            BN50 = UnifyBN50,
            BN200 = UnifyBN200,
            BN400 = UnifyBN400,
            BN800 = UnifyBN800,
            BN950 = UnifyBN950,
            NN100 = UnifyNN100,
            NN200 = UnifyNN200,
            NN300 = UnifyNN300,
            NN600 = UnifyNN600,
            NN900 = UnifyNN900,
            NN950 = UnifyNN950,
            GN50 = UnifyGN50,
            GN100 = UnifyGN100,
            GN400 = UnifyGN400,
            GN500 = UnifyGN500,
            YN100 = UnifyYN100,
            YN500 = UnifyYN500,
            RN100 = UnifyRN100,
            RN500 = UnifyRN500
        )
    }
}