package com.tokopedia.content.product.preview.view.components

import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import com.tokopedia.nest.components.NestImage
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.ImageSource

@Composable
fun ItemImageProductPreview(imageUrl: String) {
    val scale = remember { mutableStateOf(1f) }
    val offset = remember { mutableStateOf(Offset(0f, 0f)) }

    NestTheme(darkTheme = true) {
        NestImage(
            source = ImageSource.Remote(
                source = imageUrl,
                loaderType = ImageSource.Remote.LoaderType.NONE
            ),
            modifier = Modifier
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
        )
    }
}
