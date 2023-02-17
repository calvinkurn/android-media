package com.tokopedia.product.manage.feature.list.util

import androidx.annotation.StringDef

enum class TickerType {
    INFO,WARNING
}

@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
@StringDef(TickerTypeDef.INFO, TickerTypeDef.WARNING, TickerTypeDef.DANGER)
annotation class
TickerTypeDef {
    companion object {
        const val INFO = "info"
        const val WARNING = "warning"
        const val DANGER = "danger"
    }
}
