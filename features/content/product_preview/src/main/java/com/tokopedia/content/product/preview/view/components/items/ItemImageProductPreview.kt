package com.tokopedia.content.product.preview.view.components.items

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import com.tokopedia.nest.components.NestImage
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.ImageSource

@Composable
internal fun ItemImageProductPreview(
    imageUrl: String,
    onDoubleTap: (() -> Unit)? = null,
    stateListener: ((isZoomMode: Boolean) -> Unit)? = null
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp
    val screenWidth = configuration.screenWidthDp

    var zoom by remember { mutableStateOf(MIN_ZOOM) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var isZoomMode by remember { mutableStateOf(false) }
    val state = rememberTransformableState { zoomChange, offsetChange, _ ->
        zoom = (zoom * zoomChange).coerceIn(MIN_ZOOM, MAX_ZOOM)

        val extraWidth = (zoom.minus(MIN_ZOOM)) * screenWidth
        val extraHeight = (zoom.minus(MIN_ZOOM)) * screenHeight

        val maxX = extraWidth.div(HALF)
        val maxY = extraHeight.div(HALF)

        offset = Offset(
            x = (offset.x + zoom * offsetChange.x).coerceIn(-maxX, maxX),
            y = (offset.y + zoom * offsetChange.y).coerceIn(-maxY, maxY)
        )

        // notify and stop auto scroll when isZoomMode is `true`
        isZoomMode = zoom != MIN_ZOOM
        stateListener?.invoke(isZoomMode)
    }

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
                    .transformable(state)
                    .graphicsLayer(
                        scaleX = zoom,
                        scaleY = zoom,
                        translationX = offset.x,
                        translationY = offset.y
                    )
                    .fillMaxSize()
            )
        }
    }
}

private const val HALF = 2
private const val MIN_ZOOM = 1f
private const val MAX_ZOOM = 2f
