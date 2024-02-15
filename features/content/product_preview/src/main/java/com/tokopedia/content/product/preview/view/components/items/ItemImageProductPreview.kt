package com.tokopedia.content.product.preview.view.components.items

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import com.tokopedia.content.product.preview.view.components.pinchToZoomModifier
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
            modifier = Modifier.pinchToZoomModifier(scale = scale, offset = offset)
        )
    }
}
