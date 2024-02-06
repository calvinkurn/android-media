package com.tokopedia.purchase_platform.common.utils

import android.content.res.Configuration
import android.os.SystemClock
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.tooling.preview.Preview

fun Modifier.setOnClickDebounceListener(
    interval: Int = 750,
    showIndication: Boolean = true,
    enabled: Boolean = true,
    block: () -> Unit
): Modifier = composed {
    var lastClickTime = 0L

    this then clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = if (showIndication) LocalIndication.current else null,
        enabled = enabled
    ) {
        if (SystemClock.elapsedRealtime() - lastClickTime < interval) {
            return@clickable
        }

        lastClickTime = SystemClock.elapsedRealtime()
        block()
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
    showBackground = true
)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL)
annotation class LightAndDarkModePreview
