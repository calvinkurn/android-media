package com.tokopedia.report.view.fragment.unify_components

import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

/**
 * Created by yovi.putra on 07/09/22"
 * Project name: android-tokopedia-core
 **/

sealed class TextUnifyWeight(val value: Int) {
    object Regular: TextUnifyWeight(value = 1)
    object Bold: TextUnifyWeight(value = 2)
}

sealed class TextUnifyType(
    open val fontSize: TextUnit,
    open val openSourceSize: TextUnit,
) {
    object Heading1: TextUnifyType(fontSize = 24.sp, openSourceSize = 28.sp)
    object Heading2: TextUnifyType(fontSize = 20.sp, openSourceSize = 20.sp)
    object Heading3: TextUnifyType(fontSize = 18.sp, openSourceSize = 18.sp)
    object Heading4: TextUnifyType(fontSize = 16.sp, openSourceSize = 16.sp)
    object Heading5: TextUnifyType(fontSize = 14.sp, openSourceSize = 14.sp)
    object Heading6: TextUnifyType(fontSize = 12.sp, openSourceSize = 12.sp)
    object Body1: TextUnifyType(fontSize = 16.sp, openSourceSize = 16.sp)
    object Body2: TextUnifyType(fontSize = 14.sp, openSourceSize = 14.sp)
    object Body3: TextUnifyType(fontSize = 12.sp, openSourceSize = 12.sp)
    object Small: TextUnifyType(fontSize = 10.sp, openSourceSize = 10.sp)
    object Display1: TextUnifyType(fontSize = 16.sp, openSourceSize = 16.sp)
    object Display2: TextUnifyType(fontSize = 14.sp, openSourceSize = 14.sp)
    object Display3: TextUnifyType(fontSize = 12.sp, openSourceSize = 12.sp)
    object Paragraph1: TextUnifyType(fontSize = 16.sp, openSourceSize = 16.sp)
    object Paragraph2: TextUnifyType(fontSize = 14.sp, openSourceSize = 14.sp)
    object Paragraph3: TextUnifyType(fontSize = 12.sp, openSourceSize = 12.sp)
    object Display3Uppercase: TextUnifyType(fontSize = 12.sp, openSourceSize = 12.sp)
    data class Custom(val size: TextUnit): TextUnifyType(fontSize = size, openSourceSize = size)
}