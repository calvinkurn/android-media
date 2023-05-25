package com.tokopedia.common_compose.header

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.tokopedia.common_compose.components.NestImage

/**
 * Created by yovi.putra on 27/04/23"
 * Project name: android-tokopedia-core
 **/
sealed interface HeaderImageSource {
    data class Remote(val source: String) : HeaderImageSource

    data class Painter(
        val source: androidx.compose.ui.graphics.painter.Painter
    ) : HeaderImageSource

    data class ImageVector(
        val source: androidx.compose.ui.graphics.vector.ImageVector
    ) : HeaderImageSource

    data class ImageBitmap(
        val source: androidx.compose.ui.graphics.ImageBitmap
    ) : HeaderImageSource
}

/**
 * Header Image Mapper
 */
@Composable
internal fun HeaderImage(
    modifier: Modifier,
    imageSource: HeaderImageSource,
    scale: ContentScale,
    contentDescription: String
) {
    when (imageSource) {
        is HeaderImageSource.Remote -> {
            NestImage(imageUrl = imageSource.source, modifier = modifier)
        }
        is HeaderImageSource.Painter -> {
            Image(
                modifier = modifier,
                painter = imageSource.source,
                contentScale = scale,
                contentDescription = contentDescription
            )
        }
        is HeaderImageSource.ImageBitmap -> {
            Image(
                modifier = modifier,
                bitmap = imageSource.source,
                contentScale = scale,
                contentDescription = contentDescription
            )
        }
        is HeaderImageSource.ImageVector -> {
            Image(
                modifier = modifier,
                imageVector = imageSource.source,
                contentScale = scale,
                contentDescription = contentDescription
            )
        }
    }
}
