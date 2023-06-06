package com.tokopedia.common_compose.components.card

import android.animation.TimeInterpolator
import androidx.compose.animation.core.Easing
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.MutableState
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.core.view.animation.PathInterpolatorCompat

internal const val CARD_TRANSITION_DURATION = 300
internal const val LONG_PRESS_DURATION = 400

internal fun TimeInterpolator.toEasing() = Easing { x ->
    getInterpolation(x)
}

internal val bezierCustomInterpolator = PathInterpolatorCompat.create(
    .2f, .64f, .21f, 1f
).toEasing()

internal suspend fun PointerInputScope.configGesture(
    onTouch: MutableState<Boolean>,
    onClick: () -> Unit,
    onLongPress: () -> Unit
) {
    detectTapGestures(
        onPress = { offset ->
            val currentTimePress = System.currentTimeMillis()
            onTouch.value = true

            /**
             * Return true if the interaction purely click
             * return false if the interaction being consume by another gesture eg: click+dragging
             * This will fix when user click and scroll at the same time
             */
            val noScrollPress = tryAwaitRelease()
            onTouch.value = false

            val clickDuration = System.currentTimeMillis() - currentTimePress
            if (clickDuration < LONG_PRESS_DURATION && noScrollPress) {
                onClick.invoke()
            }
        },
        onDoubleTap = { offset ->

        },
        onLongPress = { offset ->
            onLongPress.invoke()
        }, onTap = { offset ->
        })
}