package com.tokopedia.common_compose.extensions

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.unit.Dp

/**
 * Created by yovi.putra on 27/10/22"
 * Project name: android-tokopedia-core
 **/

fun Modifier.tag(tag: String) = semantics {
    testTag = tag
    contentDescription = tag
}

fun Modifier.dashedStroke(
    strokeSize: Dp,
    cornerRadius: Dp,
    color: Color,
    width: Dp,
    gap: Dp = width,
) = this.then(
    drawWithCache {
        onDrawBehind {
            val stroke = Stroke(
                width = strokeSize.toPx(),
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(width.toPx(), gap.toPx()), 0f)
            )

            drawRoundRect(
                color = color,
                style = stroke,
                cornerRadius = CornerRadius(cornerRadius.toPx())
            )
        }
    }
)

fun Modifier.applyIf(condition: Boolean, modifier: Modifier.() -> Modifier) = this.then(
    if (condition) modifier()
    else this
)
