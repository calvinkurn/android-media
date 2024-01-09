package com.tokopedia.tokopedianow.annotation.presentation.constant

import androidx.annotation.StringDef

@Retention(AnnotationRetention.SOURCE)
@StringDef(
    AnnotationWidgetId.BRAND,
)
annotation class AnnotationWidgetId {
    companion object {
        const val BRAND = "brand_widget"
    }
}
