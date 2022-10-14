package com.tokopedia.common_compose.principles.nest_typography

import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

/**
 * Created by yovi.putra on 07/09/22"
 * Project name: android-tokopedia-core
 **/

enum class NestTypographyType(
    open val fontSize: TextUnit,
    open val openSourceSize: TextUnit,
) {
    Heading1(fontSize = 24.sp, openSourceSize = 28.sp),
    Heading2(fontSize = 20.sp, openSourceSize = 20.sp),
    Heading3(fontSize = 18.sp, openSourceSize = 18.sp),
    Heading4(fontSize = 16.sp, openSourceSize = 16.sp),
    Heading5(fontSize = 14.sp, openSourceSize = 14.sp),
    Heading6(fontSize = 12.sp, openSourceSize = 12.sp),
    Body1(fontSize = 16.sp, openSourceSize = 16.sp),
    Body2(fontSize = 14.sp, openSourceSize = 14.sp),
    Body3(fontSize = 12.sp, openSourceSize = 12.sp),
    Small(fontSize = 10.sp, openSourceSize = 10.sp),
    Display1(fontSize = 16.sp, openSourceSize = 16.sp),
    Display2(fontSize = 14.sp, openSourceSize = 14.sp),
    Display3(fontSize = 12.sp, openSourceSize = 12.sp),
    Paragraph1(fontSize = 16.sp, openSourceSize = 16.sp),
    Paragraph2(fontSize = 14.sp, openSourceSize = 14.sp),
    Paragraph3(fontSize = 12.sp, openSourceSize = 12.sp),
    Display3Uppercase(fontSize = 12.sp, openSourceSize = 12.sp)
}