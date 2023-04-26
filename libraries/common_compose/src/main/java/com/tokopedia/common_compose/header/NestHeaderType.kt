package com.tokopedia.common_compose.header

import androidx.compose.runtime.Composable

/**
 * Created by yovi.putra on 18/04/23"
 * Project name: android-tokopedia-core
 **/

sealed interface NestHeaderType {
    data class Location(
        override val backButtonEnabled: Boolean = true,
        override val onBackClicked: () -> Unit = {},
        override val buttonOptions: @Composable () -> Unit = {},
        override val title: String = "Title",
        override val subTitle: String = "Sub-title"
    ) : NestHeaderType, NestHeaderLineAttr

    data class SingleLine(
        override val backButtonEnabled: Boolean = true,
        override val onBackClicked: () -> Unit = {},
        override val buttonOptions: @Composable () -> Unit = {},
        override val title: String = "Title"
    ) : NestHeaderType, NestHeaderTitleAttr

    data class DoubleLine(
        override val backButtonEnabled: Boolean = true,
        override val onBackClicked: () -> Unit = {},
        override val buttonOptions: @Composable () -> Unit = {},
        override val title: String = "Title",
        override val subTitle: String = "Sub-title"
    ) : NestHeaderType, NestHeaderLineAttr

    data class Profile(
        override val backButtonEnabled: Boolean = true,
        override val onBackClicked: () -> Unit = {},
        override val buttonOptions: @Composable () -> Unit = {},
        override val title: String = "Title",
        override val subTitle: String = "Sub-title",
        val icon: String = ""
    ) : NestHeaderType, NestHeaderLineAttr

    data class Search(
        override val backButtonEnabled: Boolean = true,
        override val onBackClicked: () -> Unit = {},
        override val buttonOptions: @Composable () -> Unit = {},
        val hint: String = "",
        val onSearchChanges: (String) -> Unit = {}
    ) : NestHeaderType, NestHeaderAttr
}

interface NestHeaderAttr {
    val backButtonEnabled: Boolean
    val onBackClicked: () -> Unit
    val buttonOptions: @Composable () -> Unit
}

interface NestHeaderTitleAttr: NestHeaderAttr {
    val title: String
}

interface NestHeaderLineAttr: NestHeaderTitleAttr {
    val subTitle: String
}

