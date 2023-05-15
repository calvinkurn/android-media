package com.tokopedia.common_compose.header

import androidx.compose.ui.graphics.Color
import com.tokopedia.common_compose.ui.NestGN

/**
 * Created by yovi.putra on 27/04/23"
 * Project name: android-tokopedia-core
 **/

interface HeaderOptionals {
    val onClicked: () -> Unit
    val contentDescription: String
}

data class HeaderNotification(
    val value: String,
    val color: com.tokopedia.common_compose.components.Color
)

data class HeaderActionButton(
    override val onClicked: () -> Unit = {},
    override val contentDescription: String = "",
    val notification: HeaderNotification? = null,
    val icon: HeaderIconSource
) : HeaderOptionals

data class HeaderTextButton(
    override val onClicked: () -> Unit = {},
    override val contentDescription: String = "",
    val color: Color = NestGN.light._500,
    val text: String
) : HeaderOptionals
