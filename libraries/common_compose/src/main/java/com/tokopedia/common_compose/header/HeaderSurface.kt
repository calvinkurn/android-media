package com.tokopedia.common_compose.header

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.material.LocalAbsoluteElevation
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Created by yovi.putra on 27/04/23"
 * Project name: android-tokopedia-core
 **/

@Composable
internal fun HeaderSurface(
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    color: Color = MaterialTheme.colors.surface,
    contentColor: Color = contentColorFor(color),
    border: BorderStroke? = null,
    elevation: Dp = 0.dp,
    content: @Composable () -> Unit
) {
    val absoluteElevation = LocalAbsoluteElevation.current + elevation
    val clickAndSemanticsModifier = Modifier
        .semantics(mergeDescendants = false) {}
        .pointerInput(Unit) { }

    CompositionLocalProvider(
        LocalContentColor provides contentColor,
        LocalAbsoluteElevation provides absoluteElevation
    ) {
        Box(
            modifier
                .background(color = color, shape = shape)
                .shadow(elevation, shape, clip = false)
                .then(if (border != null) Modifier.border(border, shape) else Modifier)
                .clip(shape)
                .then(clickAndSemanticsModifier),
            propagateMinConstraints = true
        ) {
            content()
        }
    }
}
