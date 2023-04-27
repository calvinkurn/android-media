package com.tokopedia.common_compose.extensions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Created by yovi.putra on 27/10/22"
 * Project name: android-tokopedia-core
 **/

fun Modifier.tag(tag: String) = semantics {
    testTag = tag
    contentDescription = tag
    layoutId(tag)
}

fun Modifier.rippleClickable(
    radius: Dp = 16.dp,
    enable: Boolean = true,
    onClick: () -> Unit
): Modifier = composed {
    this.clickable(
        onClick = onClick,
        enabled = enable,
        role = Role.Button,
        interactionSource = remember { MutableInteractionSource() },
        indication = rememberRipple(bounded = false, radius = radius)
    )
}
