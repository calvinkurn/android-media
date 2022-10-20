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

fun populateColor(darkTheme: Boolean): TokopediaColor {
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

internal val LocalNestColors = staticCompositionLocalOf<NestColors> { NestLightColors() }

interface NestColors {
    val NN: ColorPalette
    val GN: ColorPalette
}

class NestLightColors(
    override val NN: ColorPalette = NestNN.light,
    override val GN: ColorPalette = NestGN.light
): NestColors

class NestNightColors(
    override val NN: ColorPalette = NestNN.night,
    override val GN: ColorPalette = NestGN.night
): NestColors

interface ColorMode {
    val light: ColorPalette
    val night: ColorPalette
}

interface ColorPalette {
    val _0: Color
    val _50: Color
    val _100: Color
    val _200: Color
    val _300: Color
    val _400: Color
    val _500: Color
    val _600: Color
    val _700: Color
    val _800: Color
    val _900: Color
    val _950: Color
    val _1000: Color
}

abstract class LightColorPalette : ColorPalette {
    override val _0: Color = Color(0xFFFFFFFF)
    override val _1000: Color = Color(0xFF000000)
}

abstract class DarkColorPalette : ColorPalette {
    override val _0: Color = Color(0xFF121212)
    override val _1000: Color = Color(0xFFFFFFFF)
}

object NestNN: ColorMode {

    override val light: ColorPalette = object : LightColorPalette() {
        override val _50: Color = Color(0xFFF0F3F7)
        override val _100: Color = Color(0xFFE4EBF5)
        override val _200: Color = Color(0xFFD6DFEB)
        override val _300: Color = Color(0xFFBFC9D9)
        override val _400: Color = Color(0xFFAAB4C8)
        override val _500: Color = Color(0xFF8D96AA)
        override val _600: Color = Color(0xFF6D7588)
        override val _700: Color = Color(0xFF525867)
        override val _800: Color = Color(0xFF40444E)
        override val _900: Color = Color(0xFF2E3137)
        override val _950: Color = Color(0xFF212121)
    }

    override val night: ColorPalette = object : DarkColorPalette() {
        override val _50: Color = Color(0xFF222329)
        override val _100: Color = Color(0xFF2E2F36)
        override val _200: Color = Color(0xFF3B3D45)
        override val _300: Color = Color(0xFF4F525C)
        override val _400: Color = Color(0xFF696D78)
        override val _500: Color = Color(0xFF878B99)
        override val _600: Color = Color(0xFFAEB2BF)
        override val _700: Color = Color(0xFFC0C2CC)
        override val _800: Color = Color(0xFFD5D8E0)
        override val _900: Color = Color(0xFFE9EBF5)
        override val _950: Color = Color(0xFFF5F6FF)
    }
}

object NestGN: ColorMode {

    override val light: ColorPalette = object : LightColorPalette() {
        override val _50: Color = Color(0xFFECFEF4)
        override val _100: Color = Color(0xFFC9FDE0)
        override val _200: Color = Color(0xFF83ECB2)
        override val _300: Color = Color(0xFF4FE397)
        override val _400: Color = Color(0xFF20CE7D)
        override val _500: Color = Color(0xFF098A4E)
        override val _600: Color = Color(0xFF34B670)
        override val _700: Color = Color(0xFF176C45)
        override val _800: Color = Color(0xFF145638)
        override val _900: Color = Color(0xFF064329)
        override val _950: Color = Color(0xFF05301E)
    }

    override val night: ColorPalette = object : DarkColorPalette() {
        override val _50: Color = Color(0xFF0A260F)
        override val _100: Color = Color(0xFF005823)
        override val _200: Color = Color(0xFF007335)
        override val _300: Color = Color(0xFF008842)
        override val _400: Color = Color(0xFF009A4E)
        override val _500: Color = Color(0xFF00A958)
        override val _600: Color = Color(0xFF34B670)
        override val _700: Color = Color(0xFF60C289)
        override val _800: Color = Color(0xFF91D3AA)
        override val _900: Color = Color(0xFFBCE4CB)
        override val _950: Color = Color(0xFFE3F4EA)
    }
}