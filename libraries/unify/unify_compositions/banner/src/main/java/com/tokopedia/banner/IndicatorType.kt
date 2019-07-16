package com.tokopedia.banner

import android.support.annotation.IntDef

class Indicator {
    @IntDef(WHITE, GREEN)
    @Retention(AnnotationRetention.SOURCE)
    annotation class Type

    companion object {
        const val WHITE = 0
        const val GREEN = 1
    }
}
