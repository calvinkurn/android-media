package com.tokopedia.content.product.preview.view.components.items

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntSize
import com.tokopedia.nest.components.NestImage
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.ImageSource

@Composable
internal fun ItemImageProductPreview(
    imageUrl: String,
    onDoubleTap: (() -> Unit)? = null,
    stateListener: ((isZoomMode: Boolean) -> Unit)? = null
) {
    var zoom by remember { mutableStateOf(MIN_ZOOM) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var isZoomMode by remember { mutableStateOf(false) }

    NestTheme(darkTheme = true) {
        BoxWithConstraints(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            NestImage(
                source = ImageSource.Remote(
                    source = imageUrl,
                    loaderType = ImageSource.Remote.LoaderType.NONE
                ),
                modifier = Modifier
                    .clipToBounds()
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onDoubleTap = { onDoubleTap?.invoke() }
                        )
                    }
                    .pointerInput(Unit) {
                        detectTransformGestures { centroid, pan, zoomChange, _ ->
                            offset = offset.calculateNewOffset(centroid, pan, zoom, zoomChange, size)
                            zoom = maxOf(MIN_ZOOM, zoom * zoomChange)

                            isZoomMode = zoom != MIN_ZOOM
                            stateListener?.invoke(isZoomMode)
                        }
                    }
                    .graphicsLayer(
                        scaleX = zoom,
                        scaleY = zoom,
                        translationX = -offset.x * zoom,
                        translationY = -offset.y * zoom,
                        transformOrigin = TransformOrigin(MIN_OFFSET, MIN_OFFSET)
                    )
                    .fillMaxSize()
            )
        }
    }
}

private fun Offset.calculateNewOffset(
    centroid: Offset,
    pan: Offset,
    zoom: Float,
    gestureZoom: Float,
    size: IntSize
): Offset {
    val newScale = maxOf(MIN_ZOOM, zoom * gestureZoom)
    val newOffset = (this + centroid / zoom) -
        (centroid / newScale + pan / zoom)
    return Offset(
        x = newOffset.x.coerceIn(MIN_OFFSET, (size.width / zoom) * (zoom - MIN_ZOOM)),
        y = newOffset.y.coerceIn(MIN_OFFSET, (size.height / zoom) * (zoom - MIN_ZOOM))
    )
}

private const val MIN_OFFSET = 0F
private const val MIN_ZOOM = 1f
