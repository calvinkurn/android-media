package com.tokopedia.common_compose.header

import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

/**
 * Created by yovi.putra on 27/04/23"
 * Project name: android-tokopedia-core
 **/
sealed interface HeaderIconSource {
    data class Painter(
        val source: androidx.compose.ui.graphics.painter.Painter
    ) : HeaderIconSource

    data class ImageVector(
        val source: androidx.compose.ui.graphics.vector.ImageVector
    ) : HeaderIconSource

    data class ImageBitmap(
        val source: androidx.compose.ui.graphics.ImageBitmap
    ) : HeaderIconSource
}

/**
 * Header Image Mapper
 */
@Composable
internal fun HeaderIcon(
    modifier: Modifier = Modifier,
    imageSource: HeaderIconSource,
    contentDescription: String,
    iconColor: Color
) {
    when (imageSource) {
        is HeaderIconSource.Painter -> {
            Icon(
                modifier = modifier,
                painter = imageSource.source,
                contentDescription = contentDescription,
                tint = iconColor
            )
        }
        is HeaderIconSource.ImageBitmap -> {
            Icon(
                modifier = modifier,
                bitmap = imageSource.source,
                contentDescription = contentDescription,
                tint = iconColor
            )
        }
        is HeaderIconSource.ImageVector -> {
            Icon(
                modifier = modifier,
                imageVector = imageSource.source,
                contentDescription = contentDescription,
                tint = iconColor
            )
        }
    }
}
