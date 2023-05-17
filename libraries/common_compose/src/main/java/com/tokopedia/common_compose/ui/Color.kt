package com.tokopedia.common_compose.ui

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

internal val LocalNestColor = staticCompositionLocalOf<NestColor> {
    NestLightColor()
}

interface NestColor {
    val NN: ColorPalette
    val GN: ColorPalette
    val RN: ColorPalette
    val YN: ColorPalette
    val PN: ColorPalette
    val BN: ColorPalette
    val TN: ColorPalette
}

class NestLightColor internal constructor(
    override val NN: ColorPalette = NestNN.light,
    override val GN: ColorPalette = NestGN.light,
    override val RN: ColorPalette = NestRN.light,
    override val YN: ColorPalette = NestYN.light,
    override val PN: ColorPalette = NestPN.light,
    override val BN: ColorPalette = NestBN.light,
    override val TN: ColorPalette = NestTN.light
) : NestColor

class NestDarkColor internal constructor(
    override val NN: ColorPalette = NestNN.dark,
    override val GN: ColorPalette = NestGN.dark,
    override val RN: ColorPalette = NestRN.dark,
    override val YN: ColorPalette = NestYN.dark,
    override val PN: ColorPalette = NestPN.dark,
    override val BN: ColorPalette = NestBN.dark,
    override val TN: ColorPalette = NestTN.dark
) : NestColor

interface ColorMode<T> {
    val light: T
    val dark: T
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

object NestNN : ColorMode<ColorPalette> {

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

