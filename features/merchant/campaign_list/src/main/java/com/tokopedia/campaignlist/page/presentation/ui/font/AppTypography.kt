package com.tokopedia.campaignlist.page.presentation.ui.font

import androidx.compose.material.Typography
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.tokopedia.campaignlist.R


private val OpenSauce = FontFamily(
    Font(R.font.open_saunce_one_regular, FontWeight.Normal),
    Font(R.font.open_sauce_one_extra_bold, FontWeight.Bold)
)

data class AppTypography(
    val h1: TextStyle = TextStyle(
        fontFamily = OpenSauce,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp
    ),
    val subtitle: TextStyle = TextStyle(
        fontFamily = OpenSauce,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    val body: TextStyle = TextStyle(
        fontFamily = OpenSauce,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    val button: TextStyle = TextStyle(
        fontFamily = OpenSauce,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    val caption: TextStyle = TextStyle(
        fontFamily = OpenSauce,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
)
internal val LocalTypography = staticCompositionLocalOf { AppTypography() }



val OpenSauceTypography = Typography(
    defaultFontFamily = OpenSauce,
    h1 = TextStyle(fontWeight = FontWeight.Bold, fontSize = 96.sp, letterSpacing = (-1.5).sp)
    //h4, subtitle, body1, etc.
)
