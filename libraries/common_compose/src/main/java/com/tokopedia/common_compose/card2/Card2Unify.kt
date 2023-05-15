package com.tokopedia.common_compose.card2

import android.animation.TimeInterpolator
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FloatTweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.animation.PathInterpolatorCompat
import com.tokopedia.common_compose.ui.NestTheme
import kotlinx.coroutines.launch


private const val CARD_TRANSITION_DURATION = 300
private const val LONG_PRESS_DURATION = 400

private fun TimeInterpolator.toEasing() = Easing { x ->
    getInterpolation(x)
}

private val bezierCustomInterpolator = PathInterpolatorCompat.create(
    .2f, .64f, .21f, 1f
).toEasing()

@Composable
private fun borderSelected(): Color {
    return NestTheme.colors.GN._500
}

@Composable
private fun borderDisabled(): Color {
    return NestTheme.colors.NN._200
}

sealed interface Card2Border {
    data class StateBorder(val isSelected: Boolean) : Card2Border
    object Border : Card2Border
    object Shadow : Card2Border
    object BorderActive : Card2Border
    object BorderDisabled : Card2Border
    object ShadowActive : Card2Border
    object ShadowDisabled : Card2Border
    object NoBorder : Card2Border

    companion object {
        fun default(): Card2Border {
            return Border
        }
    }
}

private suspend fun PointerInputScope.configGesture(
    onTouch: MutableState<Boolean>, onClick: () -> Unit, onLongPress: () -> Unit
) {
    detectTapGestures(onPress = { offset ->
        val currentTimePress = System.currentTimeMillis()
        onTouch.value = true

        tryAwaitRelease()
        onTouch.value = false

        val clickDuration = System.currentTimeMillis() - currentTimePress
        if (clickDuration < LONG_PRESS_DURATION) {
            onClick.invoke()
        }
    }, onDoubleTap = { offset -> }, onLongPress = { offset ->
        onLongPress.invoke()
    }, onTap = { offset -> })
}

@Composable
fun Card2Unify(
    modifier: Modifier = Modifier,
    enableTransitionAnimation: Boolean = false,
    enableBounceAnimation: Boolean = false,
    type: Card2Border = Card2Border.NoBorder,
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
    var backgroundColor = MaterialTheme.colors.surface
    var shadow = false

    when (type) {
        is Card2Border.StateBorder -> {
            if (type.isSelected) {
                shadow = true
                borderColor = borderSelected()
                backgroundColor = NestTheme.colors.GN._50
            } else {
                borderColor = borderSelected()
            }
        }
        is Card2Border.Border -> {
            borderColor = borderDisabled()
        }
        is Card2Border.Shadow -> {
            shadow = true
        }
        is Card2Border.BorderActive -> {
            borderColor = borderSelected()
            backgroundColor = NestTheme.colors.GN._50
        }
        is Card2Border.ShadowActive -> {
            shadow = true
            borderColor = borderSelected()
            backgroundColor = NestTheme.colors.GN._50
        }
        is Card2Border.BorderDisabled -> {
            borderColor = borderDisabled()
            backgroundColor = NestTheme.colors.NN._50
        }
        is Card2Border.ShadowDisabled -> {
            borderColor = borderDisabled()
            shadow = true
            backgroundColor = NestTheme.colors.NN._50
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
        modifier = modifier.padding(16.dp).scale(scale).pointerInput(Unit) {
            configGesture(onTouch = onTouch, onClick = {
                onClick.invoke()
            }, onLongPress = {
                onLongPress.invoke()
            })
        },
        border = BorderStroke(
            2.dp, if (enableTransitionAnimation) animateBorderColor.value else borderColor
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
fun BoxCustomRipple(
    boxScope: BoxScope, enableAnimation: Boolean, clickChange: Boolean
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
                Modifier.matchParentSize().alpha(color.value).background(Color(0xFF525A67))
            )
        }
    }
}

@Composable
@Preview
private fun Card2Preview() {
    Surface {
        val type by remember { mutableStateOf(Card2Border.default()) }
        Card2Unify(enableBounceAnimation = true,
            type = type,
            onClick = {

            }, onLongPress = {

            }) {
            CardContentComplexExample()
        }
    }
}