    override val dark: ColorPalette = object : DarkColorPalette() {
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

object NestGN : ColorMode<ColorPalette> {

    override val light: ColorPalette = object : LightColorPalette() {
        override val _50: Color = Color(0xFFECFEF4)
        override val _100: Color = Color(0xFFC9FDE0)
        override val _200: Color = Color(0xFF83ECB2)
        override val _300: Color = Color(0xFF4FE397)
        override val _400: Color = Color(0xFF20CE7D)
        override val _500: Color = Color(0xFF00AA5B)
        override val _600: Color = Color(0xFF098A4E)
        override val _700: Color = Color(0xFF176C45)
        override val _800: Color = Color(0xFF145638)
        override val _900: Color = Color(0xFF064329)
        override val _950: Color = Color(0xFF05301E)
    }

    override val dark: ColorPalette = object : DarkColorPalette() {
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

object NestRN : ColorMode<ColorPalette> {

    override val light: ColorPalette = object : LightColorPalette() {
        override val _50: Color = Color(0xFFFFF5F6)
        override val _100: Color = Color(0xFFFFDBE2)
        override val _200: Color = Color(0xFFFFB2C2)
        override val _300: Color = Color(0xFFFF919F)
        override val _400: Color = Color(0xFFFF7182)
        override val _500: Color = Color(0xFFF94D63)
        override val _600: Color = Color(0xFFE02954)
        override val _700: Color = Color(0xFFB31D40)
        override val _800: Color = Color(0xFF921633)
        override val _900: Color = Color(0xFF701027)
        override val _950: Color = Color(0xFF51081E)
    }

    override val dark: ColorPalette = object : DarkColorPalette() {
        override val _50: Color = Color(0xFF430F16)
        override val _100: Color = Color(0xFF7C1523)
        override val _200: Color = Color(0xFF94192A)
        override val _300: Color = Color(0xFFB21E33)
        override val _400: Color = Color(0xFFD4263F)
        override val _500: Color = Color(0xFFFF425D)
        override val _600: Color = Color(0xFFFF5E71)
        override val _700: Color = Color(0xFFFF7D8C)
        override val _800: Color = Color(0xFFFFA3AE)
        override val _900: Color = Color(0xFFFFC7CD)
        override val _950: Color = Color(0xFFF9E8EC)
    }
}

object NestYN : ColorMode<ColorPalette> {

    override val light: ColorPalette = object : LightColorPalette() {
        override val _50: Color = Color(0xFFFFFAE6)
        override val _100: Color = Color(0xFFFFF1BA)
        override val _200: Color = Color(0xFFFFDD7F)
        override val _300: Color = Color(0xFFFFC400)
        override val _400: Color = Color(0xFFFFA617)
        override val _500: Color = Color(0xFFFF7F17)
        override val _600: Color = Color(0xFFF36819)
        override val _700: Color = Color(0xFFCC4C14)
        override val _800: Color = Color(0xFF992F00)
        override val _900: Color = Color(0xFF6B1F03)
        override val _950: Color = Color(0xFF431403)
    }

    override val dark: ColorPalette = object : DarkColorPalette() {
        override val _50: Color = Color(0xFF522512)
        override val _100: Color = Color(0xFF873C1C)
        override val _200: Color = Color(0xFFB25025)
        override val _300: Color = Color(0xFFC9652A)
        override val _400: Color = Color(0xFFEB7E2A)
        override val _500: Color = Color(0xFFF29E2B)
        override val _600: Color = Color(0xFFFFB947)
        override val _700: Color = Color(0xFFFFC76E)
        override val _800: Color = Color(0xFFFFD591)
        override val _900: Color = Color(0xFFFFE5BA)
        override val _950: Color = Color(0xFFFFF9E4)
    }
}

object NestPN : ColorMode<ColorPalette> {

    override val light: ColorPalette = object : LightColorPalette() {
        override val _50: Color = Color(0xFFFBF4FF)
        override val _100: Color = Color(0xFFF0DAFF)
        override val _200: Color = Color(0xFFDEAFFF)
        override val _300: Color = Color(0xFFCB90FF)
        override val _400: Color = Color(0xFFB26EF5)
        override val _500: Color = Color(0xFF9342ED)
        override val _600: Color = Color(0xFF7F37D7)
        override val _700: Color = Color(0xFF7421D9)
        override val _800: Color = Color(0xFF5D1BB4)
        override val _900: Color = Color(0xFF441188)
        override val _950: Color = Color(0xFF330D65)
    }

    override val dark: ColorPalette = object : DarkColorPalette() {
        override val _50: Color = Color(0xFF241A30)
        override val _100: Color = Color(0xFF451575)
        override val _200: Color = Color(0xFF631FA8)
        override val _300: Color = Color(0xFF8035CC)
        override val _400: Color = Color(0xFF914AD9)
        override val _500: Color = Color(0xFFA660EB)
        override val _600: Color = Color(0xFFBD7AFF)
        override val _700: Color = Color(0xFFCB96FF)
        override val _800: Color = Color(0xFFD9B2FF)
        override val _900: Color = Color(0xFFE8D1FF)
        override val _950: Color = Color(0xFFF3E6FF)
    }
}

object NestBN : ColorMode<ColorPalette> {

    override val light: ColorPalette = object : LightColorPalette() {
        override val _50: Color = Color(0xFFEBFFFE)
        override val _100: Color = Color(0xFFC5FCFF)
        override val _200: Color = Color(0xFF70EAFA)
        override val _300: Color = Color(0xFF46D8F1)
        override val _400: Color = Color(0xFF28B9E1)
        override val _500: Color = Color(0xFF0094CF)
        override val _600: Color = Color(0xFF0479B9)
        override val _700: Color = Color(0xFF06649E)
        override val _800: Color = Color(0xFF144D73)
        override val _900: Color = Color(0xFF13354F)
        override val _950: Color = Color(0xFF102736)
    }

    override val dark: ColorPalette = object : DarkColorPalette() {
        override val _50: Color = Color(0xFF062236)
        override val _100: Color = Color(0xFF1F416B)
        override val _200: Color = Color(0xFF2A5E91)
        override val _300: Color = Color(0xFF3A77B5)
        override val _400: Color = Color(0xFF4490D4)
        override val _500: Color = Color(0xFF57A3E5)
        override val _600: Color = Color(0xFF5DB6F5)
        override val _700: Color = Color(0xFF69C8FF)
        override val _800: Color = Color(0xFF8FD6FF)
        override val _900: Color = Color(0xFFC2E9FF)
        override val _950: Color = Color(0xFFE3F5FD)
    }
}

object NestTN : ColorMode<ColorPalette> {

    override val light: ColorPalette = object : LightColorPalette() {
        override val _50: Color = Color(0xFFEDFFF9)
        override val _100: Color = Color(0xFFC2FFF0)
        override val _200: Color = Color(0xFF69F2E2)
        override val _300: Color = Color(0xFF39E5D1)
        override val _400: Color = Color(0xFF11CEBC)
        override val _500: Color = Color(0xFF009F92)
        override val _600: Color = Color(0xFF04837E)
        override val _700: Color = Color(0xFF056D6A)
        override val _800: Color = Color(0xFF135351)
        override val _900: Color = Color(0xFF0A3D3D)
        override val _950: Color = Color(0xFF042F31)
    }

    override val dark: ColorPalette = object : DarkColorPalette() {
        override val _50: Color = Color(0xFF0D2624)
        override val _100: Color = Color(0xFF004A3F)
        override val _200: Color = Color(0xFF005C54)
        override val _300: Color = Color(0xFF00756C)
        override val _400: Color = Color(0xFF00877E)
        override val _500: Color = Color(0xFF009C93)
        override val _600: Color = Color(0xFF00A6A0)
        override val _700: Color = Color(0xFF34B6B1)
        override val _800: Color = Color(0xFF76CBC8)
        override val _900: Color = Color(0xFFADDFDE)
        override val _950: Color = Color(0xFFDEF2F2)
    }
}
