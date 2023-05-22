package com.tokopedia.common_compose.card2

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FloatTweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.common_compose.ui.NestTheme
import kotlinx.coroutines.launch

@Composable
private fun getNestCardBorderActiveColor(): Color {
    return NestTheme.colors.GN._500
}

@Composable
private fun getNestCardBorderColor(): Color {
    return NestTheme.colors.NN._200
}

@Composable
private fun getNestCardBackgroundColor(): Color {
    return if (isSystemInDarkTheme()) {
        NestTheme.colors.NN._50
    } else {
        NestTheme.colors.NN._0
    }
}

@Composable
private fun getNestCardDisableBackgroundColor(): Color {
    return if (isSystemInDarkTheme()) {
        NestTheme.colors.NN._100
    } else {
        NestTheme.colors.NN._50
    }
}

sealed interface NestCardType {
    data class StateBorder(val isSelected: Boolean) : NestCardType
    object Border : NestCardType
    object Shadow : NestCardType
    object BorderActive : NestCardType
    object BorderDisabled : NestCardType
    object ShadowActive : NestCardType
    object ShadowDisabled : NestCardType
    object NoBorder : NestCardType

    companion object {
        fun default(): NestCardType {
            return Border
        }
    }
}


@Composable
fun NestCard(
    modifier: Modifier = Modifier,
    enableTransitionAnimation: Boolean = false,
    enableBounceAnimation: Boolean = false,
    type: NestCardType = NestCardType.NoBorder,
    onClick: () -> Unit,
    onLongPress: () -> Unit,
    content: @Composable () -> Unit
) {
    val onTouch = remember { mutableStateOf(false) }
    val scale = if (enableBounceAnimation) {
        animateFloatAsState(
            if (onTouch.value) 0.95F else 1.01f, animationSpec = FloatTweenSpec(
                CARD_TRANSITION_DURATION, 0, easing = bezierCustomInterpolator
            )
        ).value
    } else {
        1F
    }

    var borderColor = MaterialTheme.colors.surface
    var backgroundColor = getNestCardBackgroundColor()
    var shadow = false

    when (type) {
        is NestCardType.StateBorder -> {
            if (type.isSelected) {
                shadow = true
                borderColor = getNestCardBorderActiveColor()
                backgroundColor = NestTheme.colors.GN._50
            } else {
                borderColor = getNestCardBorderActiveColor()
            }
        }
        is NestCardType.Border -> {
            borderColor = getNestCardBorderColor()
        }
        is NestCardType.Shadow -> {
            shadow = true
        }
        is NestCardType.BorderActive -> {
            borderColor = getNestCardBorderActiveColor()
            backgroundColor = NestTheme.colors.GN._50
        }
        is NestCardType.ShadowActive -> {
            shadow = true
            borderColor = getNestCardBorderActiveColor()
            backgroundColor = NestTheme.colors.GN._50
        }
        is NestCardType.BorderDisabled -> {
            borderColor = getNestCardBorderColor()
            backgroundColor = getNestCardDisableBackgroundColor()
        }
        is NestCardType.ShadowDisabled -> {
            borderColor = getNestCardBorderColor()
            shadow = true
            backgroundColor = getNestCardDisableBackgroundColor()
        }
        else -> {
        }
    }

    val animateBackgroundColor = remember { Animatable(backgroundColor) }
    val animateBorderColor = remember { Animatable(borderColor) }

    LaunchedEffect(type) {
        launch {
            animateBackgroundColor.animateTo(
                backgroundColor, animationSpec = tween(CARD_TRANSITION_DURATION)
            )
        }

        launch {
            animateBorderColor.animateTo(
                borderColor, animationSpec = tween(CARD_TRANSITION_DURATION)
            )
        }
    }

    Card(
        modifier = modifier
            .padding(16.dp)
            .scale(scale)
            .pointerInput(Unit) {
                configGesture(
                    onTouch = onTouch,
                    onClick = {
                        onClick.invoke()
                    }, onLongPress = {
                        onLongPress.invoke()
                    })
            },
        border = BorderStroke(
            1.dp, if (enableTransitionAnimation) animateBorderColor.value else borderColor
        ),
        backgroundColor = if (enableTransitionAnimation) animateBackgroundColor.value
        else backgroundColor,
        elevation = if (shadow) 2.dp else 0.dp,
        shape = RoundedCornerShape(8.dp)
    ) {
        Box {
            BoxCustomRipple(this, enableBounceAnimation, onTouch.value)

            content()
        }
    }
}

@Composable
private fun BoxCustomRipple(
    boxScope: BoxScope,
    enableAnimation: Boolean,
    clickChange: Boolean
) {
    if (enableAnimation) {
        val color = remember {
            Animatable(initialValue = 0F)
        }

        LaunchedEffect(key1 = clickChange) {
            if (clickChange) {
                color.animateTo(
                    targetValue = 0.2F, animationSpec = tween(
                        durationMillis = 120, easing = bezierCustomInterpolator
                    )
                )
            } else {
                color.animateTo(
                    targetValue = 0F, animationSpec = tween(
                        durationMillis = 120, delayMillis = 100, bezierCustomInterpolator
                    )
                )
            }
        }

        boxScope.run {
            Spacer(
                Modifier.matchParentSize().graphicsLayer {
                    //https://developer.android.com/jetpack/compose/performance/bestpractices#defer-reads
                    alpha = color.value
                }.background(Color(0xFF525A67))
            )
        }
    }
}

@Composable
@Preview
private fun NestCardMainPreview() {
    Surface {
        val type by remember { mutableStateOf(NestCardType.default()) }
        NestCard(enableBounceAnimation = true,
            type = type,
            onClick = {

            }, onLongPress = {

            }) {
            CardContentComplexExample()
        }
    }
}