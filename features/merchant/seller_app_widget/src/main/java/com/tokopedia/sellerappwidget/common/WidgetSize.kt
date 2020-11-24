package com.tokopedia.sellerappwidget.common

import androidx.annotation.StringDef

/**
 * Created By @ilhamsuaib on 23/11/20
 */

@StringDef(WidgetSize.SMALL, WidgetSize.NORMAL, WidgetSize.LARGE)
@Retention(AnnotationRetention.SOURCE)
annotation class WidgetSize {

    companion object {
        const val SMALL = "SMALL" //4 x 2
        const val NORMAL = "NORMAL" //4 x 3
        const val LARGE = "LARGE" //4 x (>3)
    }
}