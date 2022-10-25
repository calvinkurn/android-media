package com.tokopedia.common_compose.ui

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
    val NN500: Color
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

data class NestColor(
    override val NN0: Color,
    override val BN50: Color,
    override val BN200: Color,
    override val BN400: Color,
    override val BN800: Color,
    override val BN950: Color,
    override val NN100: Color,
    override val NN200: Color,
    override val NN300: Color,
    override val NN500: Color,
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
    NestColor(
        NN0 = NestNN0,
        BN50 = NestBN50,
        BN200 = NestBN200,
        BN400 = NestBN400,
        BN800 = NestBN800,
        BN950 = NestBN950,
        NN100 = NestNN100,
        NN200 = NestNN200,
        NN300 = NestNN300,
        NN500 = NestNN500,
        NN600 = NestNN600,
        NN900 = NestNN900,
        NN950 = NestNN950,
        GN50 = NestGN50,
        GN100 = NestGN100,
        GN400 = NestGN400,
        GN500 = NestGN500,
        YN100 = NestYN100,
        YN500 = NestYN500,
        RN100 = NestRN100,
        RN500 = NestRN500
    )
}

val NestNN200 = Color(0xFFD6DFEB)
val NestNN200Dark = Color(0xFF363B45)
val NestNN300 = Color(0xFFBFC9D9)
val NestNN300Dark = Color(0xFF3D444F)

val NestBN50 = Color(0xFFEBFFFE)
val NestBN50Dark = Color(0xFF012838)
val NestBN200 = Color(0xFF70EAFA)
val NestBN200Dark = Color(0xFF035E82)
val NestBN400 = Color(0xFF28B9E1)
val NestBN400Dark = Color(0xFF0E96CC)
val NestBN800 = Color(0xFF144D73)
val NestBN800Dark = Color(0xFF73D7FF)
val NestBN950 = Color(0xFF102736)
val NestBN950Dark = Color(0xFFD4F3FF)



val NestGN50 = Color(0xFFECFEF4)
val NestGN50Dark = Color(0xFF111C17)
val NestGN100 = Color(0xFFC9FDE0)
val NestGN100Dark = Color(0xFF22382E)
val NestGN400 = Color(0xFF20CE7D)
val NestGN400Dark = Color(0xFF289D5F)
val NestGN500 = Color(0xFF00AA5B)
val NestGN500Dark = Color(0xFF2ABF70)


val NestNN0 = Color(0xFFFFFFFF)
val NestNN0Dark = Color(0xFF1D2025)
val NestNN100 = Color(0xFFE4EBF5)
val NestNN100Dark = Color(0xFF2D323A)
val NestNN500 = Color(0xFF8D96AA)
val NestNN500Dark = Color(0xFF5D6775)
val NestNN600 = Color(0xFF6D7588)
val NestNN600Dark = Color(0xFF808FA1)
val NestNN700 = Color(0xFF31353B)
val NestNN700Dark = Color(0xFFFFFFFF)
val NestNN900 = Color(0xFF2E3137)
val NestNN900Dark = Color(0xFFCBD4E1)
val NestNN950 = Color(0xFF212121)
val NestNN950Dark = Color(0xFFDCE4ED)


val NestYN100 = Color(0xFFFFF1BA)
val NestYN100Dark = Color(0xFFB33D09)
val NestYN500 = Color(0xFFFF7F17)
val NestYN500Dark = Color(0xFFFFA617)

val NestRN100 = Color(0xFFFFDBE2)
val NestRN100Dark = Color(0xFF862430)
val NestRN500 = Color(0xFFF94D63)
val NestRN500Dark = Color(0xFFFF6577)

fun populateColor(darkTheme: Boolean) : TokopediaColor {
    return if (darkTheme) {
        NestColor(
            NN0 = NestNN0Dark,
            BN50 = NestBN50Dark,
            BN200 = NestBN200Dark,
            BN400 = NestBN400Dark,
            BN800 = NestBN800Dark,
            BN950 = NestBN950Dark,
            NN100 = NestNN100Dark,
            NN200 = NestNN200Dark,
            NN300 = NestNN300Dark,
            NN500 = NestNN500Dark,
            NN600 = NestNN600Dark,
            NN900 = NestNN900Dark,
            NN950 = NestNN950Dark,
            GN50 = NestGN50Dark,
            GN100 = NestGN100Dark,
            GN400 = NestGN400Dark,
            GN500 = NestGN500Dark,
            YN100 = NestYN100Dark,
            YN500 = NestYN500Dark,
            RN100 = NestRN100Dark,
            RN500 = NestRN500Dark
        )
    } else {
        NestColor(
            NN0 = NestNN0,
            BN50 = NestBN50,
            BN200 = NestBN200,
            BN400 = NestBN400,
            BN800 = NestBN800,
            BN950 = NestBN950,
            NN100 = NestNN100,
            NN200 = NestNN200,
            NN300 = NestNN300,
            NN500 = NestNN500,
            NN600 = NestNN600,
            NN900 = NestNN900,
            NN950 = NestNN950,
            GN50 = NestGN50,
            GN100 = NestGN100,
            GN400 = NestGN400,
            GN500 = NestGN500,
            YN100 = NestYN100,
            YN500 = NestYN500,
            RN100 = NestRN100,
            RN500 = NestRN500
        )
    }
}