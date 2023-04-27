package com.tokopedia.common_compose.header

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
