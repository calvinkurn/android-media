package com.tokopedia.common_compose.card2

import android.animation.TimeInterpolator
import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FloatTweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.animation.PathInterpolatorCompat
import com.tokopedia.common_compose.R
import com.tokopedia.common_compose.ui.NestTheme

private fun TimeInterpolator.toEasing() = Easing { x ->
    getInterpolation(x)
}

private val customInterpolator = PathInterpolatorCompat
    .create(.2f, .64f, .21f, 1f)
    .toEasing()

@Composable
private fun borderSelected(): BorderStroke {
    return BorderStroke(
        2.dp,
        NestTheme.colors.GN._500
    )
}

@Composable
private fun borderDisabled(): BorderStroke {
    return BorderStroke(
        2.dp,
        NestTheme.colors.NN._200
    )
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
}

private suspend fun PointerInputScope.configGesture(
    onTouch: MutableState<Boolean>,
    onClick: () -> Unit,
    onLongPress: () -> Unit
) {
    detectTapGestures(
        onPress = { offset ->
            val currentTimePress = System.currentTimeMillis()
            onTouch.value = true

            tryAwaitRelease()
            onTouch.value = false

            val clickDuration = System.currentTimeMillis() - currentTimePress
            if (clickDuration < 400) {
                onClick.invoke()
            }
        },
        onDoubleTap = { offset -> },
        onLongPress = { offset ->
            onLongPress.invoke()
        },
        onTap = { offset -> }
    )
}

@Composable
fun Card2Unify(
    enableBounceAnimation: Boolean = true,
    type: Card2Border = Card2Border.NoBorder,
    content: @Composable () -> Unit
) {
    val onTouch = remember { mutableStateOf(false) }
    val scale = if (enableBounceAnimation) {
        animateFloatAsState(
            if (onTouch.value) 0.95F else 1.01f,
            animationSpec = FloatTweenSpec(
                300,
                0,
                easing = customInterpolator
            )
        ).value
    } else {
        1F
    }

    var border: BorderStroke? = null
    var backgroundColor = MaterialTheme.colors.surface
    var shadow = false

    when (type) {
        is Card2Border.Border -> {
            border = borderSelected()
        }
        is Card2Border.Shadow -> {
            shadow = true
        }
        is Card2Border.BorderActive -> {
            border = borderSelected()
            backgroundColor = NestTheme.colors.GN._50
        }
        is Card2Border.ShadowActive -> {
            shadow = true
            border = borderSelected()
            backgroundColor = NestTheme.colors.GN._50
        }
        is Card2Border.BorderDisabled -> {
            border = borderDisabled()
            backgroundColor = NestTheme.colors.NN._50
        }
        is Card2Border.ShadowDisabled -> {
            border = borderDisabled()
            shadow = true
            backgroundColor = NestTheme.colors.NN._50
        }
        else -> {
            Color.Transparent
        }
    }

    Card(
        modifier = Modifier
            .wrapContentSize()
            .padding(16.dp)
            .scale(scale)
            .pointerInput(Unit) {
                configGesture(onTouch = onTouch,
                    onClick = {
                        Log.e("click", "click")
                    },
                    onLongPress = {
                        Log.e("click", "long click")
                    })
            },
        border = border,
        backgroundColor = backgroundColor,
        elevation = if (shadow) Integer.MAX_VALUE.dp else 0.dp,
        shape = RoundedCornerShape(16.dp)
    ) {
        Box {
            BoxCustomRipple(this, enableBounceAnimation, onTouch.value)

            content()
        }
    }
}

@Composable
fun BoxCustomRipple(
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
                    targetValue = 0.2F,
                    animationSpec = tween(
                        durationMillis = 120,
                        easing = CubicBezierEasing(.2f, .64f, .21f, 1f)
                    )
                )
            } else {
                color.animateTo(
                    targetValue = 0F,
                    animationSpec = tween(
                        durationMillis = 120,
                        delayMillis = 100, CubicBezierEasing(.2f, .64f, .21f, 1f)
                    )
                )
            }
        }

        boxScope.run {
            Spacer(
                Modifier
                    .matchParentSize()
                    .alpha(color.value)
                    .background(Color(0xFF525A67))
            )
        }
    }
}

@Composable
@Preview
fun Card2Preview() {
    Surface {
        Card2Unify(
            enableBounceAnimation = true,
            type = Card2Border.Shadow
        ) {
            var textTest by remember {
                mutableStateOf("")
            }
//            Text(
//                modifier = Modifier.padding(16.dp),
//                text = "Open the reward"
//            )

            Column(
                modifier = Modifier.wrapContentHeight().fillMaxWidth()
            ) {

                Image(
                    painter = painterResource(id = R.drawable.png_sample),
                    contentScale = ContentScale.FillWidth,
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth().height(150.dp)
                )

                Text(
                    modifier = Modifier.padding(16.dp),
                    text = "Open the reward"
                )
                Button(modifier = Modifier.wrapContentSize(),
                    onClick = {
                        textTest = "asdasd"
                    }) {
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = textTest
                    )
                }
            }
        }
    }
}