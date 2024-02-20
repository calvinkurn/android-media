package com.tokopedia.content.product.preview.view.components.items

import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import com.tokopedia.nest.components.NestImage
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.ImageSource

@Composable
fun ItemImageProductPreview(imageUrl: String) {
    var scale by remember { mutableStateOf(DEFAULT_SCALING) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val state = rememberTransformableState { zoomChange, offsetChange, _ ->
        scale *= zoomChange
        offset += offsetChange
    }

    NestTheme(darkTheme = true) {
        NestImage(
            source = ImageSource.Remote(
                source = imageUrl,
                loaderType = ImageSource.Remote.LoaderType.NONE
            ),
            modifier = Modifier
                .graphicsLayer(
                    scaleX = if (scale < DEFAULT_SCALING) DEFAULT_SCALING else scale,
                    scaleY = if (scale < DEFAULT_SCALING) DEFAULT_SCALING else scale
                )
                .transformable(state)
        )
    }
}

private const val DEFAULT_SCALING = 1f
