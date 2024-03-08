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
    onDoubleTap: (() -> Unit)? = null
) {
    val configuration = LocalConfiguration.current

    val screenHeight = configuration.screenHeightDp
    val screenWidth = configuration.screenWidthDp

    var scale by remember { mutableStateOf(DEFAULT_SCALING) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val state = rememberTransformableState { zoomChange, panChange, _ ->
        scale = (scale * zoomChange).coerceIn(DEFAULT_SCALING, MAX_SCALING)

        val extraWidth = (scale.minus(DEFAULT_SCALING)) * screenWidth
        val extraHeight = (scale.minus(DEFAULT_SCALING)) * screenHeight

        val maxX = extraWidth.div(HALF)
        val maxY = extraHeight.div(HALF)

        offset = Offset(
            x = (offset.x + scale * panChange.x).coerceIn(-maxX, maxX),
            y = (offset.y + scale * panChange.y).coerceIn(-maxY, maxY)
        )
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
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onDoubleTap = { onDoubleTap?.invoke() }
                        )
                    }
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        translationX = offset.x,
                        translationY = offset.y
                    )
                    .transformable(state)
            )
        }
    }
}

private const val HALF = 2
private const val DEFAULT_SCALING = 1f
private const val MAX_SCALING = 5f
