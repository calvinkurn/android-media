package com.tokopedia.common_compose.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.platform.ViewConfiguration
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

/**
 * Created by kenny.hadisaputra on 17/05/23
 */

@Composable
fun NoMinimumTouchViewConfiguration(): ViewConfiguration {
    val currentConfig = LocalViewConfiguration.current

    return object : ViewConfiguration {
        override val doubleTapMinTimeMillis: Long
            get() = currentConfig.doubleTapMinTimeMillis

        override val doubleTapTimeoutMillis: Long
            get() = currentConfig.doubleTapTimeoutMillis

        override val longPressTimeoutMillis: Long
            get() = currentConfig.longPressTimeoutMillis

        override val touchSlop: Float
            get() = currentConfig.touchSlop

        override val minimumTouchTargetSize: DpSize
            get() = DpSize(0.dp, 0.dp)
    }
}
