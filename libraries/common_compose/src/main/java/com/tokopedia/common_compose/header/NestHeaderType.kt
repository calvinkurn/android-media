package com.tokopedia.common_compose.header

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Created by yovi.putra on 18/04/23"
 * Project name: android-tokopedia-core
 **/

sealed interface NestHeaderType {
    // elevation will be automatically set to 0dp when nest header variants are transparent
    val elevation: Dp

    data class Location(
        override val elevation: Dp = 1.dp,
        override val showBackButton: Boolean = true,
        override val onBackClicked: () -> Unit = {},
        override val optionsButton: List<HeaderOptionals> = emptyList(),
        override val title: String = "Title",
        override val onTitleClicked: () -> Unit = {},
        override val subTitle: String = "Sub-title"
    ) : NestHeaderType, NestHeaderDoubleLineClickableAttr

    data class SingleLine(
        override val elevation: Dp = 1.dp,
        override val showBackButton: Boolean = true,
        override val onBackClicked: () -> Unit = {},
        override val optionsButton: List<HeaderOptionals> = emptyList(),
        override val title: String = "Title"
    ) : NestHeaderType, NestHeaderSingleLineAttr

    data class DoubleLine(
        override val elevation: Dp = 1.dp,
        override val showBackButton: Boolean = true,
        override val onBackClicked: () -> Unit = {},
        override val optionsButton: List<HeaderOptionals> = emptyList(),
        override val title: String = "Title",
        override val subTitle: String = "Sub-title"
    ) : NestHeaderType, NestHeaderDoubleLineAttr

    data class Profile(
        override val elevation: Dp = 1.dp,
        override val showBackButton: Boolean = true,
        override val onBackClicked: () -> Unit = {},
        override val optionsButton: List<HeaderOptionals> = emptyList(),
        override val title: String = "Title",
        override val subTitle: String = "Sub-title",
        override val onTitleClicked: () -> Unit = {},
        val imageSource: HeaderImageSource = HeaderImageSource.Remote(source = "")
    ) : NestHeaderType, NestHeaderDoubleLineClickableAttr

    data class Search(
        override val elevation: Dp = 1.dp,
        override val showBackButton: Boolean = true,
        override val onBackClicked: () -> Unit = {},
        override val optionsButton: List<HeaderOptionals> = emptyList(),
        val hint: String = "",
        val value: String = "",
        val onSearchChanges: (String) -> Unit = {},
        val onSearchKeyPressed: () -> Unit = {}
    ) : NestHeaderType, NestHeaderAttr

    // TODO this need add type for custom view
}

internal interface NestHeaderAttr {
    val showBackButton: Boolean
    val onBackClicked: () -> Unit
    val optionsButton: List<HeaderOptionals>
}

internal interface NestHeaderSingleLineAttr : NestHeaderAttr {
    val title: String
}

internal interface NestHeaderDoubleLineAttr : NestHeaderSingleLineAttr {
    val subTitle: String
}

internal interface NestHeaderDoubleLineClickableAttr : NestHeaderDoubleLineAttr {
    val onTitleClicked: () -> Unit
}
