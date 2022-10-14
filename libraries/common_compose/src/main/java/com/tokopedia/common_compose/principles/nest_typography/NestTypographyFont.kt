package com.tokopedia.common_compose.principles.nest_typography

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.tokopedia.common_compose.R

/**
 * Created by yovi.putra on 07/10/22"
 * Project name: android-tokopedia-core
 **/

internal val fontOpenSourceOneExtraBold = FontFamily(Font(R.font.nunito_sans_extra_bold))
internal val fontOpenSourceOneRegular = FontFamily(Font(R.font.open_sauce_one_regular))
internal val fontRobotoRegular = FontFamily(Font(R.font.roboto_regular))
internal val fontRobotoBold = FontFamily(Font(R.font.roboto_bold))
internal val fontNunitoSansExtraBold = FontFamily(Font(R.font.nunito_sans_extra_bold))

internal val fontOpenSourceOne = FontFamily(
    Font(R.font.open_sauce_one_regular, weight = FontWeight.Normal),
    Font(R.font.nunito_sans_extra_bold, weight = FontWeight.Bold)
)

internal val fontRobot = FontFamily(
    Font(R.font.roboto_regular, weight = FontWeight.Normal),
    Font(R.font.roboto_bold, weight = FontWeight.Bold)
)

object NestTextFontConfig {

    var isFontTypeOpenSauceOne: Boolean = true
}