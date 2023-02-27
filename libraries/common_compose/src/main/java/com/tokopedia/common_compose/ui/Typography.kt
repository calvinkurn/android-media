package com.tokopedia.common_compose.ui

import androidx.compose.material.Typography
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.tokopedia.common_compose.R

private val OpenSauce = FontFamily(
    Font(R.font.open_sauce_one_regular, FontWeight.Normal),
    Font(R.font.open_sauce_one_extra_bold, FontWeight.Bold)
)

val OpenSauceTypography = Typography(defaultFontFamily = OpenSauce)

data class NestTextStyle(
    val heading1: TextStyle = TextStyle(
        fontFamily = OpenSauce,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        letterSpacing = (-0.0083f).sp
    ),
    val heading2: TextStyle = TextStyle(
        fontFamily = OpenSauce,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        letterSpacing = (-0.005f).sp
    ),
    val heading3: TextStyle = TextStyle(
        fontFamily = OpenSauce,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        letterSpacing = (-0.0055f).sp
    ),
    val heading4: TextStyle = TextStyle(
        fontFamily = OpenSauce,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    ),
    val heading5: TextStyle = TextStyle(
        fontFamily = OpenSauce,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp
    ),
    val heading6: TextStyle = TextStyle(
        fontFamily = OpenSauce,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp
    ),
    val body1: TextStyle = TextStyle(
        fontFamily = OpenSauce,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    val body2: TextStyle = TextStyle(
        fontFamily = OpenSauce,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    val body3: TextStyle = TextStyle(
        fontFamily = OpenSauce,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    ),
    val small: TextStyle = TextStyle(
        fontFamily = OpenSauce,
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp,
        letterSpacing = 0.01f.sp
    ),
    val display1: TextStyle = TextStyle(
        fontFamily = OpenSauce,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        letterSpacing = 0.00625f.sp
    ),
    val display2: TextStyle = TextStyle(
        fontFamily = OpenSauce,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        letterSpacing = 0.01428f.sp
    ),
    val display3: TextStyle = TextStyle(
        fontFamily = OpenSauce,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        letterSpacing = 0.00833f.sp
    ),
    val paragraph1: TextStyle = TextStyle(
        fontFamily = OpenSauce,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        letterSpacing = 0.00625f.sp
        // TODO line spacing top 3.sp bottom 3.sp, already in 1.2 version
    ),
    val paragraph2: TextStyle = TextStyle(
        fontFamily = OpenSauce,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        letterSpacing = 0.01428f.sp
        // TODO line spacing top 2.sp bottom 2.sp, already in 1.2 version
    ),
    val paragraph3: TextStyle = TextStyle(
        fontFamily = OpenSauce,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        letterSpacing = 0.00833f.sp
        // TODO line spacing top 1.sp bottom 1.sp, already in 1.2 version
    )
)

internal val LocalTypography = staticCompositionLocalOf { NestTextStyle() }
