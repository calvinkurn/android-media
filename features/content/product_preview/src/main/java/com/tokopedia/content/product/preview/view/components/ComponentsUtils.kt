package com.tokopedia.content.product.preview.view.components

import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput

internal fun Modifier.pinchToZoomModifier(
    scale: MutableState<Float>,
    offset: MutableState<Offset>
): Modifier {
    return this
        .pointerInput(Unit) {
            detectTransformGestures { _, pan, zoom, _ ->
                val offsetX = (offset.value + pan).x
                val offsetY = (offset.value + pan).y

                scale.value *= zoom
                scale.value = scale.value.coerceIn(0f, 3f)

                offset.value = if (offsetX < 0f || offsetY < 0f) {
                    Offset(0f, 0f)
                } else {
                    offset.value + pan
                }
                scale.value = if (scale.value < 1f) 1f else scale.value
            }
        }
        .graphicsLayer(
            scaleX = scale.value,
            scaleY = scale.value,
            translationX = offset.value.x,
            translationY = offset.value.y
        )
}
