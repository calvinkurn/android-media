package com.tokopedia.common_compose.header

import androidx.compose.ui.graphics.Color

/**
 * Created by yovi.putra on 27/04/23"
 * Project name: android-tokopedia-core
 **/

interface HeaderOptionals {
    val color: Color
    val onClicked: () -> Unit
}

data class HeaderNotification(
    val counter: Int,
    val color: Color
)

data class HeaderActionButton(
    override val color: Color,
    override val onClicked: () -> Unit,
    val notification: HeaderNotification?,
    val icon: HeaderImageSource
) : HeaderOptionals

data class HeaderTextButton(
    override val color: Color,
    override val onClicked: () -> Unit,
    val text: String
) : HeaderOptionals
