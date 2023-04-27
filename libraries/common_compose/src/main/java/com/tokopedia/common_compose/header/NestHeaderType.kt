package com.tokopedia.common_compose.header

import androidx.compose.runtime.Composable

/**
 * Created by yovi.putra on 18/04/23"
 * Project name: android-tokopedia-core
 **/

sealed interface NestHeaderType {
    data class Location(
        override val showBackButton: Boolean = true,
        override val onBackClicked: () -> Unit = {},
        override val buttonOptions: @Composable () -> Unit = {},
        override val title: String = "Title",
        override val onTitleClicked: () -> Unit = {},
        override val subTitle: String = "Sub-title"
    ) : NestHeaderType, NestHeaderDoubleLineClickableAttr

    data class SingleLine(
        override val showBackButton: Boolean = true,
        override val onBackClicked: () -> Unit = {},
        override val buttonOptions: @Composable () -> Unit = {},
        override val title: String = "Title"
    ) : NestHeaderType, NestHeaderSingleLineAttr

    data class DoubleLine(
        override val showBackButton: Boolean = true,
        override val onBackClicked: () -> Unit = {},
        override val buttonOptions: @Composable () -> Unit = {},
        override val title: String = "Title",
        override val subTitle: String = "Sub-title"
    ) : NestHeaderType, NestHeaderDoubleLineAttr

    data class Profile(
        override val showBackButton: Boolean = true,
        override val onBackClicked: () -> Unit = {},
        override val buttonOptions: @Composable () -> Unit = {},
        override val title: String = "Title",
        override val subTitle: String = "Sub-title",
        override val onTitleClicked: () -> Unit = {},
        val imageSource: ProfileSource = ProfileSource.Remote(source = "")
    ) : NestHeaderType, NestHeaderDoubleLineClickableAttr

    data class Search(
        override val showBackButton: Boolean = true,
        override val onBackClicked: () -> Unit = {},
        override val buttonOptions: @Composable () -> Unit = {},
        val hint: String = "",
        val value: String = "",
        val onSearchChanges: (String) -> Unit = {},
        val onSearchKeyPressed: () -> Unit = {}
    ) : NestHeaderType, NestHeaderAttr
}

interface NestHeaderAttr {
    val showBackButton: Boolean
    val onBackClicked: () -> Unit
    val buttonOptions: @Composable () -> Unit
}

interface NestHeaderSingleLineAttr : NestHeaderAttr {
    val title: String
}

interface NestHeaderDoubleLineAttr : NestHeaderSingleLineAttr {
    val subTitle: String
}

interface NestHeaderDoubleLineClickableAttr : NestHeaderDoubleLineAttr {
    val onTitleClicked: () -> Unit
}

sealed interface ProfileSource {
    data class Remote(val source: String) : ProfileSource
    data class Painter(
        val source: androidx.compose.ui.graphics.painter.Painter
    ) : ProfileSource

    data class ImageVector(
        val source: androidx.compose.ui.graphics.vector.ImageVector
    ) : ProfileSource

    data class ImageBitmap(
        val source: androidx.compose.ui.graphics.ImageBitmap
    ) : ProfileSource
}
