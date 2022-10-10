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

internal val LocalTypography = staticCompositionLocalOf { AppTypography() }


val OpenSauceTypography = Typography(defaultFontFamily = OpenSauce)
