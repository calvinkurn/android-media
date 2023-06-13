package com.tokopedia.common_compose.extensions

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.TileMode
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

fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = composed {
    this.clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        onClick = onClick,
    )
}

// still dummy
internal fun Modifier.shimmerBackground(shape: Shape = RectangleShape): Modifier = composed {
    val transition = rememberInfiniteTransition()
    val translateAnimation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            tween(durationMillis = 1000, easing = LinearEasing),
            RepeatMode.Restart
        )
    )
    val shimmerColors = listOf(
        Color(0xFFD6DFEB),
        Color(0xFFE4EBF5),
        Color(0xFFD6DFEB)
    )
    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnimation, translateAnimation),
        end = Offset(translateAnimation + 100f, translateAnimation + 30f),
        tileMode = TileMode.Clamp
    )
    return@composed this.then(background(brush, shape))
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

fun Modifier.clickableWithoutRipple(
    interactionSource: MutableInteractionSource,
    onClick: () -> Unit
) = composed(
    factory = {
        this.then(
            Modifier.clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
        )
    }
)
