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

internal data class NestTypography(
    val display1 : TextStyle = TextStyle(
        fontFamily = OpenSauce,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    val display2 : TextStyle = TextStyle(
        fontFamily = OpenSauce,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    val display3 : TextStyle = TextStyle(
        fontFamily = OpenSauce,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
)

internal val LocalTypography = staticCompositionLocalOf { NestTypography() }
